package project;

import javax.xml.stream.*;
import javax.xml.stream.events.XMLEvent;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Collectors;

public class XMLUtility {

	static String type;

	/*
	 * public static void main(String[] args) {
	 * 
	 * System.out.println("Hello world!"); String inputXMLFileName = "input.xml";
	 * XMLUtility objXMLUtility = new XMLUtility(); ArrayList<String> inputPolNum =
	 * objXMLUtility.getListFromCSV("pol_numbers.csv"); ArrayList<String>
	 * extractedPolNum = objXMLUtility.filterXML(inputXMLFileName, inputPolNum); if
	 * (inputPolNum.size() != extractedPolNum.size()) {
	 * inputPolNum.removeAll(extractedPolNum);
	 * System.out.println("These policies are not found in the extract: " +
	 * inputPolNum.toString()); } else { System.out.
	 * println("All policy number(s) provided in the csv file are found and successfully extracted"
	 * ); }
	 * 
	 * if(XMLUtility.type.equalsIgnoreCase("JUICE")){ File clientFile = new
	 * File("rsl-input.txt"); ArrayList<String> clientArrayList =
	 * objXMLUtility.getListFromCSV("client-numbers.csv"); if(clientFile.exists() &&
	 * (clientArrayList.size() !=0)) {
	 * objXMLUtility.extractClientData(clientArrayList); } else { System.out.
	 * println("Client data is not extracted due to either one of the reasons");
	 * System.out.
	 * println("1. Input XML is PDG extract and no client details are present");
	 * System.out.println("2. RSL extract is not provided in path as rsl-input.txt"
	 * ); } }
	 * 
	 * }
	 */
	public ArrayList<String> getListFromCSV(String csvInputFile) {
		ArrayList<String> requestedNo = null;

		try (BufferedReader pol_numbers = new BufferedReader(new FileReader(csvInputFile));) {

			String polNumbersInput = "";
			String strCurrentLine;
			while ((strCurrentLine = pol_numbers.readLine()) != null) {
				polNumbersInput = polNumbersInput.concat(strCurrentLine);
			}

			requestedNo = new ArrayList<>(Arrays.asList(polNumbersInput.toString().split(",")));

		} catch (IOException e) {
			e.printStackTrace();
		}
		return requestedNo;

	}

