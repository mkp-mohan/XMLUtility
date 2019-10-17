package project;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * @author Mohan Palukuri
 *
 */

public class UIClass implements ActionListener {

	JFrame frameMain;
	JLabel lblJuiceInputXML;
	JLabel lblDesiredPolNumbers;
	JLabel lblRSLExtract;
	JLabel lblResult;
	JLabel lblPDGXML;
	JTextField fieldInputJUICE;
	JTextField fieldPDGInputXML;
	JTextArea fieldPolNumbers;
	JTextArea fieldResult;
	JTextField fieldRSLExtract;
	JButton btnExtract;
	JButton btnXMLUpload;
	JButton btnTxtUpload;
	JButton btnPDGUpload;
	JPanel panelInputXML;
	JPanel panelInputPDG;
	JPanel panelInputPolNumbers;
	JPanel panelResult;
	JPanel panelRSL;
	JPanel panel;
	JFileChooser fileXML;
	JFileChooser filePDG;
	JFileChooser fileTxt;
	JCheckBox chkRSL;
	JCheckBox chkPDG;
	JLabel progressBar;
	
	public UIClass() {
		frameMain = new JFrame("NN-Test Data Utility");

		lblJuiceInputXML = new JLabel("JUICE Input XML:");
		fieldInputJUICE = new JTextField(40);
		btnXMLUpload = new JButton("Upload");

		panelInputXML = new JPanel();

		panelInputXML.add(lblJuiceInputXML);
		panelInputXML.add(fieldInputJUICE);
		panelInputXML.add(btnXMLUpload);
		
		chkPDG = new JCheckBox("Check Policies in PDG file before extraction");
		
		lblPDGXML = new JLabel("PDG Input XML:");
		fieldPDGInputXML = new JTextField(40);
		btnPDGUpload = new JButton("Upload");

		panelInputPDG = new JPanel();

		panelInputPDG.add(lblPDGXML);
		panelInputPDG.add(fieldPDGInputXML);
		panelInputPDG.add(btnPDGUpload);
		panelInputPDG.setVisible(false);
		

		lblDesiredPolNumbers = new JLabel("Required policy numbers:");
		fieldPolNumbers = new JTextArea(10, 50);
		panelInputPolNumbers = new JPanel();
		panelInputPolNumbers.add(lblDesiredPolNumbers);
		panelInputPolNumbers.add(fieldPolNumbers);
		panelInputPolNumbers.setToolTipText("Enter policy numbers comma(,) seperated");
		
		
		chkRSL = new JCheckBox("Extract client data from RSL Extract");
		
		lblRSLExtract = new JLabel("ETS RSL Extract:");
		fieldRSLExtract = new JTextField(40);
		btnTxtUpload = new JButton("Upload");

		panelRSL = new JPanel();

		panelRSL.add(lblRSLExtract);
		panelRSL.add(fieldRSLExtract);
		panelRSL.add(btnTxtUpload);
		panelRSL.setVisible(false);
		ImageIcon icon = new ImageIcon(getClass().getResource("progress.gif"));
		btnExtract = new JButton("Filter and Extract data");
		progressBar = new JLabel(icon);
		progressBar.setVisible(false);
		

		panelResult = new JPanel();
		lblResult = new JLabel("Result:");
		fieldResult = new JTextArea(5, 50);
		panelResult.add(lblResult);
		panelResult.add(fieldResult);
		panelResult.setVisible(false);

		
		
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(panelInputXML);
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(chkPDG);
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(panelInputPDG);
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(panelInputPolNumbers);
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(chkRSL);
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(panelRSL);
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(btnExtract);
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(progressBar);
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(panelResult);
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		
	
		frameMain.add(panel);		
		frameMain.setVisible(true);
		frameMain.setSize(900, 750);
		frameMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameMain.setLayout(new FlowLayout());
		btnXMLUpload.addActionListener(this);
		btnPDGUpload.addActionListener(this);
		btnTxtUpload.addActionListener(this);
		chkRSL.addActionListener(this);
		chkPDG.addActionListener(this);
		btnExtract.addActionListener(this);
		
		
	}

