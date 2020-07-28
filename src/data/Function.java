package data;

import main.Common;

// Parent for class "Constraint"
public class Function {
	protected String name;
	
	private double[] coefs;
	private String letter;
	private int initNo;
	private String dir;
	
	public Function(double[] coefs, String dir) {
		this(coefs, "x", 1, dir);
	}
	
	public Function(double[] coefs, String letter, int initNo, String dir) {
		this.name = Common.makePolynomName(coefs, letter, initNo, "--> " + dir);
		this.coefs = coefs.clone();
		this.letter = letter;
		this.initNo = initNo;
		this.dir = dir;
	}
	
	public double[] getCoefs() {
		return coefs;
	}

	public String getLetter() {
		return letter;
	}
	
	public int getInitNo() {
		return initNo;
	}
	
	public String getDir() {
		return dir;
	}

	public int getVarsAmount() {
		return coefs.length;
	}
	
	public String getVarsRestrictions() {
		String res = "";
		for (int i = 0; i < coefs.length; i++)
			res += letter + (i + initNo) + " >= 0; ";
		
		return res.length() == 0 ? " " : res.substring(0, res.length() - 2);
	}
	
	public String getVarsVector() {
		String res = "(";
		
		for (int i = 0; i < coefs.length; i++) {
			res += letter + (i + initNo);
			if (i != coefs.length - 1)
				res += ", ";
		}
		
		res += ")";
		
		return res;
	}
	
	@Override
	public String toString() {
		return name;
	}

}