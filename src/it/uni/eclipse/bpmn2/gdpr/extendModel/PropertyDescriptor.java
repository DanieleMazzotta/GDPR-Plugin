package it.uni.eclipse.bpmn2.gdpr.extendModel;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;

import javax.swing.JButton;

public class PropertyDescriptor {
	private JFrame frame;
	private JTextField VariableNameField;
	private JTextField LabelField;
	private JTextField DescriptionField;
	private JButton btnAdd;
	private JButton btnRem;
	private PropertyDescriptorActionListener pdal;

	public PropertyDescriptor() {
		initialize();
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	public JTextField getVariableNameField() {
		return VariableNameField;
	}

	public void setVariableNameField(JTextField variableNameField) {
		VariableNameField = variableNameField;
	}

	public JTextField getLabelField() {
		return LabelField;
	}

	public void setLabelField(JTextField labelField) {
		LabelField = labelField;
	}

	public JTextField getDescriptionField() {
		return DescriptionField;
	}

	public void setDescriptionField(JTextField descriptionField) {
		DescriptionField = descriptionField;
	}

	public JButton getBtnAdd() {
		return btnAdd;
	}

	public void setBtnAdd(JButton btnAdd) {
		this.btnAdd = btnAdd;
	}

	public JButton getBtnRem() {
		return btnRem;
	}

	public void setBtnRem(JButton btnRem) {
		this.btnRem = btnRem;
	}

	public PropertyDescriptorActionListener getPdal() {
		return pdal;
	}

	public void setPdal(PropertyDescriptorActionListener pdal) {
		this.pdal = pdal;
	}

	private void initialize() {
		pdal = new PropertyDescriptorActionListener(this);
		frame = new JFrame();
		frame.setBounds(100, 100, 500, 447);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblName = new JLabel("Variable Name:");
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblName.setBounds(28, 196, 120, 26);
		frame.getContentPane().add(lblName);

		JLabel lblObjectToExtend = new JLabel("Add or remove properties.The properties are added as strings\r\n");
		lblObjectToExtend.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblObjectToExtend.setBounds(12, 13, 427, 32);
		frame.getContentPane().add(lblObjectToExtend);

		JLabel lblLabel = new JLabel("Label:");
		lblLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblLabel.setBounds(27, 235, 56, 16);
		frame.getContentPane().add(lblLabel);

		JLabel lblDescription = new JLabel("Description:");
		lblDescription.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblDescription.setBounds(28, 269, 75, 16);
		frame.getContentPane().add(lblDescription);

		VariableNameField = new JTextField();
		VariableNameField.setBounds(160, 199, 290, 22);
		frame.getContentPane().add(VariableNameField);
		VariableNameField.setColumns(10);

		LabelField = new JTextField();
		LabelField.setBounds(160, 233, 289, 22);
		frame.getContentPane().add(LabelField);
		LabelField.setColumns(10);

		DescriptionField = new JTextField();
		DescriptionField.setBounds(160, 269, 289, 22);
		frame.getContentPane().add(DescriptionField);
		DescriptionField.setColumns(10);

		btnAdd = new JButton("Add");
		btnAdd.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnAdd.setBounds(87, 341, 97, 25);

		frame.getContentPane().add(btnAdd);

		btnRem = new JButton("Remove");
		btnRem.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnRem.setBounds(295, 341, 97, 25);
		frame.getContentPane().add(btnRem);

		JLabel lblAndTheyMust = new JLabel("and they must be manually enabled in the workspace");
		lblAndTheyMust.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblAndTheyMust.setBounds(12, 38, 407, 16);
		frame.getContentPane().add(lblAndTheyMust);

		JLabel lblThroughWindowpreferencesbpmn = new JLabel("through Window->Preferences->BPMN2->Editor->Tool Profile");
		lblThroughWindowpreferencesbpmn.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblThroughWindowpreferencesbpmn.setBounds(12, 58, 407, 16);
		frame.getContentPane().add(lblThroughWindowpreferencesbpmn);

		JLabel lblIt = new JLabel("For correct data export it is recommended to enable the new ");
		lblIt.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblIt.setBounds(12, 102, 455, 16);
		frame.getContentPane().add(lblIt);

		JLabel lblNewLabel = new JLabel("properties for all types of activities");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel.setBounds(12, 120, 339, 16);
		frame.getContentPane().add(lblNewLabel);
		btnAdd.addActionListener(pdal);
		btnRem.addActionListener(pdal);
	}
}
