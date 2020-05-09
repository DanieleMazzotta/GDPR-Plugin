package it.uni.eclipse.bpmn2.gdpr.customactivities;

import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractPropertiesProvider;
import org.eclipse.bpmn2.modeler.ui.property.tasks.ManualTaskDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.tasks.ManualTaskPropertySection;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;

public class ManualTaskSubstitutePropertySection extends ManualTaskPropertySection {

	public ManualTaskSubstitutePropertySection() {
		super();
	}

	@Override
	protected AbstractDetailComposite createSectionRoot() {
		return new ManualTaskSubstituteDetailComposite(this);
	}

	@Override
	public AbstractDetailComposite createSectionRoot(Composite parent, int style) {
		return new ManualTaskSubstituteDetailComposite(parent, style);

	}

	public class ManualTaskSubstituteDetailComposite extends ManualTaskDetailComposite {

		// protected Section attributesSection = null;
		// protected Composite attributesComposite = null;
		// protected Font descriptionFont = null;
		// protected AbstractPropertiesProvider propertiesProvider = null;
		// protected StyledText descriptionText = null;

		public ManualTaskSubstituteDetailComposite(AbstractBpmn2PropertySection section) {
			super(section);
		}

		public ManualTaskSubstituteDetailComposite(Composite parent, int style) {
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
}
