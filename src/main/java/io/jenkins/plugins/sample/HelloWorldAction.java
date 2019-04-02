package io.jenkins.plugins.sample;

//import hudson.model.Action;
import hudson.model.Run;
import jenkins.model.RunAction2;


public class HelloWorldAction implements RunAction2 {
    
    private transient Run run;
    
    private String name;
    
    public HelloWorldAction(String name) {
        this.name = name;
    }
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String getIconFileName() {
        return "document.png";
    }

    @Override
    public String getDisplayName() {
        return "Greeting";
    }

    @Override
    public String getUrlName() {
        return "greeting";
    }


    @Override
    public void onAttached(Run<?, ?> run) {
        this.run = run;
    }


    @Override
    public void onLoad(Run<?, ?> run) {
        this.run = run;
    }

    public Run getRun() {
        return run;
    }

}
