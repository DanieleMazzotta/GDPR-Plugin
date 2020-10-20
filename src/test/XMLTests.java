package test;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.unisalento.eclipse.bpmn2.gdpr.util.XMLTagParser;

/**
 * Test XMLTagParser functionalities
 */
public class XMLTests {
	// Test directory and file
	static File testDir = new File("tests/");
	static File file = new File("tests/test.xml");

	// Create dir and file at start
	@BeforeClass
	public static void init() {
		if (!testDir.isDirectory())
			testDir.mkdir();

		if (file.isFile())
			file.delete();
	}

	// Before each test, init the parser
	@Before
	public void initParser() {
		XMLTagParser.init(file);
	}

	// Test adding new element to xml
	@Test
	public void addElement() {
		XMLTagParser.addNewElement("Person");
		assertEquals(XMLTagParser.containsElement("Person"), true);
	}

	// Test adding new tag to element
	@Test
	public void addAttribute() {
		XMLTagParser.addNewElement("Person");

		XMLTagParser.editElement("Person", "name", "tony");
		assertEquals(XMLTagParser.getTagValueFromElement("Person", "name"), "tony");
	}
	
	// Test adding multiple tags to element
	@Test
	public void addAttributeMultiple() {
		XMLTagParser.addNewElement("Person");

		XMLTagParser.editElement("Person", "name", "tony");
		assertEquals(XMLTagParser.getTagValueFromElement("Person", "name"), "tony");
		
		XMLTagParser.editElement("Person", "surname", "blaire");
		assertEquals(XMLTagParser.getTagValueFromElement("Person", "surname"), "blaire");
	}

	// Test adding multiple properties to a tag
	@Test
	public void addProperty() {
		XMLTagParser.addNewElement("Person");
		XMLTagParser.editElement("Person", "residence", "milan");

		XMLTagParser.editProperty("Person", "residence", "isRenting", "false");
		assertEquals(XMLTagParser.getPropertyValueFromTag("Person", "residence", "isRenting"), "false");
		XMLTagParser.editProperty("Person", "residence", "hasGarage", "true");
		assertEquals(XMLTagParser.getPropertyValueFromTag("Person", "residence", "hasGarage"), "true");
	}

	// Test deleting tags
	@Test
	public void deleteTag() {
		XMLTagParser.addNewElement("Person");
		XMLTagParser.editElement("Person", "residence", "milan");

		XMLTagParser.deleteTagFromElement("Person", "residence");
		assertEquals(XMLTagParser.getTagValueFromElement("Person", "residence"), null);
	}

	// Delete dir and file after
	@AfterClass
	public static void cleanup() {
		testDir.delete();
		file.delete();
	}
}
