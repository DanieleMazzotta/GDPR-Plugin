package it.unisalento.eclipse.bpmn2.gdpr.util.bpmn;

import java.util.List;

import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.LaneSet;
import org.eclipse.bpmn2.modeler.core.runtime.TargetRuntime;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * This class contains utility methods for BPMN diagram analysis
 */
public class DiagramAnalyzer {
	/**
	 * Retrieves all BPMN definitions for the diagram
	 */
	public static Definitions getDefinitions(Resource resource) {
		if (resource != null && !resource.getContents().isEmpty()
				&& !resource.getContents().get(0).eContents().isEmpty()) {
			// Search for a Definitions object in this Resource
			for (EObject e : resource.getContents()) {
				for (Object o : e.eContents()) {
					if (o instanceof Definitions)
						return (Definitions) o;
				}
			}
		}
		return null;
	}

	/**
	 * Returns all Lane Sets
	 */
	public static List<Lane> getAllLaneSet(List<LaneSet> lls, List<Lane> ll) {
		for (LaneSet l : lls) {
			for (Lane l2 : l.getLanes()) {
				ll.add(l2);
				getAllLanes(l2.getChildLaneSet(), ll);
			}
		}

		return ll;
	}

	/**
	 * Returns all Lanes
	 */
	public static List<Lane> getAllLanes(LaneSet ls, List<Lane> ll) {
		if (ls != null) {
			List<Lane> lanes = ls.getLanes();
			for (Lane l : lanes) {
				ll.add(l);
				getAllLanes(l.getChildLaneSet(), ll);
			}
		}

		return ll;
	}

	/**
	 * Given a FlowElement, checks if it is a Task
	 */
	public static boolean checkInstance(FlowElement f) {
		if (f instanceof org.eclipse.bpmn2.Task || f instanceof org.eclipse.bpmn2.ManualTask
				|| f instanceof org.eclipse.bpmn2.SendTask || f instanceof org.eclipse.bpmn2.ReceiveTask
				|| f instanceof org.eclipse.bpmn2.UserTask || f instanceof org.eclipse.bpmn2.ScriptTask
				|| f instanceof org.eclipse.bpmn2.BusinessRuleTask || f instanceof org.eclipse.bpmn2.ServiceTask) {
			return true;
		}

		return false;
	}

	/**
	 * Returns the Lane to which the FlowElement belongs
	 */
	public static Lane getActor(FlowElement f) {
		Lane Actor = null;

		if (f instanceof org.eclipse.bpmn2.Task)
			Actor = ((org.eclipse.bpmn2.Task) f).getLanes().get(0);

		else if (f instanceof org.eclipse.bpmn2.ManualTask)
			Actor = ((org.eclipse.bpmn2.ManualTask) f).getLanes().get(0);

		else if (f instanceof org.eclipse.bpmn2.SendTask)
			Actor = ((org.eclipse.bpmn2.SendTask) f).getLanes().get(0);

		else if (f instanceof org.eclipse.bpmn2.ReceiveTask)
			Actor = ((org.eclipse.bpmn2.ReceiveTask) f).getLanes().get(0);

		else if (f instanceof org.eclipse.bpmn2.ScriptTask)
			Actor = ((org.eclipse.bpmn2.ScriptTask) f).getLanes().get(0);

		else if (f instanceof org.eclipse.bpmn2.UserTask)
			Actor = ((org.eclipse.bpmn2.UserTask) f).getLanes().get(0);

		else if (f instanceof org.eclipse.bpmn2.BusinessRuleTask)
			Actor = ((org.eclipse.bpmn2.BusinessRuleTask) f).getLanes().get(0);

		else if (f instanceof org.eclipse.bpmn2.ServiceTask)
			Actor = ((org.eclipse.bpmn2.ServiceTask) f).getLanes().get(0);

		return Actor;

	}

	/**
	 * Returns the parent Lanes to a Child
	 */
	public static List<Lane> getParents(List<Lane> allLanes, Lane Child, List<Lane> Parents) {
		for (Lane l : allLanes) {
			if (l.getChildLaneSet() != null && l.getChildLaneSet().getLanes() != null) {

				List<Lane> childlanes = l.getChildLaneSet().getLanes();
				for (Lane child : childlanes) {
					if (child.getName().equals(Child.getName())) {
						Parents.add(l);
						return getParents(allLanes, l, Parents);
					}
				}
			}
		}

		return Parents;
	}

	/**
	 * Returns only the Lanes marked as GDPR
	 */
	public static List<Lane> getOnlyGDPRLanes(List<Lane> Parents, List<Lane> NewParents) {
		for (Lane l : Parents) {
			if (l.getAnyAttribute().get(0).getValue().equals(Boolean.toString(true))) {
				NewParents.add(l);
			}
		}

		return NewParents;
	}

	/**
	 * Returns true if the FlowElement is marked as IsPersonalData
	 */
	public static boolean findIfPersonalData(TargetRuntime rt, FlowElement fe) {
		for (int i = 0; i < rt.getModelExtensionDescriptor(fe).getProperties().size(); i++) {
			if (fe.getAnyAttribute().get(i).toString().contains("IsPersonalData=true")) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Given a FlowElement, returns its TaskType
	 */
	public static TaskType getType(FlowElement fe) {
		if (fe instanceof org.eclipse.bpmn2.SendTask)
			return TaskType.SEND;
		else if (fe instanceof org.eclipse.bpmn2.ReceiveTask)
			return TaskType.RECEIVE;
		else if (fe instanceof org.eclipse.bpmn2.UserTask)
			return TaskType.USER;
		else if (fe instanceof org.eclipse.bpmn2.ServiceTask)
			return TaskType.SERVICE;
		else if (fe instanceof org.eclipse.bpmn2.Task)
			return TaskType.BASE;

		return TaskType.UNKNOWN;
	}
}
