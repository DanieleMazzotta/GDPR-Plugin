package it.uni.eclipse.bpmn2.gdpr.commands;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public class ExportPIA extends AbstractHandler {

	/**
	 * Export all data in json format for PIA software import.
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
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

		File export = new File(exportDir + "\\" + exportName);
		if (!export.isFile()) {
			try {
				export.createNewFile();
			} catch (IOException e) {
				if (e.getMessage().contains("Access"))
					MessageDialog.openError(window.getShell(), "Data Protection BPMN Modeler",
							"Access Denied: you cannot save in this directory without administrato priviledges.");

				e.printStackTrace();
				return null;
			}
		}

		MessageDialog.openInformation(window.getShell(), "Data Protection BPMN Modeler",
				"The file will be saved in " + exportDir.replace("\\", "/") + "/" + exportName);

		//TODO: Import form IntelliJ the json PIA logic, and work on a translation from the bpmn diagram
		//	Put the class in the /util package and just call the export() method from here
		
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
