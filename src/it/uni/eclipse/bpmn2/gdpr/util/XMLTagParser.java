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

public class XMLTagParser {
	// The xml file relative to the project
	private static File dataFile = new File(ProjectUtils.getCurrentProjectPath() + "xmlData.xml");;

	// The xml working tools and the root node
	private static DocumentBuilderFactory documentFactory;
	private static DocumentBuilder documentBuilder;
	private static Document document;
	private static Node root;

	/**
	 * Removes tag with name specified (attribute) from task with elementID
	 * specified
	 */
	public static void deleteTagFromElement(String elementID, String attribute) {
		// Get the task node
		Node fnd = getNodeByTaskName(elementID);

		// Delete if exists
		NodeList L = fnd.getChildNodes();
		for (int i = 0; i < L.getLength(); i++) {
			Node e = L.item(i);
			if (e.getNodeName().equals(attribute)) {
				fnd.removeChild(e);
			}
		}

		writeChangesToFile();
	}

	/**
	 * Edits specified property if it exists, creates it otherwise
	 */
	public static void editProperty(String elementID, String attribute, String property, String value) {
		// Get the task node
		Node fnd = getNodeByTaskName(elementID);

		// Edit if exists
		NodeList L = fnd.getChildNodes();
		for (int i = 0; i < L.getLength(); i++) {
			Node e = L.item(i);
			if (e.getNodeName().equals(attribute)) {
				((Element) e).setAttribute(property, value);

				writeChangesToFile();
			}
		}
	}

	/**
	 * Returns the value of the specified attribute (if exists), otherwise null
	 */
	public static String getTagValueFromElement(String elementID, String attribute) {
		// Get the task node
		Node fnd = getNodeByTaskName(elementID);

		// Return content if exists
		NodeList L = fnd.getChildNodes();
		for (int i = 0; i < L.getLength(); i++) {
			Node e = L.item(i);
			if (e.getNodeName().equals(attribute)) {
				return e.getTextContent();
			}
		}

		return null;
	}

	/**
	 * Gets the value of the specified property from a specific attribute of a
	 * specific task
	 */
	public static String getPropertyValueFromTag(String elementID, String attribute, String property) {
		// Get the task node
		Node fnd = getNodeByTaskName(elementID);

		// Return content if exists
		NodeList L = fnd.getChildNodes();
		for (int i = 0; i < L.getLength(); i++) {
			Node e = L.item(i);
			if (e.getNodeName().equals(attribute)) {
				return e.getAttributes().getNamedItem(property).getNodeValue();
			}
		}

		return null;
	}

	/**
	 * Lists and formats (one per line) all of the GDPR properties/tags associated
	 * to a task
	 */
	public static String getElementTagsAndContent(String elementID) {
		// Get the task node
		Node fnd = getNodeByTaskName(elementID);

		// Get all children
		NodeList L = fnd.getChildNodes();

		// No properties
		if (L.getLength() == 0)
			return "Here goes the personal data information";

		// List and format all properties
		StringBuilder strBuilder = new StringBuilder();
		for (int i = 0; i < L.getLength(); i++) {
			Node e = L.item(i);
			strBuilder.append(e.getNodeName() + ": " + e.getTextContent() + "\n");
			for (int j = 0; j < e.getAttributes().getLength(); j++) {
				strBuilder.append("  >" + e.getAttributes().item(j).getNodeName() + ": "
						+ e.getAttributes().item(j).getNodeValue() + "\n");
			}
			
			strBuilder.append("\n");
		}

		return strBuilder.toString();
	}

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

				writeChangesToFile();
				return;
			}
		}

		// Create if doesn't exist
		Element newTag = document.createElement(attribute);
		newTag.appendChild(document.createTextNode(content));
		fnd.appendChild(newTag);

		writeChangesToFile();
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

		writeChangesToFile();
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
	private static void writeChangesToFile() {
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource domSource = new DOMSource(document);
			StreamResult streamResult = new StreamResult(dataFile);

			transformer.transform(domSource, streamResult);
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
}
