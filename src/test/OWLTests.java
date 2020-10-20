package test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import it.unisalento.eclipse.bpmn2.gdpr.util.owl.OntologyReader;
import it.unisalento.eclipse.bpmn2.gdpr.util.owl.OwlEntity;

/**
 * Test OWLReader functionalities
 */
public class OWLTests {
	// Setup ontology before each test
	@Before
	public void init() {
		OntologyReader.setupOntology("resources/gdpr.owl");
	}

	// Test class name reading
	@Test
	public void getNameFromOntology() {
		String name = OntologyReader.getEntityByName("PersonalData").getName();
		assertEquals(name, "PersonalData");
	}

	// Test obtaining all properties from a class
	@Test
	public void getPropertiesFromClass() {
		OwlEntity e = OntologyReader.getEntityByName("PersonalData");
		assertEquals(e.getProperties().size(), 2);
		assertEquals(e.getProperties().get(0).getName(), "accessToData");
		assertEquals(e.getProperties().get(1).getName(), "dataDuration");
	}
}
