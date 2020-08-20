package it.uni.eclipse.bpmn2.gdpr.commands;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import it.uni.eclipse.bpmn2.gdpr.util.PIAExporter;
import it.uni.eclipse.bpmn2.gdpr.util.bpmn.BPMNAnalyzer;

//TODO: Maybe put all "Data Protection BPMN Modeler" inside a single title const String 
public class ExportPIA extends AbstractHandler {

	/**
	 * Export all data in json format for PIA software import.
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO: Document this function's steps
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);

		String exportName = JOptionPane.showInputDialog(null, "Choose the exported file name:",
				"Data Protection BPMN Modeler", JOptionPane.QUESTION_MESSAGE);

		if (exportName == null || exportName == "" || exportName == " ")
			return null;
		if (!exportName.endsWith(".json"))
			exportName += ".json";

		String exportDir = chooseDestDir();
		if (exportDir == null || exportDir == "")
			return null;

		// TODO: Add overwrite question
		File export = new File(exportDir + "\\" + exportName);
		if (!export.isFile()) {
			try {
				export.createNewFile();
			} catch (IOException e) {
				if (e.getMessage().contains("Access"))
					MessageDialog.openError(window.getShell(), "Data Protection BPMN Modeler",
							"Access Denied: you cannot save in this directory without administrator priviledges.");

				e.printStackTrace();
				return null;
			}
		}

		// Get the name of the file author, for PIA purpose
		boolean validName = false;
		String authorName = "";
		while (!validName) {
			authorName = JOptionPane.showInputDialog(null,
					"Insert the name of the author as <Surname>, <Name>:\n(e.g. Doe, John)",
					"Data Protection BPMN Modeler", JOptionPane.QUESTION_MESSAGE);

			if (authorName == null || authorName == "" || authorName == " " || !authorName.contains(", "))
				validName = false;
			else
				validName = true;
		}

		// Step
		JFileChooser fc = new JFileChooser(new File(ResourcesPlugin.getWorkspace().getRoot().getLocation().toString()));
		fc.setDialogTitle("Select the BPMN Diagram to export");
		int option = fc.showDialog(null, "Select");
		URI uri = URI.createFileURI(fc.getSelectedFile().toString());

		if (option == JFileChooser.APPROVE_OPTION) {
			BPMNAnalyzer analyzer = new BPMNAnalyzer(uri);

			PIAExporter exporter = new PIAExporter(export, authorName, analyzer);
			exporter.export();

			MessageDialog.openInformation(window.getShell(), "Data Protection BPMN Modeler",
					"File exported successfully");
		}

		return null;
	}

	/**
	 * Get the destination directory for the exported file
	 */
	private String chooseDestDir() {
		JFileChooser chooser = new JFileChooser("~");
		chooser.setDialogTitle("BPMN Modeler");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);

		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile().getAbsolutePath();
		} else {
			return "";
		}
	}
}
