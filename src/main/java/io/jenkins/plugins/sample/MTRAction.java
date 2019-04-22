package io.jenkins.plugins.sample;

import hudson.model.Run;
import jenkins.model.RunAction2;

public class MTRAction implements RunAction2 {
	
	private transient Run run;
	private MTRReport mtrReport;
	
	public MTRAction(MTRReport mtrReport) {
		this.mtrReport = mtrReport;
	}

	@Override
	public String getIconFileName() {
		return "graph.png";
	}

	@Override
	public String getDisplayName() {
		return "MTR report";
	}

	@Override
	public String getUrlName() {
		return "mtr_report";
	}

	@Override
	public void onAttached(Run<?, ?> r) {
		this.run = r;
	}

	@Override
	public void onLoad(Run<?, ?> r) {
		this.run = r;
	}
	
	public Run getRun() {
		return run;
	}
	
	public MTRReport getMtrReport() {
		return mtrReport;
	}

}
