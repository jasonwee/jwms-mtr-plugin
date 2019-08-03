package io.jenkins.plugins.sample;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.regex.Pattern;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.servlet.ServletException;

import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import com.google.common.net.InternetDomainName;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.FormValidation;
import io.jenkins.util.Command;
import io.jenkins.util.IP;
import io.jenkins.util.IP2Location;
import jenkins.tasks.SimpleBuildStep;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class MTRBuilder extends Builder implements SimpleBuildStep {

    private String nodes;

    private String sshUsername;
    private String sshPassword;
    private int sshPort;

    private String ip2locationLib;

    private StringBuffer mtrOutputs;
    private Map<String, Integer> ids;
    private Integer id;

    @DataBoundConstructor
    public MTRBuilder(String nodes, String sshUsername, String sshPassword, int sshPort, String ip2locationLib) {
        this.nodes = nodes;
        this.sshUsername = sshUsername;
        this.sshPassword = sshPassword;
        this.sshPort = sshPort;
        this.ip2locationLib = ip2locationLib;
    }

    public String getNodes() {
        return nodes;
    }

    public void setNodes(String nodes) {
        this.nodes = nodes;
    }

    public String getSshUsername() {
        return sshUsername;
    }

    public void setSshUsername(String sshUsername) {
        this.sshUsername = sshUsername;
    }

    public String getSshPassword() {
        return sshPassword;
    }

    public void setSshPassword(String sshPassword) {
        this.sshPassword = sshPassword;
    }

    public int getSshPort() {
        return sshPort;
    }

    public void setSshPort(int sshPort) {
        this.sshPort = sshPort;
    }

    public String getIp2locationLib() {
        return ip2locationLib;
    }

    public void setIp2locationLib(String ip2locationLib) {
        this.ip2locationLib = ip2locationLib;
    }

    @Override
    public void perform(Run<?, ?> run, FilePath workspace, Launcher launcher, TaskListener listener)
            throws InterruptedException, IOException {

        if (!launcher.isUnix()) {
            listener.getLogger().println("support only linux/unix");
            return;
        }

        IP2Location ip2location = new IP2Location(getIp2locationLib());
        String country = "";

        long start = System.currentTimeMillis();
        listener.getLogger().println("begin " + start);

        // has to init here cause this will not call during init ctor
        this.mtrOutputs = new StringBuffer();
        this.ids = new HashMap<String, Integer>();
        this.id = 0;

        MTRReport mtrReport = new MTRReport();
        StringBuffer mtrOutput;

        String[] nodePairs = nodes.split(",");

        for (String nodePair : nodePairs) {

            mtrOutput = new StringBuffer();

            String[] currentNodes = nodePair.split("->");

            String sNode = currentNodes[0].replaceAll("\\r\\n|\\r|\\n", "");
            String dNode = currentNodes[1].replaceAll("\\r\\n|\\r|\\n", "");

            Node n1 = new Node(sNode);
            if (!ids.containsKey(sNode)) {
                ++id;
                n1.setId(id);
                ids.put(sNode, id);
            } else {
                n1.setId(ids.get(sNode));
            }

            try {
                //country = ip2location.find(IP.resolveHostname(sNode)).getCountry_long();
                country = Command.run("python", "sample.py", IP.resolveHostname(sNode), getIp2locationLib()).trim();
            } catch (Exception e) {country = ""; }

            String title = String.format("hostname : %s <br> ip : %s <br> country: %s <br>", sNode, IP.resolveHostname(sNode), country);
            n1.setTitle(title);
            mtrReport.addNode(n1);

            Node n2 = new Node(dNode);
            if ( !ids.containsKey(dNode)) {
                ++id;
                n2.setId(id);
                ids.put(dNode, id);
            } else {
                n2.setId(ids.get(dNode));
            }
            try {
                //country = ip2location.find(IP.resolveHostname(dNode)).getCountry_long();
                country = Command.run("python", "sample.py", IP.resolveHostname(dNode), getIp2locationLib()).trim();
            } catch (Exception e) {country = "";}
            title = String.format("hostname : %s <br> ip : %s <br> country: %s <br>", dNode, IP.resolveHostname(dNode), country);
            n2.setTitle(title);
            mtrReport.addNode(n2);

            String mtrCommand = String.format("sudo mtr --report-wide -s 10 -r -c 10 %s", dNode);
            listener.getLogger().println("running command '" + mtrCommand + "' on " + sNode);

            executeCommand(listener.getLogger(), mtrCommand, false, sNode, mtrOutput);

            Edge edge = new Edge(n1.getId(), n2.getId(), Arrays.asList(mtrOutput.toString().split("\n")));
            mtrReport.addEdge(edge);
        }
        long end = System.currentTimeMillis();
        listener.getLogger().println("done " + end + " - (" + (end - start) + "ms)");

        run.addAction(new MTRAction(mtrReport) );
    }

    public int executeCommand(PrintStream logger, String command, boolean execEachLine, String sNode, StringBuffer mtrOutput) throws InterruptedException {

        Session session = null;
        int status = -1;
        try {
            session = createSession(logger, sNode);
            if (execEachLine) {
                StringTokenizer commands = new StringTokenizer(command, "\n\r\f");
                while (commands.hasMoreTokens()) {
                    int i = doExecCommand(session, logger, commands.nextToken().trim(), mtrOutput );
                    if (i != 0) {
                        status = i;
                        break;
                    }
                    // if there are no more commands to execute return the status of the last
                    // command
                    if (!commands.hasMoreTokens()) {
                        status = i;
                    }
                }
            } else {
                status = doExecCommand(session, logger, command, mtrOutput);
            }
            logger.printf("%n[SSH] completed");
            logger.printf("%n[SSH] exit-status: " + status + "%n%n");
        } catch (JSchException e) {
            logger.println("[SSH] Exception:" + e.getMessage());
            e.printStackTrace(logger);
        } catch (IOException e) {
            logger.println("[SSH] Exception:" + e.getMessage());
            e.printStackTrace(logger);
        } finally {
            if (session != null && session.isConnected()) {
                session.disconnect();
            }
        }

        return status;
    }

    private int doExecCommand(Session session, PrintStream logger, String command, StringBuffer mtrOutput)
            throws InterruptedException, IOException, JSchException {
        ChannelExec channel = null;
        int status = -1;
        try {
            channel = createChannel(logger, session);
            channel.setCommand(command);
            InputStream in = channel.getInputStream();
            channel.connect();
            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0)
                        break;
                    logger.write(tmp, 0, i);
                    mtrOutputs.append(new String(tmp, 0, i, "UTF-8"));
                    mtrOutput.append(new String(tmp, 0, i, "UTF-8"));
                }
                if (channel.isClosed()) {
                    status = channel.getExitStatus();
                    break;
                }
                logger.flush();
                Thread.sleep(1000);
            }
        } catch (JSchException e) {
            throw e;
        } finally {
            if (channel != null && channel.isConnected()) {
                channel.disconnect();
            }
        }
        return status;
    }

    private ChannelExec createChannel(final PrintStream logger, final Session session) throws JSchException {
        final ChannelExec channel = (ChannelExec) session.openChannel("exec");
        channel.setOutputStream(logger, true);
        channel.setExtOutputStream(logger, true);
        channel.setInputStream(null);
        channel.setPty(false);

        return channel;
    }

    private Session createSession(final PrintStream logger, String sNode) throws JSchException, IOException, InterruptedException {

        JSch jsch = new JSch();
        Session session = jsch.getSession(getSshUsername(), sNode, getSshPort());
        session.setPassword(getSshPassword());

        final Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);

        session.connect(60000);

        return session;
    }

    @Symbol("mtr-nodes")
    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

        private static final Pattern IPV4 = Pattern
                .compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
        private static final Pattern IPV6 = Pattern.compile("([0-9a-f]{1,4}:){7}([0-9a-f]){1,4}");

        public FormValidation doCheckNodes(@QueryParameter String value) throws IOException, ServletException {
            if (value.isEmpty()) {
                return FormValidation.error("empty nodes pair");
            }

            String[] nodePairs = value.split(",");
            if (nodePairs.length == 0) {
                return FormValidation.error("must specify at least a pair");
            }

            for (String nodePair : nodePairs) {
                String[] pair = nodePair.split("->");
                if (pair.length != 2) {
                    return FormValidation.error("invalid node pair");
                }

                String sourceNode = pair[0].trim();
                String destinationNode = pair[1].trim();

                if (!IPV4.matcher(sourceNode).matches() && !IPV6.matcher(sourceNode).matches() && !InternetDomainName.isValid(sourceNode)) {
                    return FormValidation.error("Invalid sourceNode "+sourceNode+" accept only ipv4, ipv6 or a valid hostname " + nodePair);
                }

                if (!IPV4.matcher(destinationNode).matches() && !IPV6.matcher(destinationNode).matches() && !InternetDomainName.isValid(destinationNode)) {
                    return FormValidation.error("Invalid destinationNode "+destinationNode+" accept only ipv4, ipv6 or a valid hostname " + nodePair);
                }
            }

            return FormValidation.ok();
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return "Node Pair";
        }

    }

}
