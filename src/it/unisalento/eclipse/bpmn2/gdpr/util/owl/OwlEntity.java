package it.unisalento.eclipse.bpmn2.gdpr.util.owl;

import java.util.ArrayList;

/**
 * Represents a class in the OWL ontology
 */
public class OwlEntity {
	private ArrayList<OwlEntity> subclasses; // All subclasses
	private ArrayList<OwlProperty> properties; // All properties
	private String name; // The class name

	public OwlEntity(String name) {
		subclasses = new ArrayList<>();
		properties = new ArrayList<>();

		this.name = name;
	}

	/**
	 * Add a subclass to this class
	 */
	public void addSubclass(OwlEntity e) {
		subclasses.add(e);
	}

	/**
	 * Returns all the subclasses for this class
	 */
	public ArrayList<OwlEntity> getSubclasses() {
		return subclasses;
	}

	/**
	 * Adds a property to this class
	 */
	public void addProperty(OwlProperty p) {
		properties.add(p);
	}

	/**
	 * Returns all properties for this class
	 */
	public ArrayList<OwlProperty> getProperties() {
		return properties;
	}

	/**
	 * Returns the class name
	 */
	public String getName() {
		return name;
	}
}
