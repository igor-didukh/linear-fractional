package output;

public abstract class Out {
	protected String letter;
	protected int di;
	
	private String output = "";

	Out(String letter, int di) {
		this.letter = letter;
		this.di = di;
	}
	
	void print(String s) {
		output += s;
	}
	
	void println(String s) {
		output += s + "\n";
	}
	
	public void emptyLine() {
		println("");
	}
	
	public String getOut() {
		return output;
	}
	
	public abstract void printH1(String s);
	public abstract void printH2(String s);
	public abstract void printH3(String s);
	public abstract void printP(String s);
	public abstract void printBR(String s);
	public abstract void printSimplexTable(double[][] x, int[] baseVars, int m, int n, int minArtif, int tetaColumn);
	public abstract void printWarning(String s1, String s2, String s3);
	public abstract String getMode();
}
