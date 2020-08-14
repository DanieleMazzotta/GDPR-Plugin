package it.uni.eclipse.bpmn2.gdpr.exportFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.LaneSet;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.modeler.core.runtime.TargetRuntime;
import org.eclipse.bpmn2.util.Bpmn2ResourceFactoryImpl;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.jface.dialogs.MessageDialog;

//TODO: Read this class
public class DiagramAnalyzer {

	public static void export(URI uri, String fileName) throws IOException {
		Bpmn2ResourceFactoryImpl resFactory = new Bpmn2ResourceFactoryImpl();

		Resource resource = resFactory.createResource(uri);

		// We need this option because all object references in the file are "by ID"
		// instead of the document reference "URI#fragment" form.
		HashMap<Object, Object> options = new HashMap<Object, Object>();
		options.put(XMLResource.OPTION_DEFER_IDREF_RESOLUTION, true);

		// Load the resource
		resource.load(options);

		// This is the root element of the XML document
		Definitions d = getDefinitions(resource);

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("GDPR Sheet");

		List<RootElement> rootElements = d.getRootElements();
		for (RootElement rel : rootElements) {
			if (rel instanceof org.eclipse.bpmn2.Process) {
				org.eclipse.bpmn2.Process process = (org.eclipse.bpmn2.Process) rel;
				int rowCount = 0;
				for (FlowElement fe : process.getFlowElements()) {
					List<Lane> allLanes = new ArrayList<Lane>();
					allLanes = getAllLaneSet(process.getLaneSets(), allLanes);
					if (checkInstance(fe)) {
						List<String> allAttributes = new ArrayList<String>();
						TargetRuntime rt = TargetRuntime.getRuntime("it.uni.eclipse.bpmn2.gdpr.runtime1");
						allAttributes = AttributesNoBooleanValue(rt, allAttributes, fe);
						boolean PersonalData = findIfPersonalData(rt, fe);

						String TypeNotNull = findPersonalDataType(rt, fe);

						List<String> AttributesNames = new ArrayList<String>();
						AttributesNames = AttributesNoBoolean(rt, AttributesNames, fe);

						Object[][] GDPR = { { "Actor", "Activity", AttributesNames, "Legal basis", "Duration" }, };

						if (PersonalData & !TypeNotNull.isEmpty() & TypeNotNull != null) {
							for (Object[] Data : GDPR) {
								Row row = sheet.createRow(++rowCount);

								int columnCount = 0;

								for (Object field : Data) {

									Cell cell = row.createCell(++columnCount);
									CellStyle cs = cell.getSheet().getWorkbook().createCellStyle();
									Font font = cell.getSheet().getWorkbook().createFont();
									font.setColor(IndexedColors.WHITE.index);
									cs.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.index);
									cs.setFillPattern(FillPatternType.SOLID_FOREGROUND);
									cs.setFont(font);
									cs.setAlignment(HorizontalAlignment.CENTER);
									if (field.equals(AttributesNames)) {
										for (int i = 0; i < AttributesNames.size(); i++) {
											cell.setCellStyle(cs);
											cell.setCellValue(AttributesNames.get(i));
											cell.getSheet().autoSizeColumn(columnCount);
											columnCount++;
											cell = row.createCell(columnCount);
										}
										columnCount--;
									} else {
										cell.setCellStyle(cs);
										cell.setCellValue((String) field);
										cell.getSheet().autoSizeColumn(columnCount);
									}
								}

							}

							String Actor = getActor(fe).getName();
							String Activity = fe.getName();

							List<Lane> Parents = new ArrayList<Lane>();
							List<Lane> NewParents = new ArrayList<Lane>();

							Parents.add(getActor(fe));
							Parents = getParents(allLanes, getActor(fe), Parents);
							NewParents = getOnlyGDPRLanes(Parents, NewParents);
							List<String> Legal = new ArrayList<String>();
							for (int i = 0; i < NewParents.size(); i++) {
								Legal.add(NewParents.get(i).getAnyAttribute().get(2).getValue().toString());
							}
							List<String> Duration = new ArrayList<String>();
							for (int i = 0; i < NewParents.size(); i++) {
								Duration.add(NewParents.get(i).getAnyAttribute().get(1).getValue().toString());
							}
							Object[][] ColumnValue = { { Actor, Activity, allAttributes, Legal, Duration }, };
							for (Object[] Data : ColumnValue) {
								Row row = sheet.createRow(++rowCount);
								int columnCount = 0;
								for (Object value : Data) {
									Cell cell = row.createCell(++columnCount);
									if (value.equals(Legal)) {
										for (int i = 0; i < Legal.size(); i++) {
											cell.setCellValue(Legal.get(i));
											row = sheet.createRow(++rowCount);
											cell = row.createCell(columnCount);
											cell.getSheet().autoSizeColumn(columnCount);

										}
									} else if (value.equals(Duration)) {
										rowCount = rowCount - Legal.size();
										row = sheet.getRow(rowCount);
										for (int i = 0; i < Duration.size(); i++) {
											cell = row.createCell(columnCount);
											cell.setCellValue(Duration.get(i));
											cell.getSheet().autoSizeColumn(columnCount);

											row = sheet.getRow(++rowCount);

										}

									} else if (value.equals(allAttributes)) {

										for (int i = 0; i < allAttributes.size(); i++) {
											cell.setCellValue(allAttributes.get(i));
											cell.getSheet().autoSizeColumn(columnCount);
											columnCount++;
											cell = row.createCell(columnCount);

										}

										columnCount = columnCount - 1;
									} else {
										cell.setCellValue(value.toString());
										cell.getSheet().autoSizeColumn(columnCount);

									}
								}
								rowCount = rowCount - 1;
							}
						}
					}
				}
			}

			if (!checkEsist(fileName + ".xlsx")) {
				try (FileOutputStream outputStream = new FileOutputStream(fileName + ".xlsx")) {
					workbook.write(outputStream);
					workbook.close();
					MessageDialog.openInformation(null, "!", "File saved");

				}
			} else {
				OverwriteOption(fileName, workbook);
			}
		}
	}

	private static Definitions getDefinitions(Resource resource) {
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

	public static List<Lane> getAllLaneSet(List<LaneSet> lls, List<Lane> ll) {
		for (LaneSet l : lls) {
			for (Lane l2 : l.getLanes()) {
				ll.add(l2);
				getAllLanes(l2.getChildLaneSet(), ll);

			}

		}
		return ll;
	}

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

	public static boolean checkInstance(FlowElement f) {
		if (f instanceof org.eclipse.bpmn2.Task || f instanceof org.eclipse.bpmn2.ManualTask
				|| f instanceof org.eclipse.bpmn2.SendTask || f instanceof org.eclipse.bpmn2.ReceiveTask
				|| f instanceof org.eclipse.bpmn2.UserTask || f instanceof org.eclipse.bpmn2.ScriptTask
				|| f instanceof org.eclipse.bpmn2.BusinessRuleTask || f instanceof org.eclipse.bpmn2.ServiceTask) {
			return true;
		}
		return false;
	}

	public static boolean checkEsist(String Filename) {
		File file = new File(Filename);
		boolean exist = file.exists();
		if (exist) {
			return true;
		}
		return false;
	}

	public static void OverwriteOption(String fileName, XSSFWorkbook workbook) {
		int n = JOptionPane.showConfirmDialog(null, "A file with the same name already exist, overwrite?", "Overwrite",
				JOptionPane.YES_NO_OPTION);

		if (n == JOptionPane.YES_OPTION) {
			try (FileOutputStream outputStream = new FileOutputStream(fileName + ".xlsx")) {
				workbook.write(outputStream);
				workbook.close();
				MessageDialog.openInformation(null, "", "File overwritten");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			MessageDialog.openInformation(null, "", "File not saved");
		}
	}

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

	public static List<Lane> getOnlyGDPRLanes(List<Lane> Parents, List<Lane> NewParents) {
		for (Lane l : Parents) {
			if (l.getAnyAttribute().get(0).getValue().equals(Boolean.toString(true))) {
				NewParents.add(l);
			}

		}

		return NewParents;
	}

	public static boolean findIfPersonalData(TargetRuntime rt, FlowElement fe) {
		for (int i = 0; i < rt.getModelExtensionDescriptor(fe).getProperties().size(); i++) {
			if (fe.getAnyAttribute().get(i).toString().contains("IsPersonalData=true")) {
				return true;
			}
		}

		return false;
	}

	public static String findPersonalDataType(TargetRuntime rt, FlowElement fe) {
		for (int i = 0; i < fe.getAnyAttribute().size(); i++) {
			if (fe.getAnyAttribute().get(i).toString().contains("ext:PersonalDataType=")) {

				return fe.getAnyAttribute().get(i).getValue().toString();
			}
		}

		return null;
	}

	public static List<String> AttributesNoBoolean(TargetRuntime rt, List<String> Att, FlowElement fe) {
		FeatureMap list = fe.getAnyAttribute();
		for (int i = 0; i < list.size(); i++) {
			if (!rt.getModelExtensionDescriptor(fe).getProperties().get(i).name.equals("IsPersonalData")) {
				Att.add(rt.getModelExtensionDescriptor(fe).getProperties().get(i).label);
			}
		}

		return Att;
	}

	public static List<String> AttributesNoBooleanValue(TargetRuntime rt, List<String> Att, FlowElement fe) {
		FeatureMap list = fe.getAnyAttribute();
		for (int i = 0; i < list.size(); i++) {
			if (!rt.getModelExtensionDescriptor(fe).getProperties().get(i).name.equals("IsPersonalData")) {
				Att.add(fe.getAnyAttribute().get(i).getValue().toString());
			}
		}

		return Att;
	}
}
