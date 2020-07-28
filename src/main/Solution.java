package main;

import data.Constraint;
import data.Function;
import output.InfoWindow;
import output.Out;
import output.OutHTML;
import output.OutTXT;

public class Solution {
	private Function fn;
	private Constraint[] cons;
	
	private int m, n, minArtif, di;
	private int[] baseVars;
	private double[][] x;
	private String letter;
	private Out out;
	
	public Solution (Function f, Constraint[] c, String outputMode) {
		fn = f;
		cons = c.clone();
		di = 1 - f.getInitNo();
		letter = f.getLetter();
		
		switch (outputMode) {
		case Common.TXT:
			out = new OutTXT(letter, di);
			break;
			
		case Common.HTML:
			out = new OutHTML(letter, di);
			break;
		}
	}
	
	public boolean initSolution() {
		boolean res = true;
		
		int listSize = cons.length;
		int varsAmount = fn.getVarsAmount();
		
		baseVars = new int[listSize + 1]; baseVars[0] = 0;
		int[] artifVars = new int[listSize + 1]; artifVars[0] = 0;
		int[] minusVars = new int[listSize + 1]; minusVars[0] = 0;
		
		String[] dirs = new String[listSize + 1];
		for (int i = 1; i <= listSize; i++)
			dirs[i] = cons[i-1].getDir();
			
		for (int i = 1; i <= listSize; i++) {
			baseVars[i] = 0;
			artifVars[i] = 0;
			minusVars[i] = 0;
			
			if ( dirs[i].equals(Common.C_LOE) ) {
				varsAmount++;
				baseVars[i] = varsAmount;
			}
		}
		
		for (int i = 1; i <= listSize; i++)
			if ( dirs[i].equals(Common.C_GOE) ) {
				varsAmount++;
				minusVars[i] = varsAmount;
			}
		
		int artifVarsAmount = 0;
		for (int i = 1; i <= listSize; i++)
			if ( !dirs[i].equals(Common.C_LOE) ) {
				varsAmount++;
				artifVarsAmount++;
				baseVars[i] = varsAmount;
				artifVars[i] = varsAmount;
			}
		
		for (int i = 1; i <= listSize; i++)
			if (baseVars[i] == 0) {
				res = false;
				break;
			}
		
		if (res) { 
			n = varsAmount;
			m = listSize;
			minArtif = (artifVarsAmount == 0) ? 0 : varsAmount - artifVarsAmount + 1;
			
			createSimplexTable(artifVars, minusVars);
			
			out.printH1( "Solve the problem" );
			out.printP( "f" + fn.getVarsVector() + " = " + fn.toString() );
			out.printP("");
			
			for (int i = 1; i <= listSize; i++) 
				out.printBR( cons[i-1].toString() );
			
			out.emptyLine();
			out.printH2("Source table");
			out.printSimplexTable(x, baseVars, m, n, minArtif, 0);
		}
		
		return res;
	}
	
	// Create initial simplex table
	private void createSimplexTable(int[] artifVars, int[] minusVars) {
		if (minArtif == 0)
			x = new double[m+2][n+1];
		else
			x = new double[m+3][n+1];
		
		int fsign = fn.getDir().equals(Common.F_MAX) ? -1 : 1;
		int fVars = fn.getVarsAmount();
		double[] coefs = fn.getCoefs();
		
		// Fill lines 0 and m+1
		x[0][0] = 0;
		x[m+1][0] = 0;
		
		for (int j = 1; j <= fVars; j++) {
			x[0][j] = fsign * coefs[j-1];
			x[m+1][j] = -fsign * coefs[j-1];
		}
		
		for (int j = fVars + 1; j <= n; j++){
			x[0][j] = 0;
			x[m+1][j] = 0;
		}
		//
			
		// Fill lines 1..m
		for (int i = 1; i <= m; i++) {
			coefs = cons[i-1].getCoefs();
			x[i][0] = cons[i-1].getC();
			
			for (int j = 1; j <= fVars; j++)
				x[i][j] = coefs[j-1];
			
			for (int j = fVars + 1; j <= n; j++) {
				x[i][j] = 0;
				if (baseVars[i] == j)
					x[i][j] = 1;
				else if (minusVars[i] == j)
					x[i][j] = -1;
				else if (artifVars[i] == j)
					x[i][j] = 1;
			}
		}
		
		// Fill line m+2
		if (minArtif != 0) {
			double[] s = new double[n + 1];
			for (int j = 0; j <= n; j++)
				s[j] = 0;
			
			for (int i = 1; i <= m; i++) {
				for (int j = 0; j <= n; j++) {
					if (j < minArtif)
						if (baseVars[i] >= minArtif)
							s[j] += x[i][j];
				}
			}
			
			for (int j = 0; j <= n; j++)
				x[m+2][j] = s[j];
		}
	}
	
	private void normalBasisMethod() {
		out.emptyLine();
		out.printH2("Normal basis method");
		
		if ( findOptimalSolution(m+1) ) {
			printOptimalSolution();
			findAlternative();
		}
			
	}
	
	public void solve() {
		if (minArtif != 0) {
			out.emptyLine();
			out.printH2("Artifical basis method");
			
			if ( findOptimalSolution(m+2) ) {
				recreateSimplexTable();
				normalBasisMethod();
			}
			
		} else
			normalBasisMethod();
			
		Common.showFrame( new InfoWindow(out.getOut(), out.getMode()) );
	}
	
