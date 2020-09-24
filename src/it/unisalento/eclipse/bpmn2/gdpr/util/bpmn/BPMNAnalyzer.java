package it.unisalento.eclipse.bpmn2.gdpr.util.bpmn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JOptionPane;

import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.Documentation;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.modeler.core.runtime.TargetRuntime;
import org.eclipse.bpmn2.util.Bpmn2ResourceFactoryImpl;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.jface.dialogs.MessageDialog;

import it.unisalento.eclipse.bpmn2.gdpr.util.XMLTagParser;

public class BPMNAnalyzer {
	private ArrayList<BPMNElement> elements = new ArrayList<BPMNElement>();

	/**
	 * Given a BPMN Diagram file uri, splits it into its elements. We can then
	 * analyze it and export it for PIA.
	 */
	public BPMNAnalyzer(URI uri) {
		// P1: Load the resource
		Bpmn2ResourceFactoryImpl resFactory = new Bpmn2ResourceFactoryImpl();
		Resource resource = resFactory.createResource(uri);
		HashMap<Object, Object> options = new HashMap<Object, Object>();
		options.put(XMLResource.OPTION_DEFER_IDREF_RESOLUTION, true);
		try {
			resource.load(options);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// P2: Get the elements
		Definitions d = DiagramAnalyzer.getDefinitions(resource);
		List<RootElement> rootElements = d.getRootElements();
		for (RootElement rel : rootElements) {
			if (rel instanceof org.eclipse.bpmn2.Process) {
				org.eclipse.bpmn2.Process process = (org.eclipse.bpmn2.Process) rel;

				// P3: Analyze each element
				for (FlowElement fe : process.getFlowElements()) {
					List<Lane> allLanes = new ArrayList<Lane>();
					allLanes = DiagramAnalyzer.getAllLaneSet(process.getLaneSets(), allLanes);
					if (DiagramAnalyzer.checkInstance(fe)) {
						// P4: Get attributes
						TargetRuntime rt = TargetRuntime.getRuntime("it.unisalento.eclipse.bpmn2.gdpr.runtime1");

						boolean PersonalData = DiagramAnalyzer.findIfPersonalData(rt, fe);
						String Actor = DiagramAnalyzer.getActor(fe).getName();
						String Activity = fe.getName();
						String ID = fe.getId();
						String Comment = "";
						for (Documentation docum : fe.getDocumentation()) {
							Comment += docum.getText();
						}
						TaskType type = DiagramAnalyzer.getType(fe);

						// P5: Trascribe attributes and add to list
						BPMNElement elem = new BPMNElement(ID, Actor, PersonalData, Activity, Comment, type);
						elements.add(elem);
					}
				}
			}
		}
	}

	/**
	 * Returns all BPMN elements in the diagram
	 */
	public ArrayList<BPMNElement> getAllElements() {
		return elements;
	}

	/**
	 * Returns a neatly formatted String for PIA (SECTION 122) in which we write
	 * what the workflow for the data is
	 */
	public String getDiagramFlow() {
		String format = "";

		format += "<ol>";
		for (BPMNElement elem : getAllElements()) {
			format += "<li><strong>" + elem.name + " [" + elem.type.toString() + " Task]:</strong> ";
			if (elem.comment == "")
				format += "No documentation provided for this element." + "</li>";
			else
				format += elem.comment.replace("\n", "<br>").replace("\r", "").replace("\n", "") + "</li>";
		}
		format += "</ol>";

		return format;
	}

	/**
	 * Returns a neatly formatted String for PIA (SECTION 121) in which we write
	 * what data the BPMN process acquires
	 */
	public String getDataAcquired() {
		String format = "";

		format += "<ol>";
		for (BPMNElement elem : getAllElements()) {
			// Get GDPR Data from the XML file
			String xmlData = XMLTagParser.getElementTagsAndContent(elem.id);
			String lines[] = xmlData.split("\\n");

			for (int i = 0; i < lines.length; i++) {
				boolean hasPersonalData = false;
				String personalData = "", dataDuration = "", dataAccess = "";

				if (lines[i].startsWith("PersonalData:")) {
					hasPersonalData = true;
					personalData = lines[i].replace("PersonalData: ", "");
					dataAccess = lines[i + 1].replace("  >accessToData: ", "");
					dataDuration = lines[i + 2].replace("  >dataDuration: ", "");

					if (hasPersonalData) {
						format += "<li><strong>" + elem.name + ": </strong>" + personalData
								+ ".\nAccess to data is granted to: " + dataAccess + ".\nThe data will be stored for "
								+ dataDuration + ".</li>";
					}
				}
			}
		}
		format += "</ol>";

		return format;
	}

	/**
	 * Returns a neatly formatted String for PIA (SECTION 123) in which we write
	 * where the data is stored and processed
	 */
	public String getDataStorageAndProcessing() {
		String format = "";

		format += "<ul>";
		for (BPMNElement elem : getAllElements()) {
			// Get GDPR Data from the XML file
			String xmlData = XMLTagParser.getElementTagsAndContent(elem.id);
			String lines[] = xmlData.split("\\n");

			for (int i = 0; i < lines.length; i++) {
				if (lines[i].startsWith("Storage:")) {
					String storageLocation = lines[i].replace("Storage: ", "");
					String dataDuration = lines[i + 1].replace("  >dataDuration: ", "");

					format += "<li><strong>" + elem.name + "</strong>\nThe data is stored in: " + storageLocation
							+ " for " + dataDuration + ".</li>";
				}

				if (lines[i].startsWith("PersonalDataProcessing:"))
					format += "<li><strong>" + elem.name + "</strong>\nThe data is processed by: "
							+ lines[i].replace("PersonalDataProcessing: ", "") + "</li>";
			}
		}
		format += "</ul>";

		return format;
	}

	/**
	 * Returns a neatly formatted String for PIA (SECTION 212) in which we write the
	 * legal basis for the data processing steps
	 */
	public String getLegalBasisInformation() {
		String format = "";

		format += "<ul>";
		for (BPMNElement elem : getAllElements()) {
			// Get GDPR Data from the XML file
			String xmlData = XMLTagParser.getElementTagsAndContent(elem.id);
			String lines[] = xmlData.split("\\n");

			for (int i = 0; i < lines.length; i++) {
				if (lines[i].startsWith("LegalBasis:"))
					format += "<li><strong>" + elem.name
							+ "</strong>\nThe legal basis for the data processing is specified as: "
							+ lines[i].replace("LegalBasis: ", "") + "</li>";
			}
		}
		format += "</ul>";

		return format;
	}

	/**
	 * Returns a neatly formatted String for PIA (SECTION 211) in which we write the
	 * reasons for the data processing
	 */
	public String getDataProcessingReason() {
		String format = "";

		format += "<ul>";
		for (BPMNElement elem : getAllElements()) {
			// Get GDPR Data from the XML file
			String xmlData = XMLTagParser.getElementTagsAndContent(elem.id);
			String lines[] = xmlData.split("\\n");

			for (int i = 0; i < lines.length; i++) {
				if (lines[i].startsWith("  >processingReason: "))
					format += "<li><strong>" + elem.name
							+ "</strong>\nThe reason for the data processing is specified as: "
							+ lines[i].replace("  >processingReason: ", "") + "</li>";
			}
		}
		format += "</ul>";

		return format;
	}

	/**
	 * Returns a neatly formatted String for PIA (SECTION 227) in which we write the
	 * details of the Data Transfers
	 */
	public String getDataTransfers() {
		String format = "";

		format += "<ul>";
		for (BPMNElement elem : getAllElements()) {
			// Get GDPR Data from the XML file
			String xmlData = XMLTagParser.getElementTagsAndContent(elem.id);
			String lines[] = xmlData.split("\\n");

			for (int i = 0; i < lines.length; i++) {
				boolean hasDataTransfer = false;
				String dataTransferred = "", transferDestination = "", transferReason = "", safetyMeasures = "";

				if (lines[i].startsWith("DataTransfer:")) {
					hasDataTransfer = true;
					dataTransferred = lines[i].replace("DataTransfer: ", "");
					safetyMeasures = lines[i + 1].replace("  >safetyMeasures: ", "");
					transferDestination = lines[i + 2].replace("  >transferDestination: ", "");
					transferReason = lines[i + 3].replace("  >transferReason: ", "");

					if (hasDataTransfer) {
						format += "<li><strong>" + elem.name + "</strong>" + ": the data transferred is "
								+ dataTransferred + ".\nThe data is transferred to '" + transferDestination
								+ "' for the reason '" + transferReason
								+ "'. Measures taken to mitigate the transfer risks are '" + safetyMeasures + "'.</li>";
					}
				}
			}
		}
		format += "</ol>";

		return format;
	}
}
