package it.uni.eclipse.bpmn2.gdpr;

import java.io.IOException;

import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.DefaultDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.DefaultPropertySection;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.xml.sax.SAXException;

import it.uni.eclipse.bpmn2.gdpr.util.DataTags;
import it.uni.eclipse.bpmn2.gdpr.util.ProjectUtils;
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
			setTitle("GDPR section");
			// TargetRuntime rt =
			// TargetRuntime.getRuntime("it.uni.eclipse.bpmn2.gdpr.runtime1");
			this.bindProperty(be, "IsPersonalData");

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
					// TODO: Add GDPR Tags & logic
					// TODO: We can retrieve the ElementID from the DataTags function, and store all
					// relevant data in a .xml
					// file inside the project, with all data associated to the ID, so it can be
					// (re)stored across
					// sessions
					String a = JOptionPane.showInputDialog(null, "GDPR", "PlaceholderData",
							JOptionPane.QUESTION_MESSAGE);
					t.setText(a);
					XMLTagParser.editElement(DataTags.getElementID(be), "GDPR", a);

					update();
					refresh();
				}
			});

			update();
			refresh();

			XMLTagParser.addNewElement(DataTags.getElementID(be));
		}
	}
}
