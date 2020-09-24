package it.unisalento.eclipse.bpmn2.gdpr.util.bpmn;

public enum TaskType {
	BASE, // Non GDPR, basic BPMN task
	SEND, // Message Send Task
	RECEIVE, // Message Receive Task
	USER, // User Task -- IE Login
	SERVICE, // Service Task, handled by System -- IE Data Processing
	UNKNOWN // Unknown, unrecognized task
}
