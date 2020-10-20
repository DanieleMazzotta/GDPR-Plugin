package it.unisalento.eclipse.bpmn2.gdpr.runtime;

import java.io.File;

import org.eclipse.bpmn2.modeler.core.utils.ModelUtil.Bpmn2DiagramType;
import org.eclipse.bpmn2.modeler.ui.AbstractBpmn2RuntimeExtension;
import org.eclipse.bpmn2.modeler.ui.wizards.FileService;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.IEditorInput;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.xml.sax.InputSource;

import it.unisalento.eclipse.bpmn2.gdpr.util.XMLTagParser;
import it.unisalento.eclipse.bpmn2.gdpr.util.owl.OntologyReader;

public class GDPRRuntimeExtension extends AbstractBpmn2RuntimeExtension {
	public static final String RUNTIME_ID = "it.unisalento.eclipse.bpmn2.gdpr.runtime1";
	public static final String targetNamespace = "http://it.unisalento.eclipse.bpmn2.gdpr";

	public static boolean hasOntologySelected = false;

	public GDPRRuntimeExtension() {
		// Plugin startup
		XMLTagParser.init(XMLTagParser.defaultFile);

		// Use custom file if already provided by user
		Bundle bundle = FrameworkUtil.getBundle(getClass());
		IPath stateLoc = Platform.getStateLocation(bundle);
		File customOntology = new File(stateLoc.toString() + "/ontology.owl");
		if (customOntology.isFile()) {
			hasOntologySelected = true;
			OntologyReader.setupOntology(customOntology.getAbsolutePath());
		}
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
		RootElementParser parser = new RootElementParser("http://it.unisalento.eclipse.bpmn2.gdpr");
		parser.parse(source);
		return parser.getResult();
	}
}
