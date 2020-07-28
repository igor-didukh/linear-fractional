package main;

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Font;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JList;
import javax.swing.border.EtchedBorder;

import data.Constraint;
import data.Function;
import data.FunctionLFP;
import data.ProblemCollection;
import data.Problem;
import input.FunctionDialog;
import input.ConstraintDialog;

import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JRadioButton;
import javax.swing.border.LineBorder;

public class MainWindow extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private static final String EDIT_FUNCTION = "EDIT_FUNCTION", TRANSFORM = "TRANSFORM", SET_AMOUNT = "SET_AMOUNT", EXIT = "EXIT", GET_PROBLEM= "GET_PROBLEM", 
			ADD_CONSTRAINT = "ADD_CONSTRAINT", EDIT_CONSTRAINT = "EDIT_CONSTRAINT", DELETE_CONSTRAINT = "DELETE_CONSTRAINT", SOLVE = "SOLVE";
	private static final int YES = JOptionPane.YES_OPTION;
	
	private final DefaultListModel<Constraint> listModelMain = new DefaultListModel<Constraint>();
	private final DefaultListModel<Constraint> listModelLinear = new DefaultListModel<Constraint>();
	public static FunctionLFP fnFromDialog;
	public static Constraint consFromDialog;
	
	private FunctionLFP functionMain;
	private Function functionLinear;
	private JPanel contentPane, panelData, panelTitle;
	private JTextField txtAmount;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JLabel lblTransform, lblFMain, lblVMain, lblFLinear, lblVLinear;
	private JButton btnTransformToLinear, btnAddConstraint, btnEditConstraint, btnDeleteConstraint, btnSolve; 
	private JList<Constraint> listMainConstraints;
	private JComboBox<String> comboProblem;
	private ButtonGroup grpFormat;
	
	// Main window
	public MainWindow() {
		setResizable(false);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent evt) {
				closeFrame(evt);
			}
		});
		
		setTitle("Solving of the linear-fractional programming problem (the Charnes-Cooper transformation)");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		setBounds(100, 100, 1006, 672);
		
		contentPane = new JPanel();
		contentPane.setBackground( new Color(238, 238, 238) );
		contentPane.setBorder( new BevelBorder(BevelBorder.LOWERED, null, null, null, null) );
		contentPane.setLayout( new BorderLayout(0, 0) );
		setContentPane(contentPane);
		
		menuBar = new JMenuBar();
		contentPane.add(menuBar, BorderLayout.NORTH);
		
		mnFile = new JMenu("File");
		
		JMenuItem mntmAmount = new JMenuItem("Variables");
		mntmAmount.setActionCommand(SET_AMOUNT);
		mntmAmount.addActionListener(this);
		mnFile.add(mntmAmount);
		
		JMenuItem mntmTransform = new JMenuItem("Transform");
		mntmTransform.setActionCommand(TRANSFORM);
		mntmTransform.addActionListener(this);
		mnFile.add(mntmTransform);
		
		JMenuItem mntmSolve = new JMenuItem("Solve");
		mntmSolve.setActionCommand(SOLVE);
		mntmSolve.addActionListener(this);
		mnFile.add(mntmSolve);
		
		mnFile.addSeparator();
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.setActionCommand(EXIT);
		mntmExit.addActionListener(this);
		mnFile.add(mntmExit);
		
		menuBar.add(mnFile);
		
		panelData = new JPanel();
		contentPane.add(panelData);
		panelData.setLayout(null);
		
		panelTitle = new JPanel();
		panelTitle.setBounds(5, 5, 987, 58);
		panelTitle.setLayout( new BorderLayout(0, 0) );
		panelData.add(panelTitle);
		
		JLabel lblTitle = new JLabel("Solving of the linear-fractional programming problem");
		lblTitle.setVerticalAlignment(SwingConstants.CENTER);
		lblTitle.setForeground(new Color(0, 0, 128));
		lblTitle.setFont(new Font("Arial", Font.PLAIN, 28));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		panelTitle.add(lblTitle, BorderLayout.CENTER);
		
		JLabel lblMethod = new JLabel("(the Charnes-Cooper transformation)");
		lblMethod.setForeground(new Color(0, 0, 128));
		lblMethod.setFont( new Font("Tahoma", Font.PLAIN, 20));
		lblMethod.setHorizontalAlignment(SwingConstants.CENTER);
		panelTitle.add(lblMethod, BorderLayout.SOUTH);
		
		JPanel panelProblem = new JPanel();
		panelProblem.setBounds(5, 70, 987, 32);
		panelData.add(panelProblem);
		
		JLabel lblProblem = new JLabel("Initial problem:");
		panelProblem.add(lblProblem);
		lblProblem.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		comboProblem = new JComboBox<String>();
		panelProblem.add(comboProblem);
		comboProblem.setForeground(new Color(128, 0, 0));
		comboProblem.setFont(new Font("Tahoma", Font.PLAIN, 12));
		comboProblem.setModel(new DefaultComboBoxModel<String>( ProblemCollection.getTitles() ));
		comboProblem.setSelectedIndex( ProblemCollection.getInitProblemNo() );
		
		JLabel lblAmount = new JLabel("variables:");
		panelProblem.add(lblAmount);
		lblAmount.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		txtAmount = new JTextField();
		panelProblem.add(txtAmount);
		txtAmount.setForeground(new Color(128, 0, 0));
		txtAmount.setEditable(false);
		txtAmount.setHorizontalAlignment(SwingConstants.CENTER);
		txtAmount.setFont(new Font("Tahoma", Font.BOLD, 14));
		txtAmount.setColumns(2);
		
		JButton btnSetAmount = new JButton("Set...");
		panelProblem.add(btnSetAmount);
		btnSetAmount.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnSetAmount.setActionCommand(SET_AMOUNT);
		btnSetAmount.addActionListener(this);
		
		btnTransformToLinear = new JButton("to linear problem");
		btnTransformToLinear.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnTransformToLinear.setBounds(191, 106, 126, 21);
		btnTransformToLinear.setActionCommand(TRANSFORM);
		btnTransformToLinear.addActionListener(this);
		panelData.add(btnTransformToLinear);
		
		JLabel lblFMainTitle = new JLabel("Function:");
		lblFMainTitle.setBounds(5, 165, 57, 14);
		panelData.add(lblFMainTitle);
		lblFMainTitle.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblFMainTitle.setHorizontalAlignment(SwingConstants.LEFT);
		
		JButton btnEditFunction = new JButton("Edit...");
		btnEditFunction.setBounds(68, 161, 71, 23);
		panelData.add(btnEditFunction);
		btnEditFunction.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnEditFunction.setActionCommand(EDIT_FUNCTION);
		btnEditFunction.addActionListener(this);
		
		JLabel lblFLinearTitle = new JLabel("Function:");
		lblFLinearTitle.setHorizontalAlignment(SwingConstants.RIGHT);
		lblFLinearTitle.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblFLinearTitle.setBounds(927, 165, 57, 14);
		panelData.add(lblFLinearTitle);
		
		JPanel panelMain = new JPanel();
		panelMain.setBounds(5, 187, 492, 376);
		panelData.add(panelMain);
		panelMain.setLayout(new BorderLayout(0, 0));
		
		JPanel panelFMain = new JPanel();
		panelMain.add(panelFMain, BorderLayout.NORTH);
		panelFMain.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		
		lblFMain = new JLabel();
		panelFMain.add(lblFMain);
		lblFMain.setForeground(new Color(128, 0, 0));
		lblFMain.setHorizontalAlignment(SwingConstants.CENTER);
		lblFMain.setFont(new Font("Courier New", Font.BOLD, 12));
		
		JPanel panelSMain = new JPanel();
		panelMain.add(panelSMain, BorderLayout.CENTER);
		panelSMain.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panelSMain.setLayout(new BorderLayout(0, 0));
		
		JLabel lblSMain = new JLabel("Constraint system:");
		lblSMain.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblSMain.setHorizontalAlignment(SwingConstants.CENTER);
		panelSMain.add(lblSMain, BorderLayout.NORTH);
		
		listMainConstraints = new JList<>(listModelMain);
		listMainConstraints.setFont(new Font("Courier New", Font.BOLD, 12));
		listMainConstraints.setForeground(new Color(128, 0, 0));
		listMainConstraints.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panelSMain.add(listMainConstraints, BorderLayout.CENTER);
		
		JPanel panelActions = new JPanel();
		panelSMain.add(panelActions, BorderLayout.EAST);
		panelActions.setLayout(new GridLayout(6, 1, 0, 0));
		
		btnAddConstraint = new JButton("+");
		btnAddConstraint.setActionCommand(ADD_CONSTRAINT);
		btnAddConstraint.addActionListener(this);
		panelActions.add(btnAddConstraint);
		
		btnEditConstraint = new JButton("*");
		btnEditConstraint.setActionCommand(EDIT_CONSTRAINT);
		btnEditConstraint.addActionListener(this);
		panelActions.add(btnEditConstraint);
		
		btnDeleteConstraint = new JButton("X");
		btnDeleteConstraint.setActionCommand(DELETE_CONSTRAINT);
		btnDeleteConstraint.addActionListener(this);
		panelActions.add(btnDeleteConstraint);
		
		JPanel panelVMain = new JPanel();
		panelVMain.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panelMain.add(panelVMain, BorderLayout.SOUTH);
		
		lblVMain = new JLabel();
		lblVMain.setHorizontalAlignment(SwingConstants.CENTER);
		lblVMain.setForeground(new Color(128, 0, 0));
		lblVMain.setFont(new Font("Courier New", Font.BOLD, 12));
		panelVMain.add(lblVMain);
		
		JPanel panelLinear = new JPanel();
		panelLinear.setBounds(500, 187, 492, 376);
		panelData.add(panelLinear);
		panelLinear.setLayout(new BorderLayout(0, 0));
		
		JPanel panelFLinear = new JPanel();
		panelLinear.add(panelFLinear, BorderLayout.NORTH);
		panelFLinear.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		
		lblFLinear = new JLabel();
		lblFLinear.setHorizontalAlignment(SwingConstants.CENTER);
		lblFLinear.setForeground(new Color(128, 0, 0));
		lblFLinear.setFont(new Font("Courier New", Font.BOLD, 12));
		panelFLinear.add(lblFLinear);
		
		JPanel panelSLinear = new JPanel();
		panelLinear.add(panelSLinear, BorderLayout.CENTER);
		panelSLinear.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panelSLinear.setLayout(new BorderLayout(0, 0));
		
		JLabel lblSLinear = new JLabel("Constraint system:");
		lblSLinear.setHorizontalAlignment(SwingConstants.CENTER);
		lblSLinear.setFont(new Font("Tahoma", Font.BOLD, 12));
		panelSLinear.add(lblSLinear, BorderLayout.NORTH);
		
		JList<Constraint> listLinearConstraints = new JList<Constraint>(listModelLinear);
		listLinearConstraints.setFont(new Font("Courier New", Font.BOLD, 12));
		listLinearConstraints.setSelectedIndex(0);
		listLinearConstraints.setForeground(new Color(128, 0, 0));
		listLinearConstraints.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panelSLinear.add(listLinearConstraints, BorderLayout.CENTER);
		
		JPanel panelVLinear = new JPanel();
		panelVLinear.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panelLinear.add(panelVLinear, BorderLayout.SOUTH);
		
		lblVLinear = new JLabel("?");
		lblVLinear.setHorizontalAlignment(SwingConstants.CENTER);
		lblVLinear.setForeground(new Color(128, 0, 0));
		lblVLinear.setFont(new Font("Courier New", Font.BOLD, 12));
		panelVLinear.add(lblVLinear);
		
		btnSolve = new JButton("Solve");
		btnSolve.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnSolve.setBounds(372, 573, 162, 39);
		btnSolve.setActionCommand(SOLVE);
		btnSolve.addActionListener(this);
		panelData.add(btnSolve);
		
		grpFormat = new ButtonGroup();
		
		JRadioButton rbTxt = new JRadioButton("Plain text");
		rbTxt.setFont(new Font("Tahoma", Font.PLAIN, 11));
		rbTxt.setBounds(552, 573, 79, 19);
		rbTxt.setActionCommand(Common.TXT);
		rbTxt.setSelected(false);
		panelData.add(rbTxt);
		grpFormat.add(rbTxt);
		
		JRadioButton rbHtml = new JRadioButton("HTML");
		rbHtml.setFont(new Font("Tahoma", Font.PLAIN, 11));
		rbHtml.setBounds(552, 593, 57, 19);
		rbHtml.setActionCommand(Common.HTML);
		rbHtml.setSelected(true);
		grpFormat.add(rbHtml);
		panelData.add(rbHtml);
		
		JLabel lblMainTitle = new JLabel("Linear-fractional problem");
		lblMainTitle.setForeground(new Color(75, 0, 130));
		lblMainTitle.setHorizontalAlignment(SwingConstants.LEFT);
		lblMainTitle.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblMainTitle.setBounds(5, 139, 182, 19);
		panelData.add(lblMainTitle);
		
		JLabel lblLinearTitle = new JLabel("Linear problem");
		lblLinearTitle.setHorizontalAlignment(SwingConstants.RIGHT);
		lblLinearTitle.setForeground(new Color(75, 0, 130));
		lblLinearTitle.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblLinearTitle.setBounds(872, 139, 112, 19);
		panelData.add(lblLinearTitle);
		
		lblTransform = new JLabel();
		lblTransform.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		lblTransform.setFont(new Font("Courier New", Font.BOLD, 12));
		lblTransform.setHorizontalAlignment(SwingConstants.CENTER);
		lblTransform.setBounds(189, 104, 679, 80);
		panelData.add(lblTransform);
		
		comboProblem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				getProblem();
			}
		});
		
		getProblem();
	}
	
	// Set problem data values to form fields
	private void setFields() {
		txtAmount.setText( "" + functionMain.getVarsAmount() );
		lblFMain.setText( "" + functionMain );
		lblVMain.setText( functionMain.getVarsRestrictions() );
		listMainConstraints.setSelectedIndex(0);
	}
	
	// Set buttons availability
	private void checkButtons() {
		functionLinear = null;
		lblTransform.setText("");
		lblFLinear.setText("<html><br>&lt;?&gt;<br>&nbsp;");
		lblVLinear.setText("<?>");
		listModelLinear.clear();
		
		boolean constrListNotEmpty = (listModelMain.getSize() != 0);
		btnTransformToLinear.setEnabled(constrListNotEmpty);
		btnEditConstraint.setEnabled(constrListNotEmpty);
		btnDeleteConstraint.setEnabled(constrListNotEmpty);
		
		btnSolve.setEnabled(false);
	}
	
	// Get problem data from combo
	private void getProblem() {
		listModelMain.clear();
		
		Problem problem = ProblemCollection.getProblem( comboProblem.getSelectedIndex() );
		functionMain = problem.getF();
		
		Constraint[] constraints = problem.getConstraints();
		for (Constraint constraint : constraints)
			listModelMain.addElement(constraint);
		
		setFields();
		checkButtons();
	}
	
	// Show dialog to change vars amount
	private void changeVarsAmount() {
		int amount = functionMain.getVarsAmount();
		int n = Common.showNumberDialog(this, "Select number of variables", "Number of variables", amount);
		
		if ( (n <= 0) || (n == amount) ) return;
		
		if ( Common.showConfirmDialog(this, "Change number of variables from " + amount + " to " + n + "?", "Change number of variables") == YES ) {
			double[] num = new double[n+1];
			double[] den = new double[n+1];
			for (int i = 0; i <= n; i++) {
				num[i] = 1;
				den[i] = 1;
			}
			
			functionMain = new FunctionLFP(num, den, Common.F_MAX);
			
			listModelMain.clear();
			setFields();
			checkButtons();
		}
	}
	
	// Transform main problem to linear problem
	private void transformToLinearProblem() {
		listModelLinear.clear();
		lblTransform.setText( functionMain.transformStr() );
		
		double[] num = functionMain.getNumerator();
		double[] coefs = new double[num.length];
		
		coefs[0] = num[num.length-1];
		for (int i = 0; i < num.length-1; i++)
			coefs[i+1] = num[i];
		
		functionLinear = new Function( coefs, "y", 0, functionMain.getDir() );
		lblFLinear.setText("<html><br>" + functionLinear + "<br>&nbsp;");
		lblVLinear.setText( functionLinear.getVarsRestrictions() );
		
		for (int i = 0; i < listModelMain.size(); i++) {
			Constraint cons = listModelMain.getElementAt(i);
			
			coefs[0] = -cons.getC();
			double[] c = cons.getCoefs();
			for (int j = 0; j < c.length; j++)
				coefs[j+1] = c[j];
			
			listModelLinear.addElement( new Constraint(coefs, "y", 0, cons.getDir(), 0) );
		}
		
		double[] den = functionMain.getDenominator();
		coefs[0] = den[den.length-1];
		for (int i = 0; i < den.length-1; i++)
			coefs[i+1] = den[i];
		
		listModelLinear.addElement( new Constraint(coefs, "y", 0, Common.C_EQU, 1) );
		
		
		btnSolve.setEnabled(listModelLinear.getSize() >= 2);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		
		switch (action) {
		case GET_PROBLEM:
			getProblem();
			break;
		
		case SET_AMOUNT:
			changeVarsAmount();
			break;
		
		case EDIT_FUNCTION:
			Common.showFrame( new FunctionDialog(functionMain) );
			
			if (fnFromDialog != null) {
				functionMain = fnFromDialog;
				lblFMain.setText( functionMain.toString() );
				checkButtons();
			}
			
			break;
		
		case ADD_CONSTRAINT:
			Common.showFrame( new ConstraintDialog( functionMain.getVarsAmount() ) );
			
			if (consFromDialog != null) {
				listModelMain.addElement(consFromDialog);
				listMainConstraints.setSelectedIndex( listModelMain.getSize()-1 );
				checkButtons();
			}
        	
			break;
			
		case EDIT_CONSTRAINT:
			Constraint selectedConstraint = listModelMain.get( listMainConstraints.getSelectedIndex() );
			Common.showFrame( new ConstraintDialog(selectedConstraint) );
			
			if (consFromDialog != null) {
				listModelMain.set( listMainConstraints.getSelectedIndex(), consFromDialog );
				checkButtons();
			}
        	
			break;
        	
		case DELETE_CONSTRAINT:
			selectedConstraint = listModelMain.get( listMainConstraints.getSelectedIndex() );
			
			if ( Common.showConfirmDialog(this, "You really want to delete constraint: " + selectedConstraint.toString() + "?", "Delete") == YES ) {
				listModelMain.removeElementAt( listMainConstraints.getSelectedIndex() );
				if (listModelMain.getSize() != 0)
					listMainConstraints.setSelectedIndex(0);
				checkButtons();
			}
        	
			break;
        
		case TRANSFORM:
			transformToLinearProblem();
			break;
			
        case SOLVE:
        	Constraint[] cons = new Constraint[ listModelLinear.size() ]; 
    		
    		for (int i = 0; i < listModelLinear.size(); i++)
    			cons[i] = listModelLinear.getElementAt(i);
        	
        	String mode = grpFormat.getSelection().getActionCommand();
        	
        	Solution sol = new Solution(functionLinear, cons, mode);
        	if ( sol.initSolution() )
        		sol.solve();
        	else
        		Common.showErrorMessage(this, "Error while initialization solution!");
        	
        	break;
        	
        case EXIT:
        	closeFrame(e);
        	break;
		}
	}
	
	// Action on close main window
	private void closeFrame(java.awt.AWTEvent evt) {
		//if ( Common.showConfirmDialog(this, "You really want to exit?", "Exit") == YES )
			System.exit(0);
    }
	
}