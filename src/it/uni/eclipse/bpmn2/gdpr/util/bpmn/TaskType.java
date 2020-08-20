package it.uni.eclipse.bpmn2.gdpr.util.bpmn;

public enum TaskType {
	BASE, // Non GDPR, basic BPMN task
	SEND, // TODO: What is this?
	RECEIVE, // TODO: What is this?
	USER, // User Task -- IE Login
	SERVICE, // Service Task, handled by System -- IE Data Processing
	UNKNOWN // Unknown, unrecognized task
}
