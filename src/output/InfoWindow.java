package output;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import main.Common;

import javax.swing.JTextPane;
import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class InfoWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	
	public InfoWindow(String info, String outMode) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Result:");
		
		setBounds(0, 0, 950, 650);
		
		contentPane = new JPanel();
		contentPane.setBackground(new Color(238, 238, 238));
		contentPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTextPane textPane = new JTextPane();
		textPane.setContentType( outMode == Common.TXT ? "text/plain" : "text/html" );
		textPane.setEditable(false);
		textPane.setFont(new Font("Courier New", Font.PLAIN, 12));
		textPane.setText(info);
		
		JScrollPane scrollPane = new JScrollPane(textPane);
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		JPanel panelBtn = new JPanel();
		contentPane.add(panelBtn, BorderLayout.SOUTH);
		
		JButton btnSaveResult = new JButton("Save to file...");
		btnSaveResult.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FileNameExtensionFilter filter = new FileNameExtensionFilter("*." + outMode,"*.*");
		        JFileChooser fc = new JFileChooser();
		        fc.setFileFilter(filter);
		        if ( fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION ) {
		            try ( FileWriter fw = new FileWriter(fc.getSelectedFile() + "." + outMode) ) {
		                fw.write(info);
		            }
		            catch ( IOException e ) {
		                Common.showErrorMessage(null, "Error while saving file!");
		            }
		        }
			}
		});
		btnSaveResult.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panelBtn.add(btnSaveResult);
	}
}