package output;

import main.Common;

public class OutHTML extends Out {
	private static final int WIDTH = 60;
	
	public OutHTML(String letter, int di) {
		super(letter, di);
		println("<html>");
	}
	
	@Override
	public void printH1(String s) {
		println("<h1>" + s + "</h1>");
	}

	@Override
	public void printH2(String s) {
		println("<h2>" + s + "</h2>");
		
	}

	@Override
	public void printH3(String s) {
		println("<h3><font color = 'red'>" + s + "</font></h3>");
		
	}
	
	@Override
	public void printP(String s) {
		println("<p>" + s + "</p>");
		
	}

	@Override
	public void printBR(String s) {
		println(s.replace("<", "&lt;") + "<br />");
	}
	
	private String oneCell2Row(String s) {
		return "\t<td align='center' rowspan='2' width='" + WIDTH + "'><b>" + s + "</b></td>\n";
	}
	
	private String oneCell2Col(String s) {
		return "\t<td align='center' colspan='2'><b>" + s + "</b></td>\n";
	}
	
	private String oneCellCenter(String s) {
		return "\t<td align='center' width='" + WIDTH + "'><b>" + s + "</b></td>\n";
	}
	
	private String oneCell(String s, String color) {
		return "\t<td " + color + "align='right' width='" + WIDTH + "'>" + s + "</td>\n";
	}
	
	private String getBackground(int val, int cmp) {
		String color =  "";
		if (cmp != 0) 
			if (val == cmp) 
				color = "bgcolor='#ffccaa' ";
		return color;
	}
	
	private String makeTetaCell(String sv, int tetaColumn) {
		return (tetaColumn != 0) ? oneCellCenter(sv) : "";
	}
	
	// Find min x[i][0] / x[i][k] for positive x[i][k]
	private int findMinTeta(double[][] x, int m, int k) {
		int l = -1;
		if (k == 0)
			return l;
		
		double minTeta = Double.MAX_VALUE;
		for (int i = 1; i <= m; i++)
			if (x[i][k] > 0) {
				double teta = x[i][0] / x[i][k];
				if (teta < minTeta) {
					minTeta = teta;
					l = i;
				}
			}
		
		return l;
	}
	
	@Override
	public void printSimplexTable(double[][] x, int[] baseVars, int m, int n, int minArtif, int tetaColumn) {
		int minRow = findMinTeta(x, m, tetaColumn);
		
		String res = "<table border='1'>";
		
		res += "<tr>\n";
		res += oneCell2Row("i");
		res += oneCell2Row("baza");
		res += oneCell2Row("Cb");
		res += oneCell2Row("b");
		
		// title line 1
		if (minArtif == 0)
			for (int j = 1; j <= n; j++)
				res += oneCellCenter( Common.clip( x[0][j] ) );
		else {
			for (int j = 1; j < minArtif; j++)
				res += oneCellCenter( Common.clip( x[0][j] ) );
			for (int j = minArtif; j <= n; j++)
				res += oneCellCenter("w");
		}
		
		res += (tetaColumn != 0) ? oneCell2Row("teta") : "";
		res += "</tr>\n";

		// title line 2
		res += "<tr>\n";
		for (int j = 1; j <= n; j++)
			res += oneCellCenter("P" + (j-di));
		res += "</tr>\n";
		
		// table lines
		for (int i = 1; i <= m; i++) {
			res += "<tr>\n";
			
			String s;
			
			if (minArtif == 0)
				s = Common.clip( x[0][baseVars[i]] );
			else
				if (baseVars[i] < minArtif)
					s = Common.clip( x[0][baseVars[i]] );
				else
					s = "w";
					
			res += oneCellCenter("" + i);
			res += oneCellCenter(letter + (baseVars[i]-di) );
			res += oneCellCenter(s);
			
			for (int j = 0; j <= n; j++) {
				String color = getBackground(i, minRow);
				if (color.length() == 0)
					color = getBackground(j, tetaColumn);
				
				res += oneCell( Common.clip( x[i][j] ), color );
			}
				
			res += makeTetaCell( Common.clip( x[i][0] / x[i][tetaColumn] ), tetaColumn );
			res += "</tr>\n";
		}
		
		// footer line 1
		res += "<tr>\n";
		String s = (minArtif == 0) ? "z(j)-c(j)" : "";
		res += oneCellCenter("" + (m+1));
		res += oneCell2Col(s);
		res += oneCell( Common.clip( x[m+1][0] ), "" );
		
		for (int j = 1; j <= n ; j++){
			String color = getBackground(j, tetaColumn);
			res += oneCell( Common.clip( x[m+1][j] ), color );
		}
			
		res += makeTetaCell("", tetaColumn);
		res += "</tr>\n";
		
		// footer line 2
		if (minArtif != 0) {
			res += "<tr>\n";
			res += oneCellCenter("" + (m+2));
			res += oneCell2Col("");
			res += oneCell( Common.clip( x[m+2][0] ), "" );
			
			for (int j = 1; j <= n ; j++) {
				String color = getBackground(j, tetaColumn);
				res += oneCell( Common.clip( x[m+2][j] ), color );
			}
			
			res += makeTetaCell("", tetaColumn);
			res += "</tr>\n";
		}
		
		res += "</table>\n";
		
		print(res);
	}
	
	@Override
	public void printWarning(String s1, String s2, String s3) {
		printP(s1 + "<br />\n" + s2);
		printH3(s3);
	}
	
	@Override
	public String getMode() {
		return Common.HTML;
	}

}
