package it.uni.eclipse.bpmn2.gdpr.util.bpmn;

/**
 * Represents a flow element of the BPMN Diagram
 */
public class BPMNElement {
	public String lane;
	public boolean isGDPR;
	public String name;
	public String comment;
	public TaskType type;

	/**
	 * Models a Task element from the BPMN Diagram
	 * 
	 * @param lane    What Lane the element belongs to
	 * @param isGDPR  If it is a GDPR Task. In the BPMN diagram, it has the
	 *                IsPersonalData flag set
	 * @param name    The task name
	 * @param comment The documentation associated to the element
	 * @param type    The task type (Task, Service, etc)
	 */
	public BPMNElement(String lane, boolean isGDPR, String name, String comment, TaskType type) {
		this.lane = lane;
		this.isGDPR = isGDPR;
		this.name = name;
		this.comment = comment;
		this.type = type;
	}
}
