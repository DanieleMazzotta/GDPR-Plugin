package it.uni.eclipse.bpmn2.gdpr.util;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;

//TODO: Comment this
public class ProjectUtils {

	public static String getCurrentProjectPath() {
		return getWorkspacePath() + "\\" + getCurrentProjectName() + "\\";
	}

	public static String getCurrentProjectName() {
		IProject workspace[] = ResourcesPlugin.getWorkspace().getRoot().getProjects();

		return workspace[0].getFullPath().toString().replace("/", "");
	}

	public static String getWorkspacePath() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		File workspaceDirectory = workspace.getRoot().getLocation().toFile();

		return workspaceDirectory.getAbsolutePath();
	}
}
