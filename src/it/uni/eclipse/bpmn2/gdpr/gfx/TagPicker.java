package it.uni.eclipse.bpmn2.gdpr.gfx;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Utilities;

import it.uni.eclipse.bpmn2.gdpr.util.owl.OntologyReader;
import it.uni.eclipse.bpmn2.gdpr.util.owl.OwlEntity;
import it.uni.eclipse.bpmn2.gdpr.util.owl.OwlProperty;

public class TagPicker extends JFrame {
	private JPanel contentPane;

	/**
	 * Creates a new tag picker for the relevant GDPRTagEditor
	 */
	public TagPicker(GDPRTagEditor instance) {
		setTitle("New Tag Selection");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		
		/// Generated
		setBounds(300, 300, 287, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		JScrollPane scroll = new JScrollPane(textArea);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		contentPane.add(scroll, BorderLayout.CENTER);
		fillTextArea(textArea);

		JButton confirmBtn = new JButton("Confirm Selection");
		confirmBtn.setSize(100, 40);
		contentPane.add(confirmBtn, BorderLayout.SOUTH);
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

		// Add tag button
		confirmBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedTag = textArea.getSelectedText();

				if (!selectedTag.equals("") && selectedTag != null) {
					// Add entity
					OwlEntity entity = OntologyReader.getEntityByName(selectedTag);

					String content = JOptionPane.showInputDialog(null, "Insert the value of tag <" + selectedTag + ">:",
							"GDPR Plugin", JOptionPane.QUESTION_MESSAGE);

					if (!content.equals("") && content != null)
						instance.addTag(selectedTag, content);

					// Add all the relevant properties, specified in the ontology
					if (entity.getProperties().size() != 0) {
						for (OwlProperty p : entity.getProperties()) {
							String value = JOptionPane.showInputDialog(null,
									"Insert the value of property <" + p.getName() + ">:", "GDPR Plugin",
									JOptionPane.QUESTION_MESSAGE);

							if (!value.equals("") && value != null)
								instance.editProp(selectedTag, p.getName(), value);
						}
					}
				}
			}
		});

		setVisible(true);
	}

	/**
	 * Help function to parse correctly all the entities in the ontology
	 */
	private void fillTextArea(JTextArea textArea) {
		String completeText = "";
		for (String s : OntologyReader.getAllEntitiesName())
			completeText = completeText + s + "\n";

		textArea.setText(completeText);
	}
}
