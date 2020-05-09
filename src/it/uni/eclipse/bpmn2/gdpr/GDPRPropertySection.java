package it.uni.eclipse.bpmn2.gdpr;

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

import it.uni.eclipse.bpmn2.gdpr.util.DataTags;
import it.uni.eclipse.bpmn2.gdpr.util.XMLTagParser;

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
			Text infoText = toolkit.createText(this, XMLTagParser.getElementTagsAndContent(elementID),
					SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
			infoText.setEditable(false);
			GridData gr = new GridData(SWT.FILL, SWT.FILL, true, false);
			gr.heightHint = 5 * infoText.getLineHeight();
			infoText.setLayoutData(gr);
			toolkit.createLabel(this, ""); // NEWLINE

			// Edit Button
			Button button = toolkit.createButton(this, "Edit Data", SWT.PUSH);
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					// TODO: This needs to be chosen among an xml/owl list
					String a = JOptionPane.showInputDialog(null, "GDPR", "PlaceholderContent",
							JOptionPane.QUESTION_MESSAGE);
					String b = JOptionPane.showInputDialog(null, "GDPR", "PlaceholderData",
							JOptionPane.QUESTION_MESSAGE);

					//Insert edited data and refresh infotext
					XMLTagParser.editElement(elementID, a, b);
					infoText.setText(XMLTagParser.getElementTagsAndContent(elementID));
				}
			});
		}
	}
}
