package output;

import main.Common;

public class OutTXT extends Out {
	
	public OutTXT(String letter, int di) {
		super(letter, di);
	}

	@Override
	public void printH1(String s) {
		println("= " + s + ": =");
		emptyLine();
	}

	@Override
	public void printH2(String s) {
		println("== " + s + ": ==");
		emptyLine();
	}

	@Override
	public void printH3(String s) {
		println("=== " + s + " ===");
		emptyLine();
	}
	
	@Override
	public void printP(String s) {
		println(s);
	}

	@Override
	public void printBR(String s) {
		println(s);
	}

	@Override
	public void printSimplexTable(double[][] x, int[] baseVars, int m, int n, int minArtif, int tetaColumn) {
		String res = " i | baza |   Cb   |   b    |";
		
		// title line 1
		if (minArtif == 0)
			for (int j = 1; j <= n; j++)
				res += String.format("%8s|", Common.clip( x[0][j] ) );
		else {
			for (int j = 1; j < minArtif; j++)
				res += String.format("%8s|", Common.clip( x[0][j] ) );
			for (int j = minArtif; j <= n; j++)
				res += "   w    |";
		}
		res += (tetaColumn != 0) ? "  teta" : "";
		res += "\n";

		// title line 2
		res += "___|______|________|________|";
		for (int j = 1; j <= n; j++)
			res += String.format("___P%1d___|", j-di);
		res += (tetaColumn != 0) ? "________" : "";
		res += "\n";
		
		// table lines
		for (int i = 1; i <= m; i++) {
			String s;
			
			if (minArtif == 0)
				s = Common.clip( x[0][baseVars[i]] );
			else
				if (baseVars[i] < minArtif)
					s = Common.clip( x[0][baseVars[i]] );
				else
					s = "   w    ";
					
			res += String.format(" %1d |  %s%1d  |%8s|", i, letter, baseVars[i]-di, s);
			
			for (int j = 0; j <= n; j++)
				res += String.format("%8s|", Common.clip( x[i][j] ) );
			
			res += (tetaColumn != 0) ? String.format("%8s", Common.clip( x[i][0] / x[i][tetaColumn] ) ) : "";
			res += "\n";
		}
		
		// separator above footer
		res += "---|---------------|--------|";
		for (int j = 1; j <= n ; j++)
			res += "--------|";
		res += (tetaColumn != 0) ? "--------" : "";
		res += "\n";

		// footer line 1
		String s = (minArtif != 0) ? "               " : "   z(j)-c(j)   ";
		res += String.format(" %1d |%s|%8s|", m+1, s, Common.clip( x[m+1][0] ));
		for (int j = 1; j <= n ; j++)
			res += String.format("%8s|", Common.clip( x[m+1][j] ));
		res += "\n";
		
		// footer line 2
		if (minArtif != 0) {
			res += String.format("_%1d_|_______________|%8s|", m+2, Common.clip( x[m+2][0] ));
			for (int j = 1; j <= n ; j++)
				res += String.format("%8s|", Common.clip( x[m+2][j] ));
			res += "\n";
		}
		
		print(res);
	}

	@Override
	public void printWarning(String s1, String s2, String s3) {
		emptyLine();
		println(s1);
		println(s2);
		
		emptyLine();
		printH3(s3);
	}

	@Override
	public String getMode() {
		return Common.TXT;
	}

}
