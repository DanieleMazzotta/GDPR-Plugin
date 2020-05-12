package it.uni.eclipse.bpmn2.gdpr.extendModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

public class PropertyDescriptorActionListener implements ActionListener {

	private PropertyDescriptor pd;

	public PropertyDescriptor getPd() {
		return pd;
	}

	public void setPd(PropertyDescriptor pd) {
		this.pd = pd;
	}

	public PropertyDescriptorActionListener(PropertyDescriptor pd) {
		this.setPd(pd);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		File myObj = new File("plugin.xml");
		if (e.getSource() == pd.getBtnAdd()) {
			String name = pd.getVariableNameField().getText();
			String label = pd.getLabelField().getText();
			String description = pd.getDescriptionField().getText();

			try {
				boolean canAdd = true;
				Scanner myReader = new Scanner(myObj);
				while (myReader.hasNextLine()) {

					String data = myReader.nextLine();

					if (data.equals("           name=\"" + name + "\"")) {
						JOptionPane.showMessageDialog(null, "A property with the same name already exist", "Warning",
								JOptionPane.WARNING_MESSAGE);
						canAdd = false;
						myReader.close();

						break;
					} else if (name.contains(" ")) {
						JOptionPane.showMessageDialog(null, "The variable name cannot contain spaces", "Warning",
								JOptionPane.WARNING_MESSAGE);
						canAdd = false;
						myReader.close();

					} else if (ContainsSpecialCharactersNoSpace(name, canAdd)
							|| ContainsSpecialCharactersSpace(description, canAdd)
							|| ContainsSpecialCharactersSpace(label, canAdd)) {
						JOptionPane.showMessageDialog(null, "Special characters cannot be used", "Warning",
								JOptionPane.WARNING_MESSAGE);

						myReader.close();

					} else if (description.isEmpty() || name.isEmpty() || label.isEmpty()) {
						JOptionPane.showMessageDialog(null, "Please fill all the fields", "Warning",
								JOptionPane.WARNING_MESSAGE);

						myReader.close();
					}
				}
				myReader.close();
				if (canAdd == true) {
					Scanner SecondReader = new Scanner(myObj);
					while (SecondReader.hasNextLine()) {

						FileWriter writer = new FileWriter("plugin2.xml", true);
						String data = SecondReader.nextLine();
						writer.write(data);
						writer.write(System.getProperty("line.separator"));
						if (data.equals("           name=\"PersonalDataType\"")) {
							for (int i = 0; i < 3; i++) {

								data = SecondReader.nextLine();
								writer.write(data);
								writer.write(System.getProperty("line.separator"));

							}

							writer.write("     <property");
							writer.write(System.getProperty("line.separator"));
							writer.write("           name=\"" + name + "\"");
							writer.write(System.getProperty("line.separator"));
							writer.write("           description=\"" + description + "\"");
							writer.write(System.getProperty("line.separator"));
							writer.write("           label=\"" + label + "\"");
							writer.write(System.getProperty("line.separator"));
							writer.write("           type=\"EString\"");
							writer.write(System.getProperty("line.separator"));
							writer.write("           value=\"Set empty if not used\">");
							writer.write(System.getProperty("line.separator"));
							writer.write("     </property>");
							writer.write(System.getProperty("line.separator"));

						}
						writer.close();
					}
					SecondReader.close();
					myObj.delete();
					File f1 = new File("plugin.xml");
					File f2 = new File("plugin2.xml");
					f2.renameTo(f1);
					JOptionPane.showMessageDialog(null, "Property added", null, JOptionPane.INFORMATION_MESSAGE);

				}

			} catch (FileNotFoundException ex) {
				JOptionPane.showMessageDialog(null, "File does not exist", "Error", JOptionPane.ERROR_MESSAGE);
				ex.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		} else if (e.getSource() == pd.getBtnRem()) {
			String name = pd.getVariableNameField().getText();

			if (name.equals("IsPersonalData") || name.equals("PersonalDataType")) {
				JOptionPane.showMessageDialog(null, "Impossible to remove this property", "Error",
						JOptionPane.ERROR_MESSAGE);
			} else {
				try {
					boolean canRemove = false;
					Scanner myReader = new Scanner(myObj);
					while (myReader.hasNextLine()) {

						String line = myReader.nextLine();

						if (line.equals("           name=\"" + name + "\"")) {
							canRemove = true;
							myReader.close();

							break;
						}
					}
					myReader.close();

					if (canRemove == true) {

						Scanner RemoveScanner = new Scanner(myObj);
						while (RemoveScanner.hasNextLine()) {
							FileWriter writer = new FileWriter("plugin2.xml", true);
							String data = RemoveScanner.nextLine();
							if ((data.equals("     <property"))) {
								String nextLine = RemoveScanner.nextLine();
								if ((nextLine.equals("           name=\"" + name + "\""))) {
									for (int i = 0; i < 5; i++) {
										data = RemoveScanner.nextLine();
									}

								} else {
									writer.write(data);
									writer.write(System.getProperty("line.separator"));
									writer.write(nextLine);
									writer.write(System.getProperty("line.separator"));

								}
								writer.close();

							} else {
								writer.write(data);
								writer.write(System.getProperty("line.separator"));
								writer.close();
							}

						}
						RemoveScanner.close();
						myObj.delete();
						File f1 = new File("plugin.xml");
						File f2 = new File("plugin2.xml");
						f2.renameTo(f1);
						JOptionPane.showMessageDialog(null, "Property Removed", null, JOptionPane.INFORMATION_MESSAGE);
					}

				} catch (FileNotFoundException e1) {
					JOptionPane.showMessageDialog(null, "File does not exist", "Error", JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		}
	}

	public static boolean ContainsSpecialCharactersSpace(String string, boolean canAdd) {
		Pattern regex = Pattern.compile("^[a-zA-Z0-9 ]*$");
		if (regex.matcher(string).find()) {
			canAdd = false;
		}
		return canAdd;
	}

	public static boolean ContainsSpecialCharactersNoSpace(String string, boolean canAdd) {
		Pattern regex = Pattern.compile("^[a-zA-Z0-9]");
		if (regex.matcher(string).find()) {
			canAdd = false;
		}
		return canAdd;
	}

}
