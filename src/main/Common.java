package main;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Window;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

public class Common {
	public static final String F_MIN = "min", F_MAX = "max", C_LOE = "<=", C_GOE = ">=", C_EQU = "=", TXT = "txt", HTML = "html";
	public static int MAX_AMOUNT = 9;
	
	// Show frame on the center of screen 
	public static void showFrame(Window frame) {
		int screenWidth = 0, screenHeight = 0;
		
		GraphicsDevice[] screenDevices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        for (GraphicsDevice graphicsDevice : screenDevices) {
            screenWidth = graphicsDevice.getDefaultConfiguration().getBounds().width;
            screenHeight = graphicsDevice.getDefaultConfiguration().getBounds().height;
        }
		
        Rectangle r = frame.getBounds();
		
		int frameWidth = r.width, frameHeight = r.height;
		int posX = (screenWidth - frameWidth) / 2;
		int posY = (screenHeight - frameHeight) / 2 - 40;
		
		frame.setPreferredSize(new Dimension(frameWidth, frameHeight));
		frame.setBounds(posX, posY, r.width, r.height);
		
		frame.setVisible(true);
	}
	
	// Create frame from panel snd show it
	public static void makeFrame(JPanel panel, String title) {
		Rectangle r = panel.getBounds();
		
		JFrame frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setBounds(r.x, r.y, r.width, r.height);
		frame.add(panel);
		
		showFrame(frame);
	}
	
	// String --> int
	public static int parseInt(String s) {
		int n = 0;
		try {
			n = Integer.parseInt(s);
		} catch (Exception e) {}
		return n;
	}
	
	// String --> double
	public static double parseDouble(String s) {
		double d = 0;
		try {
			d = Double.parseDouble(s);
		} catch (Exception e) {}
		return d;
	}
	
	// Error message
	public static void showErrorMessage(Component cmp, String message) {
		JOptionPane.showMessageDialog(cmp, message, "Error!", JOptionPane.ERROR_MESSAGE);
	}
	
	// Yes-No dialog
	public static int showConfirmDialog(Component cmp, String message, String title) {
		Object[] options = { "Yes", "No" };
        return JOptionPane.showOptionDialog(
        		cmp, message, title,
        		JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
        		null, options, options[1]
        );
	}
	
	// To select number from 2 to MAX_AMOUNT
	public static int showNumberDialog(Component cmp, String message, String title, int initValue) {
		Integer[] numbers = new Integer[MAX_AMOUNT];
		int pos = 0;
		
		for (int i = 2; i <= MAX_AMOUNT; i++) {
			numbers[i-2] = i;
			if (i == initValue)
				pos = i-2;
		}
			
		Integer num = (Integer) JOptionPane.showInputDialog(cmp, 
			        message,
			        title,
			        JOptionPane.QUESTION_MESSAGE, 
			        null, 
			        numbers, 
			        numbers[pos]);
		
        return num != null ? num.intValue() : 0; 
	}
	
	// Round double value
	public static double Round(double value, int digits) {
		return new BigDecimal(value).setScale(digits, RoundingMode.HALF_UP).doubleValue();
	}
	
	// Text filter to set allowed symbols in text field
	private static DocumentFilter getTextFilter(String filterType) {
		String bannedSymbols;
		
		switch (filterType.toUpperCase()) {
		case "DOUBLE":
			bannedSymbols = "[^0123456789.-]";
			break;
		case "NOT_NEG_DOUBLE":
			bannedSymbols = "[^0123456789.]";
			break;
		default:
			bannedSymbols = "[^0123456789]";
			break;
		}
		
		return new DocumentFilter() {
		    @Override
		    public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
		        string = string.replaceAll(bannedSymbols, "");
		        super.insertString(fb, offset, string, attr);
		    }

		    @Override
		    public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
		        text = text.replaceAll(bannedSymbols, "");
		        super.replace(fb, offset, length, text, attrs);
		    }
		};
		
	}
	
	// Set restriction for text field with numeric values
	public static void setRestrictions(JTextField field, String filterType) {
		((AbstractDocument) field.getDocument()).setDocumentFilter(getTextFilter(filterType));
	}
	
	// Readeable view for double value
	public static String clip(double f) {
		String sf = String.format("%8.3f", f);
		String res = sf;
		
		if ( sf.endsWith("000") )
			res = sf.substring(0,4);
		else 
			if ( sf.endsWith("00") )
				res = sf.substring(0,6);
			else
				if ( sf.endsWith("0") )
					res = sf.substring(0,7);
		
		res = res.replace(",", ".");
		res = res.replace(" ", "");
		res = res.equals("-0") ? "0" : res;
		
		return res;
	}
	
	// Create name of polynom set by its coefs
	public static String makePolynomName(double[] c, String dir) {
		return makePolynomName(c, "x", 1, dir);
	}
	
	public static String makePolynomName(double[] c, String letter, int initNo, String dir) {
		String[] letters = new String[c.length];
		
		for (int i = 0; i < c.length; i++)
			letters[i] = letter + (i + initNo);
		
		return createPolynomName(c, letters, dir);
	}
	
	private static String createPolynomName(double[] c, String[] letters, String dir) {
		String res = "", sign;
		boolean first = true;
		
		for (int i = 0; i < c.length; i++) {
			double a = c[i];
			if (a != 0) {
				if (first)
					sign = (a<0) ? "-" : "";
				else
					sign = (a<0) ? " - " : " + ";
					
				a = Math.abs(a);
				
				if ( letters[i].isEmpty() ) {
					res += sign + clip(a);
				} else {
					String part = (a != 1) ? "" + clip(a) + " * ": "";
					res += sign + part + letters[i];
				}
				first = false;
			}
		}
		
		res += " " + dir;
		return res.trim();
	}
	
	// Create part of fraction (numerator or denominator)
	private static String makeFracPart(double[] c) {
		String letter = "x";
		String[] letters = new String[c.length];
		
		for (int i = 0; i < c.length - 1; i++)
			letters[i] = letter + (i+1);
		
		letters[c.length-1] = "";
		
		return createPolynomName(c, letters, "");
	}
	
	// Create name of linear-fractional function set by coefs of numerator and denominator
	public static String makeNameLFP(String before, double[] num, double[] den, String after) {
		String numS = makeFracPart(num); 
		String denS = makeFracPart(den);
		int numL = numS.length();
		int denL = denS.length();
		
		String tab0 = "";
		for (int i = 0; i < before.length(); i++)
			tab0 += "&nbsp;";
		
		String tab = "";
		for (int i = 0; i < Math.abs(numL - denL) / 2; i++)
			tab += "&nbsp;";
		
		String str = "";
		for (int i = 0; i < Math.max(numL, denL); i++)
			str += "-";
		
		String res = "<html>"
				+ tab0 + (numL < denL ? tab : "") + numS + "<br />"
				+ before + str + after + "<br />"
				+ tab0 + (numL > denL ? tab : "") + denS;
		
		return res;
	}
	
}