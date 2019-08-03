# Introduction
jwms-mtr-plugin allow server admin to visualize network traceroute tools output in Jenkins.

# Quick start
Due to privacy concern, some part of the screenshots have been filtered out and it will be replace with text explanation below.

1. Download the hpi https://github.com/jasonwee/jwms-mtr-plugin/releases . Make sure you have mtr installed on all your hosts configured.
2. Setup a Jenkins freestyle project
3. In the build section, select 'Node Pair'.
4. Configure ssh username, ssh password and ssh port will be used between nodes. So it is advisable to have a common monitoring ssh user.
5. For node pair, entry is separated by command and a newline. For example
```
host1.mydomain.com->host2.mydomain.com,
host2.mydomain.com->host1.mydomain.com,
host3.mydomain.com->host1.mydomain.com,
host1.mydomain.com->host3.mydomain.com,
```
![configuration](https://raw.githubusercontent.com/jasonwee/jwms-mtr-plugin/master/screenshots/configuration.png "configuration")
6. Click build and check the console output
![console output](https://github.com/jasonwee/jwms-mtr-plugin/blob/master/screenshots/console_output.png "console output")
7. When the build is done, there should be a 'MTR Report' link.
8. Click on the 'MTR Report' link.
![mtr report](https://raw.githubusercontent.com/jasonwee/jwms-mtr-plugin/master/screenshots/mtr_report.png "mtr report")
9. If you click on the link, the mtr report should shown below the graph
![mtr report 1](https://raw.githubusercontent.com/jasonwee/jwms-mtr-plugin/master/screenshots/mtr_report_1.png "mtr report 1")
10. If you point the mouse cursor to the pc icon, information such as ip, hostname and country will be shown.
![device info](https://raw.githubusercontent.com/jasonwee/jwms-mtr-plugin/master/screenshots/device_info.png "device info")

# Requirements
* java8 onward
* python2 onward

# How is this project begin?
```
$ # export java 8 environment
$ JAVA_HOME=/usr/lib/jvm/jdk1.8.0_152/
$ 
$ # setup project framework
$ mvn -U archetype:generate -Dfilter=io.jenkins.archetypes:
...
[INFO] Generating project in Interactive mode
[INFO] No archetype defined. Using maven-archetype-quickstart (org.apache.maven.archetypes:maven-archetype-quickstart:1.0)
Choose archetype:
1: remote -> io.jenkins.archetypes:empty-plugin (Skeleton of a Jenkins plugin with a POM and an empty source tree.)
2: remote -> io.jenkins.archetypes:global-configuration-plugin (Skeleton of a Jenkins plugin with a POM and an example piece of global configuration.)
3: remote -> io.jenkins.archetypes:global-shared-library (Uses the Jenkins Pipeline Unit mock library to test the usage of a Global Shared Library)
4: remote -> io.jenkins.archetypes:hello-world-plugin (Skeleton of a Jenkins plugin with a POM and an example build step.)
5: remote -> io.jenkins.archetypes:scripted-pipeline (Uses the Jenkins Pipeline Unit mock library to test the logic inside a Pipeline script.)
Choose a number or apply filter (format: [groupId:]artifactId, case sensitive contains): : 4
Choose io.jenkins.archetypes:hello-world-plugin version: 
1: 1.1
2: 1.2
3: 1.3
4: 1.4
Choose a number: 4: 4
...
[INFO] Using property: groupId = unused
Define value for property 'artifactId': jwms-mtr
Define value for property 'version' 1.0-SNAPSHOT: : 
[INFO] Using property: package = io.jenkins.plugins.sample
Confirm properties configuration:
groupId: unused
artifactId: jwms-mtr
version: 1.0-SNAPSHOT
package: io.jenkins.plugins.sample
 Y: : y
```

rename directory to conform to jenkins plugin naming
```
$ mv jwms-mtr/ jwms-mtr-plugin
$ cd jwms-mtr-plugin/
$ ls
```

verify everything ok
```
$ mvn verify
```


test run jenkins locally with the plugin. jenkins can be accessible via http://localhost:8080/jenkins/
```
mvn clean hpi:run
```

how to enable eclipse support to develop this plugin?
```
mvn -DdownloadSources=true -DdownloadJavadocs=true -DoutputDirectory=target/eclipse-classes -Declipse.workspace=/home/jason/workspace eclipse:eclipse eclipse:configure-workspace
```
Once this command completes successfully, use "Import..." (under the File menu in Eclipse) and select "General" > "Existing Projects into Workspace".
Do not select "Existing Maven Projects", which takes you to the m2e route


how to build a hpi?
```
mvn -DskipTests clean package
```

# References
http://visjs.org/network_examples.html

https://wiki.jenkins.io/display/JENKINS/Plugin+tutorial