	public void filterXML(String inputXML, ArrayList<String> inputPolNumbers, String outputFileName) {
		boolean toPrint = false;
		boolean client_required = false;

		try (PrintWriter writeClientRef = new PrintWriter(new File("client-numbers.csv"));
				FileOutputStream fileOutPut = new FileOutputStream(outputFileName);) {

			XMLInputFactory factory = XMLInputFactory.newInstance();
			XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
			XMLStreamReader eventReader = factory.createXMLStreamReader(inputXML, new FileInputStream(inputXML));
			XMLStreamWriter streamWriter;

			streamWriter = outputFactory.createXMLStreamWriter(fileOutPut);
			streamWriter.writeStartDocument();
			streamWriter.writeCharacters("\n");

			System.out.println("Please be patient while I process your XML file");
			while (eventReader.hasNext()) {
				int eventType = eventReader.next();

				switch (eventType) {
				case XMLEvent.START_ELEMENT:
					String startElement = getWholeTag(eventReader);
					String qName = eventReader.getLocalName();
					if (qName.equalsIgnoreCase("UICSLoadRequestList")) {
						streamWriter.writeDTD(startElement.toString());
						streamWriter.writeCharacters("\n");
						if (startElement.contains("LSTSchema")) {
							XMLUtility.type = "JUICE";
						} else {
							XMLUtility.type = "PDG";
						}

					}
					/*
					 * if (qName.equalsIgnoreCase("UICSLoadRequestList")) {
					 * 
					 * for (int i = 0; i < eventReader.getNamespaceCount(); i++) { if
					 * (eventReader.getNamespaceURI(i).contains("LSTSchema")) { type = "Juice"; }
					 * 
					 * }
					 * 
					 * if (type.equalsIgnoreCase("JUICE")) { streamWriter.writeDTD(
					 * "<UICSLoadRequestList xmlns:ufsalst=\"http://unisys.financial.framework/LSTschema\" "
					 * + " xmlns:ufsa=\"https://unisys.financial.framework/UDTSchema>\"" +
					 * " xsi:schemaLocation=\"http://unisys.financial.framework/LSTSchema srdzyapp0036.insim.biz/Informatica/UL_Migration/XSD Structure/LSTSchema.xsd "
					 * +
					 * "http://unisys.financial.framework/UDTSchema srdzyapp0036.insim.biz/Informatica/UL_Migration/XSD structure/UDTSchema.xsd\" "
					 * + " xmlns:xsi = \"http://www.w3.org/2001/XMLSchema-instance\">");
					 * streamWriter.writeCharacters("\n"); } else { streamWriter.writeDTD(
					 * "<UICSLoadRequestList xmlns:ufsa=\"https://unisys.financial.framework/UDTSchema>\""
					 * +
					 * " xsi:schemaLocation=\"http://unisys.financial.framework/UDTSchema srdzyapp0036.insim.biz/Informatica/UL_Migration/XSD structure/UDTSchema.xsd\" "
					 * + " xmlns:xsi = \"http://www.w3.org/2001/XMLSchema-instance\">");
					 * streamWriter.writeCharacters("\n"); }
					 * 
					 * }
					 */

					if (qName.equalsIgnoreCase("UICSLoadRequest")) {
						String tempPolicyNum = eventReader.getAttributeValue("", "id");
						if (inputPolNumbers.contains(tempPolicyNum)) {
							toPrint = true;

						}

					}

					if (toPrint) {
						streamWriter.writeDTD(startElement.toString());
						if (qName.equalsIgnoreCase("clientRef")) {
							client_required = true;
						}
					}

					break;
				case XMLStreamConstants.CHARACTERS:
					if (toPrint) {
						streamWriter.writeCharacters(eventReader.getTextCharacters(), eventReader.getTextStart(), eventReader.getTextLength());
						//streamWriter.writeDTD(eventReader.getText());
					}
					if (client_required) {
						writeClientRef.write(eventReader.getText() + ",");
					}
					break;
				case XMLStreamConstants.END_ELEMENT:
					String endElement = "</" + eventReader.getLocalName() + ">";
					String qNames = eventReader.getLocalName();
					if (toPrint) {

						streamWriter.writeDTD(endElement);
						if (qNames.equalsIgnoreCase("UICSLoadRequest")) {
							streamWriter.writeCharacters("\n");
							toPrint = false;
							writeClientRef.write("\n");
						}

					}
					if (qNames.equalsIgnoreCase("clientRef")) {
						client_required = false;
					}

					break;
				case XMLStreamConstants.NAMESPACE:
					System.out.println("I am here");
					break;
				}
			}
			streamWriter.writeDTD("</UICSLoadRequestList>");
			streamWriter.flush();
			streamWriter.close();

			// writeClientRef.flush();
			// writeClientRef.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void filterPDGXML(String inputXML, ArrayList<String> inputPolNumbers, String outputFileName) {
		boolean toPrint = false;

		try (FileOutputStream fileOutput = new FileOutputStream(outputFileName);) {
			XMLStreamWriter streamWriter;
			XMLInputFactory factory = XMLInputFactory.newInstance();
			XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
			XMLStreamReader eventReader = factory.createXMLStreamReader(inputXML, new FileInputStream(inputXML));
			// FileOutputStream fileOutput = new FileOutputStream(outputFileName);
			streamWriter = outputFactory.createXMLStreamWriter(fileOutput);
			streamWriter.writeStartDocument();
			streamWriter.writeCharacters("\n");

			while (eventReader.hasNext()) {
				int eventType = eventReader.next();

				switch (eventType) {
				case XMLEvent.START_ELEMENT:
					String startElement = getWholeTag(eventReader);
					String qName = eventReader.getLocalName();
					if (qName.equalsIgnoreCase("UICSLoadRequestList")) {
						streamWriter.writeDTD(startElement.toString());
						streamWriter.writeCharacters("\n");

					}

					if (qName.equalsIgnoreCase("UICSLoadRequest")) {
						String tempPolicyNum = eventReader.getAttributeValue("", "id");
						if (inputPolNumbers.contains(tempPolicyNum)) {
							toPrint = true;

						}

					}

					if (toPrint) {
						streamWriter.writeDTD(startElement.toString());
					}

					break;
				case XMLStreamConstants.CHARACTERS:
					if (toPrint) {
						streamWriter.writeCharacters(eventReader.getTextCharacters(), eventReader.getTextStart(), eventReader.getTextLength());
						//streamWriter.writeDTD(eventReader.getText());
					}

					break;
				case XMLStreamConstants.END_ELEMENT:
					String endElement = "</" + eventReader.getLocalName() + ">";
					String qNames = eventReader.getLocalName();
					if (toPrint) {

						streamWriter.writeDTD(endElement);
						if (qNames.equalsIgnoreCase("UICSLoadRequest")) {
							streamWriter.writeCharacters("\n");
							toPrint = false;

						}

					}

					break;
				case XMLStreamConstants.NAMESPACE:
					System.out.println("I am here");
					break;
				}
			}
			streamWriter.writeDTD("</UICSLoadRequestList>");
			streamWriter.flush();
			streamWriter.close();
			// fileOutput.close();
			// writeClientRef.flush();
			// writeClientRef.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public String getWholeTag(XMLStreamReader eventReader) {

		StringBuffer startElement = new StringBuffer();
		String qName = eventReader.getLocalName();

		// open element
		startElement.append("<");
		startElement.append(qName);

		if (eventReader.getNamespaceCount() != 0) {
			startElement.append(" ");
			for (int i = 0; i < eventReader.getNamespaceCount(); i++) {
				startElement.append("xmlns:" + eventReader.getNamespacePrefix(i));
				startElement.append("= \"" + eventReader.getNamespaceURI(i) + "\" ");
				startElement.append(" ");
			}

		}
		// add any attributes

		if (eventReader.getAttributeCount() != 0) {

			for (int i = 0; i < eventReader.getAttributeCount(); i++) {
				startElement.append(" ");
				if (!(eventReader.getAttributePrefix(i)).isEmpty())
					startElement.append(eventReader.getAttributePrefix(i) + ":");
				startElement.append(eventReader.getAttributeLocalName(i));
				startElement.append("= \"" + eventReader.getAttributeValue(i) + "\"");
				startElement.append(" ");
			}

		}

		startElement.append(">");
		return startElement.toString();
	}

	public void extractClientData(ArrayList<String> clientList, String filePath) {
		clientList = (ArrayList<String>) clientList.stream().distinct().collect(Collectors.toList());

		try (BufferedReader eslReader = new BufferedReader(new FileReader(filePath));
				BufferedWriter clientWriter = new BufferedWriter(new FileWriter("ets-client-extract.csv"))) {
			ClientSchema objClientSchema = new ClientSchema();
			ArrayList<ClientSchema> lstClientSchema = objClientSchema.getClientSchema();
			for (Iterator<ClientSchema> iterator = lstClientSchema.iterator(); iterator.hasNext();) {
				clientWriter.write(iterator.next().fieldName + ",");
			}
			clientWriter.newLine();
			for (String line = null; (line = eslReader.readLine()) != null;) {

				if (clientList.contains(line.substring(601, 610))) {
					for (Iterator<ClientSchema> iterator = lstClientSchema.iterator(); iterator.hasNext();) {
						ClientSchema temp = iterator.next();
						clientWriter.write(line.substring(temp.startIndex, temp.endIndex) + ",");
					}
					clientWriter.newLine();
				}

			}
			System.out.println("Client details are succefully extracted from RSL extract");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String removeZero(String str) {

		int i = 0;
		while (i < str.length() && str.charAt(i) == '0')
			i++;

		StringBuffer sb = new StringBuffer(str);

		sb.replace(0, i, "");

		return sb.toString();
	}

	public ArrayList<String> searchPolicies(String inputXML, ArrayList<String> inputPolNumbers) {
		ArrayList<String> foundPol = new ArrayList<String>();

		try {
			XMLInputFactory factory = XMLInputFactory.newInstance();
			XMLStreamReader eventReader = factory.createXMLStreamReader(inputXML, new FileInputStream(inputXML));
			while (eventReader.hasNext()) {
				int eventType = eventReader.next();

				switch (eventType) {
				case XMLEvent.START_ELEMENT:
					String qName = eventReader.getLocalName();
					if (qName.equalsIgnoreCase("UICSLoadRequest")) {
						String tempPolicyNum = eventReader.getAttributeValue("", "id");
						if (inputPolNumbers.contains(tempPolicyNum)) {
							foundPol.add(tempPolicyNum);
						}
					}
				}
			}

		} catch (FileNotFoundException | XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return foundPol;
	}
}
