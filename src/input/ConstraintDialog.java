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

import data.Constraint;
import main.Common;
import main.MainWindow;

public class ConstraintDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	private static final String SAVE = "SAVE";
	private static final String[] DIRS = new String[] {Common.C_LOE, Common.C_GOE, Common.C_EQU};
	private static final int WIDTH = 50, HEIGHT = 26, SPACE = 5; 
	
	private int amount;
	private JPanel contentPane;
	private DoubleTextField[] txtCoefs;
	private DoubleTextField txtC;
	private JLabel lblName;
	private JComboBox<String> comboDirection;
	
	// For ADD constraint
	public ConstraintDialog(int amount) {
		setTitle("Add constraint");
		this.amount = amount;
		initDialog();
		makeName();
	}
	
	// For EDIT constraint
	public ConstraintDialog(Constraint cons) {
		setTitle("Edit constraint");
		amount = cons.getVarsAmount();
		initDialog();
		
		// set text field's values
		double[] coefs = cons.getCoefs();
		for (int i = 0; i < coefs.length; i++) 
			txtCoefs[i].setText( Common.clip( coefs[i] ) );
		
		txtC.setText( Common.clip( cons.getC() ) );
		
		// set combo direction value
		int pos = 0;
		String dir = cons.getDir();
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
		
		MainWindow.consFromDialog = null;
		
		setBounds(0, 0, (2 * amount + 2) * (WIDTH + SPACE) + 10, 140);
		
		contentPane = new JPanel();
		contentPane.setBackground(new Color(238, 238, 238));
		contentPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtCoefs = new DoubleTextField[amount];
		int posX = 5;
		
		for (int i = 0; i < amount; i++) {
			txtCoefs[i] = createTextField("double");
			txtCoefs[i].setBounds(posX, 5, WIDTH, HEIGHT);
			contentPane.add(txtCoefs[i]);
			posX += WIDTH + SPACE;
			
			String lbl = "* x" + (i+1);
			lbl = (i != amount - 1) ? lbl + " +": lbl;
			
			JLabel lblC = new JLabel(lbl);
			lblC.setVerticalAlignment(SwingConstants.CENTER);
			lblC.setHorizontalAlignment(SwingConstants.CENTER);
			lblC.setFont(new Font("Tahoma", Font.PLAIN, 12));
			lblC.setBounds(posX, 5, WIDTH, HEIGHT);
			contentPane.add(lblC);
			posX += WIDTH + SPACE;
		}
		
		comboDirection = new JComboBox<String>();
		comboDirection.setModel(new DefaultComboBoxModel<String>(DIRS));
		comboDirection.setBounds(posX, 5, WIDTH, HEIGHT);
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
		
		posX += WIDTH + SPACE;
		txtC = createTextField("not_neg_double");
		txtC.setBounds(posX, 5, WIDTH, HEIGHT);
		contentPane.add(txtC);
		
		JPanel panelName = new JPanel();
		panelName.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		panelName.setBounds(5, HEIGHT + 10, (2 * amount + 2) * (WIDTH + SPACE) - 5, 35);
		contentPane.add(panelName);
		
		lblName = new JLabel();
		lblName.setForeground(new Color(0, 0, 128));
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblName.setHorizontalAlignment(SwingConstants.CENTER);
		panelName.add(lblName);
		
		JPanel panelButtons = new JPanel();
		panelButtons.setBounds(5, HEIGHT + 47, (2 * amount + 2) * (WIDTH + SPACE) - 5, 35);
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
	
	// Create the name of function or cor constraint 
	private void makeName() {
		double[] c = new double[amount];
		for (int i = 0; i < amount; i++)
			c[i] = txtCoefs[i].getDouble();
		
		String dir = (String) comboDirection.getSelectedItem();
		String name = Common.makePolynomName(c, dir) + " " + Common.clip(txtC.getDouble());
		lblName.setText(name);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand() == SAVE) {
			int zeroCoefsCount = 0;
			double[] c = new double[amount];
			
			for (int i = 0; i < amount; i++) {
				c[i] = txtCoefs[i].getDouble();
				if (c[i] == 0)
					zeroCoefsCount++;
			}
			
			if (zeroCoefsCount == amount)
				Common.showErrorMessage(this, "All coefs should not be zero!");
			else {
				String dir = (String) comboDirection.getSelectedItem();
				MainWindow.consFromDialog = new Constraint(c, dir, txtC.getDouble());
				dispose();
			}
		} 
		else
			dispose();
	}

}