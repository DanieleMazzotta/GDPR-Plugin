package it.unisalento.eclipse.bpmn2.gdpr.util.owl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class OntologyReader {
	public static File owl; // The ontology file
	public static String[] fileLines; // The parsed ontology file
	public static ArrayList<OwlEntity> entities; // The mapped ontology

	/**
	 * Returns the entity in the ontology with the specified name if it exists, null
	 * otherwise
	 */
	public static OwlEntity getEntityByName(String name) {
		for (OwlEntity e : entities) {
			if (e.getName().equals(name))
				return e;
		}

		return null;
	}

	/**
	 * Get the ontology mapped in memory
	 */
	public static ArrayList<OwlEntity> getOntology() {
		return entities;
	}

	/**
	 * Returns an String array containing all names of entities in the ontology
	 */
	public static String[] getAllEntitiesName() {
		ArrayList<String> a = new ArrayList<>();
		for (OwlEntity e : entities) {
			a.add(e.getName());
		}

		String[] array = new String[a.size()];
		array = a.toArray(array);

		return array;
	}

	/**
	 * Must be called first to setup the ontology in regard to the file @ the
	 * specified path
	 */
	public static void setupOntology(String ontologyPath) {
		owl = new File(ontologyPath);
		if (!owl.isFile()) {
			JOptionPane.showMessageDialog(null, "GDPR Plugin", "Could not find the file " + owl.getName() + "...",
					JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}

		fileLines = readLineByLine();
		entities = getAllEntities();
		associateSubclassesAndProperties();
	}

	/**
	 * Reads the file into memory line by line
	 */
	private static String[] readLineByLine() {
		ArrayList<String> fileLines = new ArrayList<>();
		BufferedReader reader;

		try {
			reader = new BufferedReader(new FileReader(owl));
			String line = reader.readLine();
			while (line != null) {
				// Ignore the comments
				if (!line.startsWith("#"))
					fileLines.add(line);

				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String[] array = new String[fileLines.size()];
		array = fileLines.toArray(array);

		return array;
	}

	/**
	 * Parses all entities in the line-by-line reading of the owl file
	 */
	private static ArrayList<OwlEntity> getAllEntities() {
		ArrayList<OwlEntity> list = new ArrayList<>();

		for (String s : fileLines) {
			if (s.startsWith("Declaration(Class")) {
				String className = s.split(":")[1].replace(")", "");
				list.add(new OwlEntity(className));
			}
		}

		return list;
	}

	/**
	 * Maps sub-entities and properties for every entity in the ontology
	 */
	private static void associateSubclassesAndProperties() {
		for (OwlEntity e : entities) {
			for (String s : fileLines) {
				// Get all subclasses for e
				if (s.startsWith("SubClassOf(") && s.endsWith(e.getName() + ")")) {
					e.addSubclass(getEntityByName(s.split(":")[1].replace(" ", "")));
				}

				// Get all properties for e
				if (s.startsWith("ObjectPropertyDomain(") && s.endsWith(e.getName() + ")")) {
					e.addProperty(new OwlProperty(s.split(":")[1].replace(" ", "")));
				}
			}
		}
	}
}
