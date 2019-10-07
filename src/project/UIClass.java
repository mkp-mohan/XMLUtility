package project;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * @author Mohan Palukuri
 *
 */

public class UIClass implements ActionListener {

	JFrame frameMain;
	JLabel lblJuiceInputXML;
	JLabel lblDesiredPolNumbers;
	JLabel lblCheckBox;
	JLabel lblRSLExtract;
	JLabel lblResult;
	JLabel lblPDGXML;
	JTextField fieldInputXML;
	JTextField fieldPDGInputXML;
	JTextArea fieldPolNumbers;
	JTextArea fieldResult;
	JTextField fieldRSLExtract;
	JButton btnExtract;
	JButton btnXMLUpload;
	JButton btnTxtUpload;
	JButton btnPDGUpload;
	JPanel panelInputXML;
	JPanel panelInputPolNumbers;
	JPanel panelResult;
	JPanel panelRSL;
	JFileChooser fileXML;
	JFileChooser fileTxt;
	JCheckBox chkRSL;
	
	public UIClass() {
		frameMain = new JFrame("NN-Test Data Utility");

		lblJuiceInputXML = new JLabel("JUICE Input XML:");
		fieldInputXML = new JTextField(40);
		btnXMLUpload = new JButton("Upload");

		panelInputXML = new JPanel();

		panelInputXML.add(lblJuiceInputXML);
		panelInputXML.add(fieldInputXML);
		panelInputXML.add(btnXMLUpload);

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
		
		btnExtract = new JButton("Filter and Extract data");

		panelResult = new JPanel();
		lblResult = new JLabel("Result:");
		fieldResult = new JTextArea(5, 50);
		panelResult.add(lblResult);
		panelResult.add(fieldResult);
		panelResult.setVisible(false);

		frameMain.setVisible(true);
		frameMain.setSize(820, 500);
		frameMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameMain.setLayout(new FlowLayout());
		

		frameMain.add(panelInputXML);
		frameMain.add(new JSeparator());
		frameMain.add(panelInputPolNumbers);
		frameMain.add(new JSeparator(SwingConstants.HORIZONTAL));
		frameMain.add(chkRSL);
		frameMain.add(panelRSL);
		frameMain.add(btnExtract);		
		frameMain.add(panelResult);

		btnXMLUpload.addActionListener(this);
		btnTxtUpload.addActionListener(this);
		chkRSL.addActionListener(this);
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
				if (f.getName().endsWith(".xml") == false) {
					panelInputXML.setBackground(Color.RED);
					btnExtract.setEnabled(false);
					fieldInputXML.setText("INVALID FILE FORMAT. EXPECTING XML FILE");
				} else {
					panelInputXML.setBackground(null);
					fieldInputXML.setText(f.getPath());
					fieldInputXML.setEnabled(false);
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
		} else if(event.getSource() == chkRSL) {
			panelResult.setVisible(false);
			if(chkRSL.isSelected()) {
				panelRSL.setVisible(true);
			}else {
				panelRSL.setVisible(false);
			}
			
		
		} else if (event.getSource() == btnExtract) {
			StringBuilder result = new StringBuilder();

			XMLUtility objXMLUtility = new XMLUtility();
			try {
				if (fieldInputXML.getText().isEmpty()) {
					result.append("Please provide input JUICE/PDG XML");
					panelInputXML.setBackground(Color.RED);
				} else if (fieldPolNumbers.getText().isEmpty()) {
					result.append("Please provide the policy numbers you want to extract.");
					panelInputPolNumbers.setBackground(Color.RED);
				} else if(chkRSL.isSelected() && fieldRSLExtract.getText().isEmpty()) {
					result.append("Please provide RSL extract input file");
					panelRSL.setBackground(Color.RED);
					
				} else {

					panelInputXML.setBackground(null);
					panelInputPolNumbers.setBackground(null);
					panelRSL.setBackground(null);

					ArrayList<String> inputPolNum = new ArrayList<>(
							Arrays.asList(fieldPolNumbers.getText().split(",")));
					ArrayList<String> extractedPolNum = objXMLUtility.filterXML(fieldInputXML.getText(), inputPolNum);
					if (inputPolNum.size() != extractedPolNum.size()) {
						inputPolNum.removeAll(extractedPolNum);
						result.append("These policies are not found in the extract: " + inputPolNum.toString());
						result.append("\n");
					} else {
						result.append(
								"All policy number(s) provided in the csv file are found and successfully extracted");
						result.append("\n" + "All the client reference numbers are pulled into client-numbers.csv file"+ "\n");
					}

					if (XMLUtility.type.equalsIgnoreCase("JUICE") && chkRSL.isSelected()) {
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
				panelResult.setVisible(true);
				fieldResult.setText(result.toString());
			}

		}

	}

}
