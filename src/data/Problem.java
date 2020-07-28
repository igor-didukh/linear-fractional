package data;

public class Problem {
	private String title;
	private FunctionLFP f;
	private Constraint[] constraints;
	private String restrictions;
	
	public Problem(String title, FunctionLFP f, Constraint[] constraints) {
		this.title = title;
		this.f = f;
		this.constraints = constraints;
	}

	public String getTitle() {
		return title;
	}
	
	public FunctionLFP getF() {
		return f;
	}

	public Constraint[] getConstraints() {
		return constraints;
	}
	
	public String getRestrictions() {
		return restrictions;
	}
	
	public void setRestrictions(String restrictions) {
		this.restrictions = restrictions;
	}

}