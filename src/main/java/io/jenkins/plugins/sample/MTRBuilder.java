package io.jenkins.plugins.sample;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.ServletException;

import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import com.google.common.net.InternetDomainName;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.Launcher.ProcStarter;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.FormValidation;
import jenkins.tasks.SimpleBuildStep;

public class MTRBuilder extends Builder implements SimpleBuildStep {

    private String sourceNode;
    private String destinationNode;

    @DataBoundConstructor
    public MTRBuilder(String sourceNode, String destinationNode) {
        this.sourceNode = sourceNode;
        this.destinationNode = destinationNode;
    }

    public String getSourceNode() {
        return sourceNode;
    }

    public void setSourceNode(String sourceNode) {
        this.sourceNode = sourceNode;
    }

    public String getDestinationNode() {
        return destinationNode;
    }

    public void setDestinationNode(String destinationNode) {
        this.destinationNode = destinationNode;
    }

    @Override
    public void perform(Run<?, ?> run, FilePath workspace, Launcher launcher, TaskListener listener)
            throws InterruptedException, IOException {
        long start = System.currentTimeMillis();
        listener.getLogger().println("begin " + start);

        String command = String.format("mtr -s 10 -r -c 10 %s", destinationNode);
        listener.getLogger().println("running command '" + command + "' on " + sourceNode);

        if (launcher.isUnix()) {
            ProcStarter procStarter = launcher.launch().cmdAsSingleString(command).stderr(listener.getLogger())
                    .stdout(listener.getLogger());
            procStarter.join();
        } else {
            listener.getLogger().println("support only linux/unix");
        }

        long end = System.currentTimeMillis();
        listener.getLogger().println("done " + end + " - (" + (end - start) + ")");
    }

    @Symbol("mtr-nodes")
    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

        private static final Pattern IPV4 = Pattern
                .compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
        private static final Pattern IPV6 = Pattern.compile("([0-9a-f]{1,4}:){7}([0-9a-f]){1,4}");

        public FormValidation doCheckSourceNode(@QueryParameter String value) throws IOException, ServletException {
            if (!IPV4.matcher(value).matches() && !IPV6.matcher(value).matches()
                    && !InternetDomainName.isValid(value)) {
                return FormValidation.error("Invalid input, accept only ipv4, ipv6 or a valid hostname");
            }

            return FormValidation.ok();
        }

        public FormValidation doCheckDestinationNode(@QueryParameter String value)
                throws IOException, ServletException {
            if (!IPV4.matcher(value).matches() && !IPV6.matcher(value).matches()
                    && !InternetDomainName.isValid(value)) {
                return FormValidation.error("Invalid input, accept only ipv4, ipv6 or a valid hostname");
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
