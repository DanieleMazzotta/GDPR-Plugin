package it.uni.eclipse.bpmn2.gdpr.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

import it.uni.eclipse.bpmn2.gdpr.util.bpmn.BPMNAnalyzer;

/**
 * Export in JSON format for PIA the data in the current BPMN diagram
 */
public class PIAExporter {
	private File file; // The file to export
	private int id; // Incremental id for entries, used by PIA, reverse engineered
	private String name; // The name of the author
	private String projectName; // The name of the PIA table when it will be imported
	private BPMNAnalyzer analyzer;

	private String stub = "<strong>--This section is a stub and was filled automatically by the GDPR Plugin for BPMN diagrams in Eclipse.--\n"
			+ "--Please fill this section manually.--</strong>";

	/**
	 * Initialize with following parameters: <br>
	 * jsonFile: the file path to export the data to; <br>
	 * writerName: the name of the author of the diagram.
	 */
	public PIAExporter(File jsonFile, String writerName, BPMNAnalyzer analyzer) {
		file = jsonFile;
		id = 58; // Start from here, incremental... reverse engineered number
		name = writerName;
		projectName = ProjectUtils.getCurrentProjectName();
		this.analyzer = analyzer;
	}

	/**
	 * Export the data to the json file specified
	 */
	public void export() {
		makeHeader();
		makeAnswers();
		makeMeasures();

		// Trailer data
		write("\"evaluations\":[],\n");
		write("\"comments\":[]}");
	}

	private void makeHeader() {
		write("{\n\"pia\":{");
		writeTag("dbVersion", "201809012140");
		writeTag("tableName", "\"pia\"");
		writeTag("serverUrl", "null");
		writeTag("status", "0");
		writeTag("is_example", "0");
		writeTag("created_at", "\"" + getPIATime() + "\"");
		writeTag("id", "10");
		writeTag("objectStore", "{}");
		writeTag("name", "\"" + projectName + "\"");
		writeTag("author_name", "\"" + name + "\"");
		writeTag("structure_id", "\"\"");
		writeLastTag("structure_data", "\"\"");
		write("},");
	}

	// TODO: Which field can we fill from here? Which ones will have to be filled by
	// PIA?
	private void makeAnswers() {
		// Answers header
		write("\"answers\":[{");
		writeTag("pia_id", "10");
		writeTag("created_at", "\"" + getPIATime() + "\"");
		writeLastTag("id", String.valueOf(id));
		write("},");

		// Answers
		writeAnswer("111", stub + "\n\nThis section must contain an outline of the product/process and its purposes.");
		writeAnswer("112", stub
				+ "\n\nThis section must contain the responsibilities of the manufacturer and the subcontractors.");
		writeAnswer("113", stub
				+ "\n\nThis section must list the sector-specific standars to which the processing adheres (code of conducts, certifications, ...)");

		writeAnswer("121", analyzer.getDataAcquired());
		writeAnswer("122", analyzer.getDiagramFlow());
		writeAnswer("123", analyzer.getDataStorageAndProcessing());

		writeAnswer("211", analyzer.getDataProcessingReason());
		writeAnswer("212", analyzer.getLegalBasisInformation());
		writeAnswer("213", stub
				+ "\n\nThis section must contain the steps taken in order to minimize the sensitivity of the data: "
				+ "IE Anonymization, Replacing DoB with age group, ...");
		writeAnswer("214", stub
				+ "\n\nThis section must contain the steps taken in order to ensure the quality and accuracy of the data.");
		writeAnswer("215", stub
				+ "\n\nThis section must contain, for each data type, its duration, the justification for the duration "
				+ "and the purging mechanism at the end of the storage period.");

		writeAnswer("221", stub
				+ "\n\nThis section must contain information on how the users are made aware of the data collection and processing, "
				+ "of the Terms and Conditions, of their rights to consent withdrawal and data erasure.\n"
				+ "Moreover, if the data is shared to third parties, the user must be notified of the reason of the sharing "
				+ "and the identity of the third parties with a detailed list of what data types are sent to each recipient.");
		writeAnswer("222", stub
				+ "\n\nThis section must contain information on how the users have given their informed consent to the data "
				+ "collection and processing, such as acceptance of User EULA.\n"
				+ "NB: Under GDPR, the burden of proof for the minor's parents acceptance of the product terms lies with the "
				+ "data controller and processor.");
		writeAnswer("223", stub
				+ "\n\nThis section must contain information on how the users can retrieve, consult, and download their data securely.");
		writeAnswer("224", stub
				+ "\n\nThis section must contain information on how the users can rectify or erase their data.\n"
				+ "All data that is not purged must be specified.\n The right to be forgotten for minors must be necessarily implemented.\n"
				+ "The Data Controller has ONE MONTH to erase the data.");
		writeAnswer("225", stub
				+ "\n\nThis section must contain the controls intended to ensure the right to object and data restriction for the users.\n"
				+ "There needs to be a 'Settings' interface easily accessible, some forms of parental control, the possibility of "
				+ "deactivating some of the device/product features.");
		writeAnswer("226", stub
				+ "\n\nAccording to Art.28 of the GDPR, a contract must be stipulated with each processor.\nSpecify, in detail, "
				+ "for each contract:\nThe processor's name\nThe purpose\nThe scope\nThe contract reference\nThe compliance with Art.28");
		// TODO: ^ Make a <ul> of the contract items for better visualization in PIA
		writeLastAnswer("227", stub
				+ "\n\nThis section must detail all data relevant to data transfers outside of the European Union. "
				+ "Need to specify the geographical storage of the device and data, and all the justifications and measures taken "
				+ "in order to ensure adequate protection to the data in the case of a cross-border transfer.");
		write("\n],\n");
	}

