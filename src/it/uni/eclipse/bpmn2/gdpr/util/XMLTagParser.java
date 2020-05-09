package it.uni.eclipse.bpmn2.gdpr.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

//TODO: Really really really document properly
//TODO: Add deletetag
//TODO: Tags will be like
/*
 * <UserTask_1>
 *     <PersonalDataType>Medical</>
 *     <Duration>1Year</>
 * </UserTask_1>
 * 
 * All of them will be editable / deletable from the button in GDPRPropertySection
 */
public class XMLTagParser {
	// The xml file relative to the project
	private static File dataFile = new File(ProjectUtils.getCurrentProjectPath() + "xmlData.xml");;

	// The xml working tools and the root node
	private static DocumentBuilderFactory documentFactory;
	private static DocumentBuilder documentBuilder;
	private static Document document;
	private static Node root;

	/**
	 * Edits element with elementID specified, by writing content in attribute if it
	 * exists, and creating it if it doesn't
	 */
	public static void editElement(String elementID, String attribute, String content) {
		// Get the task node
		Node fnd = getNodeByTaskName(elementID);

		// Edit if exists
		NodeList L = fnd.getChildNodes();
		for (int i = 0; i < L.getLength(); i++) {
			Node e = L.item(i);
			if (e.getNodeName().equals(attribute)) {
				e.setTextContent(content);

				try {
					writeChangesToFile();
				} catch (TransformerException e1) {
					e1.printStackTrace();
				}
				return;
			}
		}

		// Create if doesn't exist
		Element newTag = document.createElement(attribute);
		newTag.appendChild(document.createTextNode(content));
		fnd.appendChild(newTag);

		try {
			writeChangesToFile();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the node relative to the Task name, coming from the root node
	 */
	public static Node getNodeByTaskName(String elementID) {
		root = document.getFirstChild();
		NodeList L = root.getChildNodes();
		for (int i = 0; i < L.getLength(); i++) {
			Node e = L.item(i);
			if (e.getNodeName().equals(elementID))
				return e;
		}

		return null;
	}

	/**
	 * Adds an element to the xmlData if it doesn't already exist
	 */
	public static void addNewElement(String elementID) {
		// Create the file if it doesn't exist
		if (!dataFile.isFile())
			initFile();

		// If element exists already, don't add it
		if (containsElement(elementID))
			return;

		// Get root element
		root = document.getFirstChild();

		Element newElement = document.createElement(elementID);
		root.appendChild(newElement);

		try {
			writeChangesToFile();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks whether the element specified exists in the xml already
	 */
	public static boolean containsElement(String elementID) {
		return document.getElementsByTagName(elementID).getLength() != 0;
	}

	/**
	 * Called at plugin start, it sets the main tools to work on the xml file
	 */
	public static void init() {
		documentFactory = DocumentBuilderFactory.newInstance();
		try {
			documentBuilder = documentFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		// If the file exists already, load it in memory
		if (dataFile.isFile()) {
			try {
				document = documentBuilder.parse(dataFile);
			} catch (SAXException | IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Creates the xml file if it doesn't exist
	 */
	private static void initFile() {
		try {
			dataFile.createNewFile();
			writeHeader();

			document = documentBuilder.parse(dataFile);
		} catch (IOException | SAXException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Manually write xml header
	 */
	private static void writeHeader() throws IOException {
		FileWriter myWriter = new FileWriter(dataFile);
		myWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<project>\n</project>\n");
		myWriter.close();
	}

	/**
	 * Save all changes made to file (remember to do it manually every time)
	 */
	private static void writeChangesToFile() throws TransformerException {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource domSource = new DOMSource(document);
		StreamResult streamResult = new StreamResult(dataFile);

		transformer.transform(domSource, streamResult);
	}
}
