package data;

import main.Common;

public class FunctionLFP {
	protected String name;
	
	private double[] numerator;
	private double[] denominator;
	private String dir;
	
	public FunctionLFP(double[] numerator, double[] denominator, String dir) {
		this.name = Common.makeNameLFP("", numerator, denominator, " --> " + dir);
		this.numerator = numerator.clone();
		this.denominator = denominator.clone();
		this.dir = dir;
	}
	
	public double[] getNumerator() {
		return numerator;
	}
	
	public double[] getDenominator() {
		return denominator;
	}
	
	// get linear transformation string
	public String transformStr() {
		return Common.makeNameLFP( "y0 = ", new double[] {1}, denominator, "; yi = y0 * xi, i=1.." + getVarsAmount() );
	}
	
	public String getDir() {
		return dir;
	}
	
	public String getVarsRestrictions() {
		String res = "";
		for (int i = 0; i < getVarsAmount(); i++)
			res += "x" + (i + 1) + " >= 0; ";
		
		return res.length() == 0 ? " " : res.substring(0, res.length() - 2);
	}

	public int getVarsAmount() {
		return numerator.length-1;
	}
	
	@Override
	public String toString() {
		return name;
	}

}