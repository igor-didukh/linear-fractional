package input;

import java.awt.Font;

import javax.swing.JTextField;
import javax.swing.SwingConstants;

import main.Common;

public class DoubleTextField extends JTextField {
	private static final long serialVersionUID = 1L;
	
	public DoubleTextField() {
		this("double");
	}
	
	public DoubleTextField(String restr) {
		super();
		setFont(new Font("Tahoma", Font.BOLD, 12));
		setHorizontalAlignment(SwingConstants.RIGHT);
		setColumns(10);
		setRestrictions(restr);
		this.setText("1");
	}
	
	public void setRestrictions(String restr) {
		Common.setRestrictions(this, restr);
	}
	
	public double getDouble() {
		return Common.parseDouble(this.getText().trim());
	}

}