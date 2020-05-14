package it.uni.eclipse.bpmn2.gdpr.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JFileChooser;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import it.uni.eclipse.bpmn2.gdpr.runtime.GDPRRuntimeExtension;
import it.uni.eclipse.bpmn2.gdpr.util.owl.OntologyReader;

public class ChooseOntology extends AbstractHandler {

	/**
	 * Choose and validate .owl file to be used as ontology for the Data Protection
	 * BPMN Extension
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);

		File ontologyFile = getFileFromUser();

		if (ontologyFile == null) {
			MessageDialog.openError(window.getShell(), "Data Protection BPMN Modeler",
					"Please select a valid .owl ontology file.");
			return null;
		}

		if (ontologyFile.getName().endsWith(".owl")) {
			try {
				copyFileToMetadata(ontologyFile);
			} catch (IOException e) {
				e.printStackTrace();
			}

			GDPRRuntimeExtension.hasOntologySelected = true;
			OntologyReader.setupOntology(ontologyFile.getAbsolutePath());

			MessageDialog.openInformation(window.getShell(), "Data Protection BPMN Modeler",
					"The ontology in " + ontologyFile.getName() + " is now the active ontology.");
		} else {
			MessageDialog.openError(window.getShell(), "Data Protection BPMN Modeler",
					"Please select a valid .owl ontology file.");
		}
		
		return null;
	}

	private void copyFileToMetadata(File ontologyFile) throws IOException {
		// Get plugin metadata folder
		Bundle bundle = FrameworkUtil.getBundle(getClass());
		IPath stateLoc = Platform.getStateLocation(bundle);

		// Copy chosen file to plugin working folder
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(ontologyFile);
			os = new FileOutputStream(new File(stateLoc.toString() + "/ontology.owl"));
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
		} finally {
			is.close();
			os.close();
		}
	}

	/**
	 * Return the chosen file from a JFileChooser prompt window
	 */
	private File getFileFromUser() {
		JFileChooser fileChooser = new JFileChooser("~");

		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		int choose = fileChooser.showOpenDialog(null);
		if (choose == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		}

		return null;
	}
}
