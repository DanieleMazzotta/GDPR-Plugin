package it.uni.eclipse.bpmn2.gdpr.util.bpmn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
						List<String> allAttributes = new ArrayList<String>();
						TargetRuntime rt = TargetRuntime.getRuntime("it.uni.eclipse.bpmn2.gdpr.runtime1");
						allAttributes = DiagramAnalyzer.AttributesNoBooleanValue(rt, allAttributes, fe);

						// P4: Get attributes
						boolean PersonalData = DiagramAnalyzer.findIfPersonalData(rt, fe);
						List<String> AttributesNames = new ArrayList<String>();
						AttributesNames = DiagramAnalyzer.AttributesNoBoolean(rt, AttributesNames, fe);
						String Actor = DiagramAnalyzer.getActor(fe).getName();
						String Activity = fe.getName();
						String Comment = "";
						for (Documentation docum : fe.getDocumentation()) {
							Comment += docum.getText();
						}
						TaskType type = DiagramAnalyzer.getType(fe);

						// P5: Trascribe attributes and add to list
						BPMNElement elem = new BPMNElement(Actor, PersonalData, Activity, Comment, type);
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
	 * Returns a neatly formatted String for PIA in which we write what the workflow
	 * for the data is
	 */
	public String getDiagramFlow() {
		String format = "";
		
		format += "<ol>";
		for(BPMNElement elem : getAllElements()) {
			format += "<li><strong>" + elem.name + " [" + elem.type.toString() + " Task]:</strong> ";
			if(elem.comment == "")
				format += "No documentation provided for this element." + "</li>";
			else
				format += elem.comment + "</li>";
		}
		format += "</ol>";
		
		return format;
	}
}
