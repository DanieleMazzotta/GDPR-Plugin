package it.unisalento.eclipse.bpmn2.gdpr.commands;

import java.io.File;

import javax.swing.JFileChooser;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import it.unisalento.eclipse.bpmn2.gdpr.util.bpmn.BPMNAnalyzer;

/**
 * Temporary class to quickly test some conversion stuff from BPMN to PIA
 */
public class DebugTranslate extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);

		JFileChooser fc = new JFileChooser(new File(ResourcesPlugin.getWorkspace().getRoot().getLocation().toString()));
		int option = fc.showDialog(null, "Select");
		URI uri = URI.createFileURI(fc.getSelectedFile().toString());

		if (option == JFileChooser.APPROVE_OPTION) {
			BPMNAnalyzer analyzer = new BPMNAnalyzer(uri);

			String show = analyzer.getDataProcessingReason();
			MessageDialog.openInformation(window.getShell(), "Debug", show);
		}

		return null;
	}
}
