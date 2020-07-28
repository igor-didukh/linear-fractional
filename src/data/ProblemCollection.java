package data;

import java.util.ArrayList;

import main.Common;

public class ProblemCollection {
	private static ArrayList<Problem> problems = new ArrayList<Problem>(); 
	
	static {
		problems.add(
				new Problem(
					"Main problem",
					new FunctionLFP( new double[] {-2, 6, 0}, new double[] {1, 1, 1}, Common.F_MAX ), 
					new Constraint[] {
							new Constraint( new double[] {1.5, 1.5}, Common.C_LOE, 12 ), 
							new Constraint( new double[] {1, 1}, Common.C_GOE, 4 ),
							new Constraint( new double[] {3, -3}, Common.C_LOE, 6 ),
							new Constraint( new double[] {-1, 1}, Common.C_LOE, 2 )
					}
				)
			);
		
		problems.add(
				new Problem(
					"Problem #1 (LP_Drob1.pdf)",
					new FunctionLFP( new double[] {2, 1, 0}, new double[] {1.5, 1, 4}, Common.F_MAX ), 
					new Constraint[] {
							new Constraint( new double[] {4, 1}, Common.C_LOE, 10 ), 
							new Constraint( new double[] {1, 4}, Common.C_LOE, 10 )
					}
				)
			);
		
		problems.add(
				new Problem(
					"Problem #2 (mathprog6.doc)",
					new FunctionLFP( new double[] {2, 3, 0}, new double[] {2, 1, 4}, Common.F_MAX ), 
					new Constraint[] {
							new Constraint( new double[] {4, 1}, Common.C_LOE, 10 ), 
							new Constraint( new double[] {1, 4}, Common.C_LOE, 10 )
					}
				)
			);
		
	}
	
	public static Problem getProblem(int i) {
		return problems.get(i);
	}
	
	public static String[] getTitles() {
		String[] res = new String[ problems.size() ];
		for (int i = 0; i < problems.size(); i++)
			res[i] = problems.get(i).getTitle();
		
		return res;
	}
	
	public static int getInitProblemNo() {
		return 0;
	}

}