	public static void main(String[] args) {

		new UIClass();

	}

	@Override
	public void actionPerformed(ActionEvent event) {
		panelResult.setVisible(false);
		if (event.getSource() == btnXMLUpload) {
			fileXML = new JFileChooser();
			int i = fileXML.showOpenDialog(frameMain);
			if (i == JFileChooser.APPROVE_OPTION) {
				File f = fileXML.getSelectedFile();
				if (f.getName().endsWith(".xml") == false && f.getName().endsWith(".XML") == false) {
					panelInputXML.setBackground(Color.RED);
					btnExtract.setEnabled(false);
					fieldInputJUICE.setText("INVALID FILE FORMAT. EXPECTING XML FILE");
				} else {
					panelInputXML.setBackground(null);
					fieldInputJUICE.setText(f.getPath());
					fieldInputJUICE.setEnabled(false);
					btnExtract.setEnabled(true);
				}

			}
		}else if (event.getSource() == btnTxtUpload) {
			panelResult.setVisible(false);
			fileTxt = new JFileChooser();
			int i = fileTxt.showOpenDialog(frameMain);
			if (i == JFileChooser.APPROVE_OPTION) {
				File f = fileTxt.getSelectedFile();
				if (f.getName().endsWith(".txt") == false && f.getName().endsWith(".TXT") == false) {
					panelRSL.setBackground(Color.RED);
					btnExtract.setEnabled(false);
					fieldRSLExtract.setText("INVALID FILE FORMAT. EXPECTING TEXT FILE");
				} else {
					panelRSL.setBackground(null);
					fieldRSLExtract.setText(f.getPath());
					fieldRSLExtract.setEnabled(false);
					btnExtract.setEnabled(true);
				}

			}
		}else if (event.getSource() == btnPDGUpload) {
			panelResult.setVisible(false);
			filePDG = new JFileChooser();
			int i = filePDG.showOpenDialog(frameMain);
			if (i == JFileChooser.APPROVE_OPTION) {
				File f = filePDG.getSelectedFile();
				if (f.getName().endsWith(".xml") == false && f.getName().endsWith(".XML") == false) {
					panelInputPDG.setBackground(Color.RED);
					btnExtract.setEnabled(false);
					fieldPDGInputXML.setText("INVALID FILE FORMAT. EXPECTING XML FILE");
				} else {
					panelInputPDG.setBackground(null);
					fieldPDGInputXML.setText(f.getPath());
					fieldPDGInputXML.setEnabled(false);
					btnExtract.setEnabled(true);
				}

			}
		} else if(event.getSource() == chkRSL) {
			panelResult.setVisible(false);
			if(chkRSL.isSelected()) {
				panelRSL.setVisible(true);
			}else {
				panelRSL.setVisible(false);
			}
			
		} else if(event.getSource() == chkPDG) {
			panelResult.setVisible(false);
			if(chkPDG.isSelected()) {
				panelInputPDG.setVisible(true);
			}else {
				panelInputPDG.setVisible(false);
			}
			
		
		} else if (event.getSource() == btnExtract) {
			StringBuilder result = new StringBuilder();
			ArrayList<String> polFoundInJuice;
			ArrayList<String> polFoundInPDG;
			XMLUtility objXMLUtility = new XMLUtility();
			try {
				if (fieldInputJUICE.getText().isEmpty()) {
					result.append("Please provide input JUICE/PDG XML");
					panelInputXML.setBackground(Color.RED);
				} else if (fieldPolNumbers.getText().isEmpty()) {
					result.append("Please provide the policy numbers you want to extract.");
					panelInputPolNumbers.setBackground(Color.RED);
				} else if(chkRSL.isSelected() && fieldRSLExtract.getText().isEmpty()) {
					result.append("Please provide RSL extract input file");
					panelRSL.setBackground(Color.RED);
				} else if(chkPDG.isSelected() && fieldPDGInputXML.getText().isEmpty()) {
					result.append("Please provide PDG input file");
					panelInputPDG.setBackground(Color.RED);					
				} else {
					
					panelInputXML.setBackground(null);
					panelInputPolNumbers.setBackground(null);
					panelRSL.setBackground(null);
					progressBar.setVisible(true);
																	        
					ArrayList<String> inputPolNum = new ArrayList<>(
							Arrays.asList(fieldPolNumbers.getText().split(",")));
					ArrayList<String> tempInputPolNum = new ArrayList<>(
							Arrays.asList(fieldPolNumbers.getText().split(",")));
					if(chkPDG.isSelected()) {
						polFoundInJuice = objXMLUtility.searchPolicies(fieldInputJUICE.getText(), inputPolNum);
						polFoundInPDG = objXMLUtility.searchPolicies(fieldPDGInputXML.getText(), polFoundInJuice);
						boolean resultOfPolComparision = inputPolNum.retainAll(polFoundInPDG);
						objXMLUtility.filterXML(fieldInputJUICE.getText(), polFoundInPDG,"output-JUICE.xml");
						objXMLUtility.filterPDGXML(fieldPDGInputXML.getText(), polFoundInPDG,"output-PDG.xml");
						if(resultOfPolComparision) {
							tempInputPolNum.removeAll(inputPolNum);
							result.append("These policies are missing in either Juice or PDG. Hence not extracted:" + tempInputPolNum.toString());
							result.append("\n"+ "All the client reference numbers corresponding to found policies are pulled into client-numbers.csv file"+ "\n");
						}else {
							result.append(
									"All policy number(s) provided are found and successfully extracted");
							result.append("\n" + "All the client reference numbers are pulled into client-numbers.csv file"+ "\n");
						}
						
					}else {
						polFoundInJuice = objXMLUtility.searchPolicies(fieldInputJUICE.getText(), inputPolNum);						
						boolean resultOfPolComparision = inputPolNum.retainAll(polFoundInJuice);
						objXMLUtility.filterXML(fieldInputJUICE.getText(), polFoundInJuice,"output.xml");						
						if(resultOfPolComparision) {
							tempInputPolNum.removeAll(inputPolNum);
							result.append("These policies are missing in either Juice or PDG. Hence not extracted:" + tempInputPolNum.toString()+ "\n");
							if(XMLUtility.type.equalsIgnoreCase("JUICE")) result.append( "All the client reference numbers corresponding to found policies are pulled into client-numbers.csv file"+ "\n");
						}else {
							result.append(
									"All policy number(s) provided are found and successfully extracted" + "\n");
							if(XMLUtility.type.equalsIgnoreCase("JUICE"))  result.append( "All the client reference numbers are pulled into client-numbers.csv file"+ "\n");
						}
						
					}
					
					

					if (chkRSL.isSelected()) {
						File clientFile = new File(fieldRSLExtract.getText());
						ArrayList<String> clientArrayList = objXMLUtility.getListFromCSV("client-numbers.csv");
						
						if (clientFile.exists() && (clientArrayList.size() != 0)) {
							objXMLUtility.extractClientData(clientArrayList,fieldRSLExtract.getText());
							result.append("Client details are succefully extracted from RSL extract into ets-client-extract.csv file");
						} else {
							result.append("Client data is not extracted due to either one of the reasons");
							result.append("\n");
							result.append("1. Input XML is PDG extract and no client details are present");
							result.append("\n");
							result.append("2. RSL extract is missing or corrupt ");
						}
					}

				}
			} catch (Exception e) {
				
				result.append(e.getMessage());
				result.append("\n" + e.getStackTrace().toString());
			
			

			} finally {
				progressBar.setVisible(false);
				panelResult.setVisible(true);
				fieldResult.setText(result.toString());
			}

		}

	}

}
