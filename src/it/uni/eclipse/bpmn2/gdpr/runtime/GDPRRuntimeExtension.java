package it.uni.eclipse.bpmn2.gdpr.runtime;

import org.eclipse.bpmn2.modeler.core.utils.ModelUtil.Bpmn2DiagramType;
import org.eclipse.bpmn2.modeler.ui.AbstractBpmn2RuntimeExtension;
import org.eclipse.bpmn2.modeler.ui.wizards.FileService;
import org.eclipse.ui.IEditorInput;
import org.xml.sax.InputSource;

import it.uni.eclipse.bpmn2.gdpr.util.XMLTagParser;
import it.uni.eclipse.bpmn2.gdpr.util.owl.OntologyReader;

public class GDPRRuntimeExtension extends AbstractBpmn2RuntimeExtension {

	public static final String RUNTIME_ID = "it.uni.eclipse.bpmn2.gdpr.runtime1";

	public static final String targetNamespace = "http://it.uni.eclipse.bpmn2.gdpr";

	public GDPRRuntimeExtension() {
		//Plugin startup
		XMLTagParser.init();
		OntologyReader.setupOntology("C:\\Users\\Daniele-PC\\Desktop\\Tesiv2\\ontology\\gdpr.owl");
		//TODO: Non static path
	}

	@Override
	public String getTargetNamespace(Bpmn2DiagramType diagramType) {
		return targetNamespace;
	}

	/**
	 * IMPORTANT: The plugin is responsible for inspecting the file contents! Unless
	 * you are absolutely sure that the file is targeted for this runtime (by, e.g.
	 * looking at the targetNamespace or some other feature) then this method must
	 * return FALSE.
	 */
	@Override
	public boolean isContentForRuntime(IEditorInput input) {
		InputSource source = new InputSource(FileService.getInputContents(input));
		RootElementParser parser = new RootElementParser("http://it.uni.eclipse.bpmn2.gdpr");
		parser.parse(source);
		return parser.getResult();
	}
}