	// TODO: Look into this section
	private void makeMeasures() {
		write("\"measures\":[{");
		writeTag("title", "\"Cryptography\"");
		writeTag("pia_id", "10");
		writeTag("content", "\"Tipo crypt\"");
		writeTag("placeholder", "\"knowledge_base.slugs.PIA_TEC_CHIF.placeholder\"");
		writeTag("created_at", "\"" + getPIATime() + "\"");
		writeLastTag("id", "24");
		write("}],\n");
	}

	private void writeAnswer(String reference, String text) {
		writeLastAnswer(reference, text);
		write(",\n");
	}

	private void writeLastAnswer(String reference, String text) {
		id++;
		write("{");
		writeTag("pia_id", "10");
		writeTag("reference_to", reference);
		write("\"data\":{\n");
		writeTag("text", "\"" + piaText(text) + "\"");
		writeTag("gauge", "null");
		writeLastTag("list", "[]");
		write("},\n");
		writeTag("created_at", "\"" + getPIATime() + "\"");
		writeLastTag("id", String.valueOf(id));
		write("\n}");
	}

	private void writeTag(String tag, String value) {
		write("\"" + tag + "\":" + value + ",\n");
	}

	private void writeLastTag(String tag, String value) {
		write("\"" + tag + "\":" + value);
	}

	/**
	 * Append str to the json file
	 */
	private void write(String str) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(file, true));
			out.write(str);
			out.close();
		} catch (IOException e) {
			System.out.println("JSON Writing Error:\n" + e);
		}
	}

	/**
	 * Returns current time in the format used by the PIA software <br>
	 * IE 2020-06-14T14:57:12.621Z
	 */
	private String getPIATime() {
		// Part 1: Get the current time divided into its portions
		LocalDateTime now = LocalDateTime.now();
		int day = now.getDayOfMonth();
		int month = now.getMonthValue();
		int year = now.getYear();
		int hour = now.getHour();
		int minute = now.getMinute();
		int second = now.getSecond();

		// Part 2: Convert all data to strings, and append zeroes when less than 10
		// (i.e. 8 -> 08)
		String dayS, monthS, yearS, hourS, minuteS, secondS;

		dayS = day < 10 ? "0" + day : String.valueOf(day);
		monthS = month < 10 ? "0" + month : String.valueOf(month);
		yearS = String.valueOf(year);

		hourS = hour < 10 ? "0" + hour : String.valueOf(hour);
		minuteS = minute < 10 ? "0" + minute : String.valueOf(minute);
		secondS = second < 10 ? "0" + second : String.valueOf(second);

		// Part 3: Reconstruct the time string by putting it in the proper format
		return yearS + "-" + monthS + "-" + dayS + "T" + hourS + ":" + minuteS + ":" + secondS + ".000Z";
	}

	/**
	 * Format the text as PIA text (html tags)<br>
	 * IE \n --> <br>
	 */
	private String piaText(String text) {
		String translated = text;

		translated = translated.replace("\n", "<br>");
		// NOTE: Check if we need some more translations

		return translated;
	}
}
