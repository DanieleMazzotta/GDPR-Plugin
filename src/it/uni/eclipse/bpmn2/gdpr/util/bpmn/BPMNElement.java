package it.uni.eclipse.bpmn2.gdpr.util.bpmn;

/**
 * Represents a flow element of the BPMN Diagram
 */
public class BPMNElement {
	public String lane;		// What lane the element belongs to
	public boolean isGDPR;	// True if the flag IsPersonalData was specified in the element
	public String name;		// The element name
	public String comment;	// The documentation associated to the element
	public TaskType type;	// The element type (Task, Service, etc)

	/**
	 * TODO: Document constructor parameters
	 * @param lane
	 * @param isGDPR
	 * @param name
	 * @param comment
	 * @param type
	 */
	public BPMNElement(String lane, boolean isGDPR, String name, String comment, TaskType type) {
		this.lane = lane;
		this.isGDPR = isGDPR;
		this.name = name;
		this.comment = comment;
		this.type = type;
	}
}
