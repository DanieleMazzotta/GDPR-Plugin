package it.uni.eclipse.bpmn2.gdpr.exportFile;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.util.URI;

public class Exportcommand implements IHandler {

	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		JFrame frame = null;
		JFileChooser fc = new JFileChooser(new File(ResourcesPlugin.getWorkspace().getRoot().getLocation().toString()));
		int option = fc.showDialog(frame, "Select");
		URI uri = URI.createFileURI(fc.getSelectedFile().toString());
		
		if (option == JFileChooser.APPROVE_OPTION) {
			JFileChooser fc2 = new JFileChooser();
			String fileName = getWithoutExtension(uri.toString());
			fc2.setSelectedFile(new File(fileName));
			FileNameExtensionFilter filter = new FileNameExtensionFilter("XLSX files", "xlsx");
			fc2.setFileFilter(filter);
			int newOption = fc2.showSaveDialog(null);
			if (newOption == JFileChooser.APPROVE_OPTION) {
				fileName = fc2.getSelectedFile().getAbsolutePath();
				try {
					DiagramAnalyzer.export(uri, fileName);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "GDPR", "Error in export", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
			}

		}
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isHandled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}

	public String getWithoutExtension(String fileFullPath) {
		return fileFullPath.substring(0, fileFullPath.lastIndexOf('.'));
	}
}
