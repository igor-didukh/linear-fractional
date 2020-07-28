package input;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;

import data.FunctionLFP;
import main.Common;
import main.MainWindow;

public class FunctionDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	private static final String SAVE = "SAVE";
	private static final String[] DIRS = new String[] {Common.F_MIN, Common.F_MAX};
	private static final int WIDTH = 50, HEIGHT = 26, SPACE = 5;
	
	private int amount;
	private JPanel contentPane;
	private DoubleTextField[] numCoefs, denCoefs;
	private JLabel lblName;
	private JComboBox<String> comboDirection;
	
	// For EDIT function
	public FunctionDialog(FunctionLFP f) {
		setTitle("Edit function");
		amount = f.getVarsAmount();
		initDialog();
		
		// set text field's values
		double[] num = f.getNumerator();
		double[] den = f.getDenominator();
		
		for (int i = 0; i <= amount; i++) {
			numCoefs[i].setText( Common.clip( num[i] ) );
			denCoefs[i].setText( Common.clip( den[i] ) );
		}
			
		// set combo direction value
		int pos = 0;
		String dir = f.getDir();
		for (int i = 0; i < DIRS.length; i++)
			if ( dir.equals(DIRS[i]) )
				pos = i;
		
		comboDirection.setSelectedIndex(pos);
		
		makeName();
	}
	
	// Make dialog window
	private void initDialog() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		setModal(true);
		
		MainWindow.fnFromDialog = null;
		
		setBounds(0, 0, (2 * amount + 3) * (WIDTH + SPACE) + 10, 205);
		
		contentPane = new JPanel();
		contentPane.setBackground(new Color(238, 238, 238));
		contentPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		int posX = 20;
		
		JPanel panelLine = new JPanel();
		panelLine.setBorder(new LineBorder(Color.DARK_GRAY));
		panelLine.setBounds(posX-5, HEIGHT + 9, (2 * amount + 1) * (WIDTH + SPACE) + 5, 1);
		contentPane.add(panelLine);
		
		numCoefs = new DoubleTextField[amount+1];
		denCoefs = new DoubleTextField[amount+1];
		
		for (int i = 0; i <= amount; i++) {
			numCoefs[i] = createTextField("double");
			numCoefs[i].setBounds(posX, 5, WIDTH, HEIGHT);
			contentPane.add(numCoefs[i]);
			
			denCoefs[i] = createTextField("not_neg_double");
			denCoefs[i].setBounds(posX, 15 + HEIGHT, WIDTH, HEIGHT);
			contentPane.add(denCoefs[i]);
			
			posX += WIDTH + SPACE;
			
			String lbl = i == amount ? "" : "* x" + (i+1) + " +";
			
			JLabel lblNum = createLabel(lbl);
			lblNum.setBounds(posX, 5, WIDTH, HEIGHT);
			contentPane.add(lblNum);
			
			JLabel lblDen = createLabel(lbl);
			lblDen.setBounds(posX, 15 + HEIGHT, WIDTH, HEIGHT);
			contentPane.add(lblDen);
			
			posX += WIDTH + SPACE;
		}
		
		JLabel lblDir = createLabel("---->");
		lblDir.setBounds(posX - WIDTH - 12, HEIGHT-5, WIDTH, HEIGHT);
		contentPane.add(lblDir);
		
		comboDirection = new JComboBox<String>();
		comboDirection.setModel(new DefaultComboBoxModel<String>(DIRS));
		comboDirection.setBounds(posX - 20, HEIGHT-5, WIDTH, HEIGHT);
		comboDirection.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				makeName();
			}
		});
		comboDirection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				makeName();
			}
		});
		contentPane.add(comboDirection);
		
		JPanel panelName = new JPanel();
		panelName.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		panelName.setBounds(5, HEIGHT + 50, (2 * amount + 3) * (WIDTH + SPACE) - 5, 55);
		contentPane.add(panelName);
		
		lblName = new JLabel();
		lblName.setForeground(new Color(0, 0, 128));
		lblName.setFont(new Font("Courier New", Font.PLAIN, 12));
		lblName.setHorizontalAlignment(SwingConstants.CENTER);
		panelName.add(lblName);
		
		JPanel panelButtons = new JPanel();
		panelButtons.setBounds(5, HEIGHT + 110, (2 * amount + 3) * (WIDTH + SPACE) - 5, 35);
		contentPane.add(panelButtons);
		
		JButton btnOK = new JButton("OK");
		btnOK.addActionListener(this);
		btnOK.setActionCommand(SAVE);
		panelButtons.add(btnOK);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(this);
		panelButtons.add(btnCancel);
	}
	
	// Create one text field with listeners 
	private DoubleTextField createTextField(String restr) {
		DoubleTextField res = new DoubleTextField(restr);
		
		res.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				res.selectAll();
			}
		});
		
		res.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				res.setText( Common.clip( res.getDouble() ) );
				makeName();
			}
		});
		
		res.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				res.setText( Common.clip( res.getDouble() ) );
				makeName();
			}
		});
		
		return res;
	}
	
	private JLabel createLabel(String lbl) {
		JLabel res = new JLabel(lbl);
		res.setVerticalAlignment(SwingConstants.CENTER);
		res.setHorizontalAlignment(SwingConstants.CENTER);
		res.setFont(new Font("Tahoma", Font.PLAIN, 12));
		return res;
	}
	
	// Create the name of function 
	private void makeName() {
		double[] num = new double[amount+1];
		double[] den = new double[amount+1];
		
		for (int i = 0; i <= amount; i++) {
			num[i] = numCoefs[i].getDouble();
			den[i] = denCoefs[i].getDouble();
		}
			
		String dir = (String) comboDirection.getSelectedItem();
		String name = Common.makeNameLFP("", num, den, " --> " + dir);
		
		lblName.setText(name);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand() == SAVE) {
			int zeroCoefsNum = 0;
			int zeroCoefsDen = 0;
			
			double[] num = new double[amount+1];
			double[] den = new double[amount+1];
			for (int i = 0; i <= amount; i++) {
				num[i] = numCoefs[i].getDouble();
				if (num[i] == 0)
					zeroCoefsNum++;
				
				den[i] = denCoefs[i].getDouble();
				if (den[i] == 0)
					zeroCoefsDen++;
			}
			
			if (zeroCoefsNum == amount + 1)
				Common.showErrorMessage(this, "All coefs in numerator should not be zero!");
			else
				if (zeroCoefsDen == amount + 1)
					Common.showErrorMessage(this, "All coefs in denominator should not be zero!");
				else {
					String dir = (String) comboDirection.getSelectedItem();
					MainWindow.fnFromDialog = new FunctionLFP(num, den, dir);
					dispose();
				}
		} 
		else
			dispose();
	}

}