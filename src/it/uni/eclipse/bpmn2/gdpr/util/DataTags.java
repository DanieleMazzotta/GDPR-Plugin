package it.uni.eclipse.bpmn2.gdpr.util;

import org.eclipse.bpmn2.modeler.core.model.ModelDecorator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

//TODO: Document this
public class DataTags {

	public static Object getTagValue(EObject be, String tag) {
		EStructuralFeature f = ModelDecorator.getAnyAttribute(be, tag);
		if (f != null) {
			Object id = be.eGet(f);
			
			return id;
		}
		
		return null;
	}
	
	public static boolean containsPersonalData(EObject be) {
		return Boolean.parseBoolean(String.valueOf(getTagValue(be, "IsPersonalData")));
	}
	
	public static String getElementID(EObject be) {
		return be.toString().split(":")[1].split(",")[0].trim();
	}
}
