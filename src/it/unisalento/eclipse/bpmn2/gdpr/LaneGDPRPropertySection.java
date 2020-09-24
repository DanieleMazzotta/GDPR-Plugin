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

public class LaneGDPRPropertySection extends DefaultPropertySection {

	public LaneGDPRPropertySection() {
		super();
	}

	protected AbstractDetailComposite createSectionRoot() {
		return new LaneGDPRDetailComposite(this);
	}

	public AbstractDetailComposite createSectionRoot(Composite parent, int style) {
		return new LaneGDPRDetailComposite(parent, style);
	}

	public class LaneGDPRDetailComposite extends DefaultDetailComposite {
		public LaneGDPRDetailComposite(DefaultPropertySection section) {
			super(section);
		}

		public LaneGDPRDetailComposite(Composite parent, int style) {
			super(parent, style);
		}

		// TODO: What am I supposed to do with lanes?
		@Override
		public void createBindings(EObject be) {
			setTitle("GDPR section");

			/*
			 * this.createDescription(attributesComposite,
			 * "This section is used to set the data protection parameters");
			 * this.bindProperty(be, "IsPersonalDataProcessing"); this.bindProperty(be,
			 * "LegalBasis"); this.bindProperty(be, "Duration");
			 */
			this.bindProperty(be, "IsPersonalDataProcessing");

			setLayout(new GridLayout(1, false));

			toolkit.createLabel(this, "PersonalData");

			Text t = toolkit.createText(this, "Here goes the personal data information",
					SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
			t.setEditable(false);
			GridData gr = new GridData(SWT.FILL, SWT.FILL, true, false);
			gr.heightHint = 5 * t.getLineHeight();
			t.setLayoutData(gr);

			toolkit.createLabel(this, ""); // NEWLINE

			Button button = toolkit.createButton(this, "Edit Data", SWT.PUSH);
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					String a = JOptionPane.showInputDialog(null, "Changind GDPR Tags for Lanes is not yet implemented",
							"Unimplemented Feature", JOptionPane.WARNING_MESSAGE);
					t.setText(a);

					update();
					refresh();
				}
			});

			update();
			refresh();
		}
	}

	@Override
	public boolean appliesTo(EObject eObj) {
		if (super.appliesTo(eObj))
			return eObj != null && ((eObj.eClass().getName().equals("Lane")));//$NON-NLS-1$
		return false;
	}
}
