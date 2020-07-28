package data;

import main.Common;

public class Constraint extends Function {
	double c;
	
	public Constraint(double[] coefs, String dir, double c) {
		super(coefs, dir);
		super.name = Common.makePolynomName(coefs, dir) + " " + Common.clip(c);
		this.c = c;
	}
	
	public Constraint(double[] coefs, String letter, int initNo, String dir, double c) {
		super( coefs, letter, initNo, dir);
		super.name = Common.makePolynomName(coefs, letter, initNo, dir) + " " + Common.clip(c);
		this.c = c;
	}
	
	public double getC() {
		return c;
	}

	public void setC(double c) {
		this.c = c;
	}
	
}