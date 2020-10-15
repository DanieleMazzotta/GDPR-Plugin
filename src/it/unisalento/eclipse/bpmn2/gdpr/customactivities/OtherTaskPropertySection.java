package it.unisalento.eclipse.bpmn2.gdpr.customactivities;

import org.eclipse.bpmn2.BusinessRuleTask;
import org.eclipse.bpmn2.ReceiveTask;
import org.eclipse.bpmn2.SendTask;
import org.eclipse.bpmn2.ServiceTask;
import org.eclipse.bpmn2.UserTask;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractPropertiesProvider;
import org.eclipse.bpmn2.modeler.ui.property.tasks.ActivityDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.tasks.ActivityPropertySection;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Composite;

public class OtherTaskPropertySection extends ActivityPropertySection {

	public OtherTaskPropertySection() {
		super();
	}

	@Override
	protected AbstractDetailComposite createSectionRoot() {
		return new VariousTaskSubstituteDetailComposite(this);
	}

	@Override
	public AbstractDetailComposite createSectionRoot(Composite parent, int style) {
		return new VariousTaskSubstituteDetailComposite(parent, style);

	}

	public class VariousTaskSubstituteDetailComposite extends ActivityDetailComposite {
		
		public VariousTaskSubstituteDetailComposite(AbstractBpmn2PropertySection section) {
			super(section);
		}

		public VariousTaskSubstituteDetailComposite(Composite parent, int style) {
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

	@Override
	public EObject getBusinessObjectForSelection(ISelection selection) {
		EObject be = super.getBusinessObjectForSelection(selection);
		if (be instanceof UserTask || be instanceof BusinessRuleTask || be instanceof ServiceTask
				|| be instanceof SendTask || be instanceof ReceiveTask)
			return be;
		return null;
	}
}
