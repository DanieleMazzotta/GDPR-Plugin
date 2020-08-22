package it.unisalento.eclipse.bpmn2.gdpr.util;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;

public class ProjectUtils {
	public static String dialogTitle = "Data Protection BPMN Modeler";
	
	/**
	 * Returns the global system path for the current BPMN project
	 */
	public static String getCurrentProjectPath() {
		return getWorkspacePath() + "\\" + getCurrentProjectName() + "\\";
	}

	/**
	 * Returns the name of the current active project in Eclipse
	 */
	public static String getCurrentProjectName() {
		IProject workspace[] = ResourcesPlugin.getWorkspace().getRoot().getProjects();

		return workspace[0].getFullPath().toString().replace("/", "");
	}

	/**
	 * Returns the global system path for the Eclipse workspace
	 */
	public static String getWorkspacePath() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		File workspaceDirectory = workspace.getRoot().getLocation().toFile();

		return workspaceDirectory.getAbsolutePath();
	}
}
