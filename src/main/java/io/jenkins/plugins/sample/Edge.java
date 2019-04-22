package io.jenkins.plugins.sample;

import java.util.List;

/*
 * Edge
from
to
arrows
dashes
color
link_status
length
mtrOutputs

        
 */
public class Edge {
	
	private int from;
	private int to;
	private String arrows = "middle";
	private String color = "{color:'green'}";
	private String linkStatus = "up";
	private int length = 300;
	private List<String> mtrOutputs;

	public Edge(int from, int to,  List<String> mtrOutputs) {
		this.from = from;
		this.to = to;
		this.mtrOutputs = mtrOutputs;
	}

	/*
	 * 
        {from: 2, to: 1, arrows:'middle', color:{color:'green'}, link_status: 'up', length: 300, 
            mtr_outputs: [ "Start: Tue Apr 16 04:38:55 2019", 
                           "HOST: foo.bar.comLoss%   Snt   Last   Avg  Best  Wrst StDev", 
                           "  1.|-- static  0.0%    10    0.2   2.2   0.2  16.0   4.9",
                           "  2.|-- core    0.0%    10    0.6   0.4   0.3   0.6   0.0",
                           "  3.|-- core      0.0%    10    5.0   5.0   4.9   5.1   0.0",
                           "  4.|-- gw1.     0.0%    10    8.6   5.8   5.3   8.6   1.0",
                           "  5.|-- 139             0.0%    10    5.6   5.6   5.5   5.6   0.0",
                           "  6.|-- 1501-2160.0%    10    5.8   5.7   5.4   6.2   0.0",
                         ] 
        },
	 */
	public String toString() {
		StringBuffer output = new StringBuffer();
		for (String mtrOutput : mtrOutputs) {
			output.append("\"");
			output.append(mtrOutput);
			output.append("\"");
			output.append(", ");
		}

		return "{"+ 
                "from: "        + from + ", " +
                "to: "     + to + ", " +
			    "arrows: '"   + arrows + "', " +
                "color: "   + color +  ", " +
			    "link_status: '"       + linkStatus + "', " +
			    "length: "      + length + ", " + 
			    "mtr_outputs: [" + output.toString() + "]" +
			     "},";
	}
}
