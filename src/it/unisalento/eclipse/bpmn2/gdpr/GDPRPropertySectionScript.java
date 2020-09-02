package it.unisalento.eclipse.bpmn2.gdpr;

import javax.swing.JOptionPane;

import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.DefaultDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.DefaultPropertySection;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class GDPRPropertySectionScript extends DefaultPropertySection {

	public GDPRPropertySectionScript() {
		super();
	}

	protected AbstractDetailComposite createSectionRoot() {
		return new GDPRDetailComposite(this);
	}

	public AbstractDetailComposite createSectionRoot(Composite parent, int style) {
		return new GDPRDetailComposite(parent, style);

	}

	public class GDPRDetailComposite extends DefaultDetailComposite {

		public GDPRDetailComposite(DefaultPropertySection section) {
			super(section);
		}

		public GDPRDetailComposite(Composite parent, int style) {
			super(parent, style);
		}
		
		@Override
		public void createBindings(EObject be) {
			setTitle("GDPR section");
			//TargetRuntime rt = TargetRuntime.getRuntime("it.unisalento.eclipse.bpmn2.gdpr.runtime1");

			this.bindProperty(be, "IsPersonalData");
			
			setLayout(new GridLayout(1, false));

			toolkit.createLabel(this, "PersonalData");

			Text t = toolkit.createText(this, "Here goes the personal data information", SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
			t.setEditable(false);
			GridData gr = new GridData(SWT.FILL, SWT.FILL, true, false);
			gr.heightHint = 5 * t.getLineHeight();
	        t.setLayoutData(gr);

			toolkit.createLabel(this, ""); // NEWLINE

			Button button = toolkit.createButton(this, "Edit Data", SWT.PUSH);
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					String a = JOptionPane.showInputDialog(null, "GDPRPropertySectionScript", "????", JOptionPane.QUESTION_MESSAGE);
					t.setText(a);

					update();
					refresh();
				}
			});

			update();
			refresh();
		}

		/*
		 * @Override public void createBindings(EObject be) { setTitle("GDPR section");
		 * this.createDescription(attributesComposite,
		 * "This section is used to set the data protection parameters");
		 * 
		 * TargetRuntime rt =
		 * TargetRuntime.getRuntime("it.unisalento.eclipse.bpmn2.gdpr.runtime1");
		 * 
		 * this.bindProperty(be, "IsPersonalData"); this.bindProperty(be,
		 * "PersonalDataType"); this.bindProperty(be, "DOG");
		 * 
		 * for (int i = 0; i <
		 * rt.getModelExtensionDescriptor(be).getProperties().size(); i++) { if
		 * (rt.getModelExtensionDescriptor(be).getProperties().get(i).name.equals(
		 * "IsPersonalData") &&
		 * rt.getModelExtensionDescriptor(be).getProperties().get(i).name.equals(
		 * "PersonalDataType")) { this.bindProperty(be,
		 * rt.getModelExtensionDescriptor(be).getProperties().get(i).name);
		 * JOptionPane.showMessageDialog(null,
		 * rt.getModelExtensionDescriptor(be).getProperties().get(i).name); } } }
		 */
	}
}
