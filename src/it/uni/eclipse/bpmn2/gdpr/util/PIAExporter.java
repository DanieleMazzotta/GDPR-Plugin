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

	private String stub = "--This section is a stub and was filled automatically by the GDPR Plugin for BPMN diagrams in Eclipse.--\n"
			+ "--Please fill this section manually.--";

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
		writeAnswer("111", "STUB? Description and purposes of the device");
		writeAnswer("112", "STUB? Who hosts the data/service? Who analizes the data?");
		writeAnswer("113",
				"STUB? Sector specific standards to which the processing adheres (code of conduct or similar)");

		writeAnswer("121", analyzer.getDataAcquired());
		writeAnswer("122", analyzer.getDiagramFlow());
		writeAnswer("123", analyzer.getDataStorageAndProcessing());

		writeAnswer("211", "Legittimita trattamento");
		writeAnswer("212", "Basi legali");
		writeAnswer("213", "Necessarieta dati");
		writeAnswer("214", "Qualita dati");
		writeAnswer("215", "Perche durata dati");

		writeAnswer("221", "Informativa utenti");
		writeAnswer("222", "Consenso utenti");
		writeAnswer("223", "Accesso utenti a dati");
		writeAnswer("224", "Diritto all oblio");
		writeAnswer("225", "Limitazione dati");
		writeAnswer("226", "Obblighi responsabili");
		writeLastAnswer("227", stub);
		write("\n],\n");
	}

	// TODO: This will depend on BPMN or idk
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
