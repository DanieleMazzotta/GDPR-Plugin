package it.unisalento.eclipse.bpmn2.gdpr.util;

import org.eclipse.bpmn2.modeler.core.model.ModelDecorator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

public class DataTags {
	/**
	 * Returns the value of property tag of EObject be
	 */
	public static Object getTagValue(EObject be, String tag) {
		EStructuralFeature f = ModelDecorator.getAnyAttribute(be, tag);
		if (f != null) {
			Object id = be.eGet(f);

			return id;
		}

		return null;
	}

	/**
	 * Returns true if the Task is marked as PersonalData, false otherwise
	 */
	public static boolean containsPersonalData(EObject be) {
		return Boolean.parseBoolean(String.valueOf(getTagValue(be, "IsPersonalData")));
	}

	/**
	 * Returns the element (task) ID
	 */
	public static String getElementID(EObject be) {
		return be.toString().split(":")[1].split(",")[0].trim();
	}
}