	// Jordan transformation of simplex table based on element x(k,l)
	private void recalcSimplexTable(int l, int k, int maxLine) {
		out.printSimplexTable(x, baseVars, m, n, minArtif, k);
		out.printP( String.format("Recalc on value %s (row '%s%d', column 'P%d')", Common.clip( x[l][k] ), letter, baseVars[l]-di, k-di) );
		out.emptyLine();
		
		baseVars[l] = k;
		
		double r = x[l][k];
		
		for (int j = 0; j <= n; j++)
			x[l][j] = x[l][j] / r;
		
		for (int i = 1; i <= maxLine; i++)
			if (i != l) {
				double p = x[i][k];
				for (int j = 0; j <= n; j++)
					x[i][j] -= x[l][j] * p;
			}
	}
	
	// Find min x[i][0] / x[i][k] for positive x[i][k]
	private int findMinTeta(int k) {
		double minTeta = Double.MAX_VALUE;
		int l = 0;
		
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
	
	// Find an optimal solution
	private boolean findOptimalSolution(int maxLine) {
		boolean res = false;
		String s1 = "", s2 = "", s3 = "";
		
		for (int attempt = 1; attempt <= n; attempt++) {
			double maxF = 0;
			int k = 0;
			
			for (int j = 1; j <= n; j++) {
				double elem = Common.Round(x[maxLine][j], 6);
				if (elem > maxF) {
					maxF = x[maxLine][j];
					k = j;
				}
			}
			
			// all OK, optimal solution found 
			if (k == 0)	{
				res = true;
				break;
			}
			
			int l = findMinTeta(k);
			
			// The problem has an infinite number of solutions
			if (l == 0) {
				s1 = String.format("The last element of the column 'P%d' is positive,", k-di);
				s2 = String.format("but the column 'P%d' does not contain positive elements!", k-di);
				s3 = "The problem has an infinite number of solutions!";
				break;
			}
				
			out.printP( String.format("In the last row positive value %s is found (column 'P%d')", Common.clip(maxF), k-di) );
			
			recalcSimplexTable(l, k, maxLine);
		}
		
		if (res && (minArtif != 0))	// For artifical basis method only
			for (int i = 1; i <= m; i++)
				if (baseVars[i] >= minArtif) {
					res = false;
					s1 = "The optimality condition is satisfied,";
					s2 = String.format("but in the basic there are artificial elements!");
					s3 = "The constraint system is inconsistent!";
					break;
				}
		
		out.printP("");
		out.printSimplexTable(x, baseVars, m, n, minArtif, 0);
		
		if (!res)
			out.printWarning(s1, s2, s3);
		
		return res;
	}
	
	// Create new simplex table (for artifical basis method only)
	private void recreateSimplexTable() {
		n = minArtif - 1;
		minArtif = 0;
		double[][] y = new double[m+2][n+1];
		
		for (int i = 0; i <= m + 1; i++)
			for (int j = 0; j <= n; j++)
				y[i][j] = x[i][j];
		
		x = y;
	}
	
	// Print optimal solution
	private void printOptimalSolution() {
		double[] res = new double[n + 1];
		for (int j = 1; j <= n; j++)
			res[j] = 0;
			
		for (int i = 1; i <= m; i++)
			res[ baseVars[i] ] = x[i][0];		// basis var value
		
		out.emptyLine();
		out.printH2("Optimal solution");
		
		String s = "";
		for (int j = 1; j <= n; j++)
			s += String.format("%s[%d] = %s; ", letter, j-di, Common.clip( res[j] ) );
		out.printP(s);
		
		double f = ((fn.getDir() == Common.F_MAX) ? -1 : 1) * x[m+1][0];  
		out.printP( String.format("F = %s", Common.clip(f) ) );
		
		out.emptyLine();
		out.printH2( "Linear-fractional programming problem solution" );
		s = "";
		for (int j = 2; j <= n; j++)
			s += String.format("x[%d] = %s; ", j-di, Common.clip( res[j]/res[1] ) );
		out.printP(s);
		out.printP( String.format("F = %s", Common.clip(f) ) );
	}
	
	// Check for alternative
	private void findAlternative() {
		double[] res = new double[n + 1];
		for (int j = 1; j <= n; j++)
			res[j] = 0;
			
		for (int i = 1; i <= m; i++)
			res[ baseVars[i] ] = baseVars[i];	// basis var index
		
		int alterPos = 0;
		
		for (int j = 1; j <= n; j++)
			if (x[m+1][j] == 0)			// In last line: zero for...
				if (res[j] == 0)	{	// ...not basis variable 
					alterPos = j;
					break;
				}
		
		if (alterPos == 0) return;
		
		int l = findMinTeta(alterPos);
		
		if (l != 0) {
			out.emptyLine();
			out.emptyLine();
			out.printH2("Look for the alternative solution");
			out.printP( String.format("In the last row zero value is found for not basis variable (column 'P%d')", alterPos-di) );

			recalcSimplexTable(l, alterPos, m+1);
			out.printSimplexTable(x, baseVars, m, n, minArtif, 0);
			printOptimalSolution();
		}
	}

}