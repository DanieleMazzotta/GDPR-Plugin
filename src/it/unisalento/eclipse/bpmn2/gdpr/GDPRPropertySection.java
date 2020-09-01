package it.unisalento.eclipse.bpmn2.gdpr;

import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.DefaultDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.DefaultPropertySection;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import it.unisalento.eclipse.bpmn2.gdpr.gfx.GDPRTagEditor;
import it.unisalento.eclipse.bpmn2.gdpr.util.DataTags;
import it.unisalento.eclipse.bpmn2.gdpr.util.XMLTagParser;

//TODO: Document this
public class GDPRPropertySection extends DefaultPropertySection {

	public GDPRPropertySection() {
		super();
	}

	protected AbstractDetailComposite createSectionRoot() {
		return new GDPRDetailComposite(this);
	}

	public AbstractDetailComposite createSectionRoot(Composite parent, int style) {
		return new GDPRDetailComposite(parent, style);

	}

	public class GDPRDetailComposite extends DefaultDetailComposite {
		Text infoText;

		public GDPRDetailComposite(DefaultPropertySection section) {
			super(section);
		}

		public GDPRDetailComposite(Composite parent, int style) {
			super(parent, style);
		}

		@Override
		public void createBindings(EObject be) {
			// Get element id from BPMN diagram and add it to XML data
			String elementID = DataTags.getElementID(be);
			XMLTagParser.addNewElement(elementID);

			// Set properties
			setTitle("GDPR section");
			bindProperty(be, "IsPersonalData");
			setLayout(new GridLayout(1, false));

			// InfoText
			toolkit.createLabel(this, "PersonalData");
			infoText = toolkit.createText(this, XMLTagParser.getElementTagsAndContent(elementID),
					SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
			infoText.setEditable(false);
			GridData gr = new GridData(SWT.FILL, SWT.FILL, true, false);
			gr.heightHint = 5 * infoText.getLineHeight();
			infoText.setLayoutData(gr);
			toolkit.createLabel(this, ""); // NEWLINE

			// Edit Button
			Button editDataButton = toolkit.createButton(this, "Edit Data", SWT.PUSH);
			editDataButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					new GDPRTagEditor(elementID);
				}
			});

			// Only enable "Edit Data" button if the current Task is set as a GDPR task  
			for (EAttribute attr : be.eClass().getEAllAttributes()) {
				if (be.eGet(attr) == null)
					continue;

				if (be.eGet(attr).toString().contains("[ext:IsPersonalData=false"))
					editDataButton.setEnabled(false);
				if (be.eGet(attr).toString().contains("[ext:IsPersonalData=true"))
					editDataButton.setEnabled(true);
			}
		}
	}
}
