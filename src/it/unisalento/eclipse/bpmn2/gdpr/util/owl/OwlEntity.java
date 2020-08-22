package it.unisalento.eclipse.bpmn2.gdpr.util.owl;

import java.util.ArrayList;

public class OwlEntity {
	private ArrayList<OwlEntity> subclasses;
	private ArrayList<OwlProperty> properties;
	private String name;
	
	public OwlEntity(String name) {
		subclasses = new ArrayList<>();
		properties = new ArrayList<>();
		
		this.name = name;
	}
	
	public void addSubclass(OwlEntity e) {
		subclasses.add(e);
	}
	
	public ArrayList<OwlEntity> getSubclasses() {
		return subclasses;
	}
	
	public void addProperty(OwlProperty p) {
		properties.add(p);
	}
	
	public ArrayList<OwlProperty> getProperties() {
		return properties;
	}
	
	public String getName() {
		return name;
	}
}
