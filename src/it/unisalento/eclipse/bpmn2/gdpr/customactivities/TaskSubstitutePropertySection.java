package it.unisalento.eclipse.bpmn2.gdpr.customactivities;

import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractPropertiesProvider;
import org.eclipse.bpmn2.modeler.ui.property.tasks.TaskDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.tasks.TaskPropertySection;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;

public class TaskSubstitutePropertySection extends TaskPropertySection {

	public TaskSubstitutePropertySection() {
		super();
	}

	@Override
	protected AbstractDetailComposite createSectionRoot() {
		return new TaskSubstituteDetailComposite(this);
	}

	@Override
	public AbstractDetailComposite createSectionRoot(Composite parent, int style) {
		return new TaskSubstituteDetailComposite(parent, style);

	}

	public class TaskSubstituteDetailComposite extends TaskDetailComposite {

		// protected Section attributesSection = null;
		// protected Composite attributesComposite = null;
		// protected Font descriptionFont = null;
		// protected AbstractPropertiesProvider propertiesProvider = null;
		// protected StyledText descriptionText = null;

		public TaskSubstituteDetailComposite(AbstractBpmn2PropertySection section) {
			super(section);
		}

		public TaskSubstituteDetailComposite(Composite parent, int style) {
			super(parent, style);
		}

		@Override
		public void createBindings(EObject be) {
			AbstractPropertiesProvider provider = getPropertiesProvider(be);
			if (provider == null) {
				createEmptyLabel(be);
			} else {
				String[] properties = provider.getProperties();
				provider.getProperties();
				if (properties != null) {
					getAttributesParent();
					for (String property : properties) {
						if (property != "anyAttribute") {

							bindProperty(be, property);
						}
					}
				}

				if (getAttributesParent().getChildren().length == 0) {
					if (attributesComposite != null) {
						attributesComposite.dispose();
						attributesComposite = null;
						attributesSection.dispose();
						attributesSection = null;
					}
				} else if (attributesSection != null) {
					attributesSection.setRedraw(false);
					boolean expanded = attributesSection.isExpanded();
					attributesSection.setExpanded(!expanded);
					attributesSection.setExpanded(expanded);
					attributesSection.setRedraw(true);
				}
			}
			redrawPage();
		}
	}

	/*
	 * @Override public boolean appliesTo(EObject eObj) { if (super.appliesTo(eObj))
	 * return eObj!=null && (eObj.eClass().getName().equals("Task")); //$NON-NLS-1$
	 * return false; }
	 */

}
