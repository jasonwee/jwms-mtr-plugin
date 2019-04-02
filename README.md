export java 8 environment



how to begin?
```
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













https://wiki.jenkins.io/display/JENKINS/Plugin+tutorial