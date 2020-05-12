package it.uni.eclipse.bpmn2.gdpr.gfx;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.Utilities;

import it.uni.eclipse.bpmn2.gdpr.util.XMLTagParser;

public class GDPRTagEditor extends JFrame {
	private JPanel contentPane;
	private JTextArea textArea;

	private String taskID; // The task id from the BPMN diagram
	private GDPRTagEditor instance; // Currently opened instance

	/**
	 * Opens the tag editor for the task specified
	 */
	public GDPRTagEditor(String taskID) {
		this.taskID = taskID;
		this.instance = this;

		setTitle("Tag Editor");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 535, 300);
		organizeLayout();

		setVisible(true);
	}

	private void organizeLayout() {
		/// Generated
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 290, 10 };
		gbl_contentPane.rowHeights = new int[] { 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 1.0, 1.0 };
		gbl_contentPane.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 5, 0, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		contentPane.add(panel, gbc_panel);

		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setRows(13);
		textArea.setColumns(34);
		refreshTextArea();

		JScrollPane scroll = new JScrollPane(textArea);
		panel.add(scroll);

		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 1;
		gbc_panel_1.gridy = 0;
		contentPane.add(panel_1, gbc_panel_1);
		panel_1.setLayout(new GridLayout(7, 0, 0, 0));

		JButton addBtn = new JButton("Add");
		panel_1.add(addBtn);

		JLabel lblNewLabel = new JLabel("");
		panel_1.add(lblNewLabel);

		JButton removeBtn = new JButton("Remove");
		panel_1.add(removeBtn);

		JLabel lblNewLabel_1 = new JLabel("");
		panel_1.add(lblNewLabel_1);

		JButton editBtn = new JButton("Edit");
		panel_1.add(editBtn);
		///

		// Highlight entire row on user click
		textArea.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				int pos = textArea.getCaretPosition();
				try {
					int start = Utilities.getRowStart(textArea, pos);
					int end = Utilities.getRowEnd(textArea, pos);
					textArea.setSelectionStart(start);
					textArea.setSelectionEnd(end);
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		});

		// Add new Tag
		addBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Delegate to another class
				new TagPicker(instance);
			}
		});

		// Remove selected tag
		removeBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedTag = textArea.getSelectedText().split(":")[0];

				// Cannot delete properties, only tags
				if (selectedTag.trim().startsWith(">")) {
					JOptionPane.showMessageDialog(null, "You cannot delete single properties from tags.", "GDPR Plugin",
							JOptionPane.WARNING_MESSAGE);
				} else {
					int delete = JOptionPane.showConfirmDialog(null,
							"Are you sure you want to delete tag " + selectedTag + "?", "GDPR Plugin",
							JOptionPane.YES_NO_OPTION);

					if (delete == JOptionPane.YES_OPTION)
						XMLTagParser.deleteTagFromElement(taskID, selectedTag);

					refreshTextArea();
				}
			}
		});

		// Edit selected tag / property
		editBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedTag = textArea.getSelectedText().split(":")[0];

				// Editing properties is different from editing tags
				if (selectedTag.trim().startsWith(">")) {
					String selectedProperty = selectedTag.trim().replace(">", "").replace("\n", "");
					String tagName = findTagOfSelectedProperty();

					String newValue = JOptionPane.showInputDialog(null,
							"Insert the new value for property " + selectedProperty + ":", "GDPR Plugin",
							JOptionPane.QUESTION_MESSAGE);

					if (newValue != null && !newValue.equals(""))
						XMLTagParser.editProperty(taskID, tagName, selectedProperty, newValue);
				} else {
					String newValue = JOptionPane.showInputDialog(null,
							"Insert the new value for tag " + selectedTag + ":", "GDPR Plugin",
							JOptionPane.QUESTION_MESSAGE);

					if (newValue != null && !newValue.equals(""))
						XMLTagParser.editElement(taskID, selectedTag, newValue);
				}

				refreshTextArea();
			}
		});

		repaint();
	}

	/**
	 * Finds the parent tag name, starting from the property name+value
	 */
	private String findTagOfSelectedProperty() {
		int startingPoint = getLineAtCaret(textArea);

		int i = startingPoint - 1;
		while (getRowContent(i).startsWith(">")) {
			i--;
		}

		return getRowContent(i).split(":")[0];
	}

	/**
	 * Return the line number at the Caret position.
	 */
	public static int getLineAtCaret(JTextComponent component) {
		int caretPosition = component.getCaretPosition();
		Element root = component.getDocument().getDefaultRootElement();

		return root.getElementIndex(caretPosition);
	}

	/**
	 * Get the content of the textArea at the specified row number
	 */
	private String getRowContent(int rowN) {
		Element element = textArea.getDocument().getDefaultRootElement().getElement(rowN);
		int start = element.getStartOffset();
		int end = element.getEndOffset();

		try {
			return textArea.getDocument().getText(start, end - start).trim().replace("\n", "");
		} catch (BadLocationException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Refresh the tag list on GDPRTagEditor and on GDPRPropertySection
	 */
	private void refreshTextArea() {
		// TODO: Reflect changes on GDPRPropertySection
		textArea.setText(XMLTagParser.getElementTagsAndContent(taskID));
	}

	/**
	 * (External) add tag
	 */
	public void addTag(String tagName, String content) {
		XMLTagParser.editElement(taskID, tagName, content);
		refreshTextArea();
	}

	/**
	 * (External) edit property
	 */
	public void editProp(String tagName, String property, String content) {
		XMLTagParser.editProperty(taskID, tagName, property, content);
		refreshTextArea();
	}
}
