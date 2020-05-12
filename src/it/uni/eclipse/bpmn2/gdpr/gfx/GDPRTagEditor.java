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
import javax.swing.text.Utilities;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.widgets.Text;

import it.uni.eclipse.bpmn2.gdpr.GDPRPropertySection.GDPRDetailComposite;
import it.uni.eclipse.bpmn2.gdpr.util.XMLTagParser;
import it.uni.eclipse.bpmn2.gdpr.util.owl.OntologyReader;

// TODO: Thoroughly document
public class GDPRTagEditor extends JFrame {
	private JPanel contentPane;
	private String taskID;

	public GDPRTagEditor(String taskID) {
		this.taskID = taskID;

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 535, 300);
		organizeLayout();

		setVisible(true);
	}

	private void organizeLayout() {
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

		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setRows(13);
		textArea.setColumns(34);
		refreshTextArea(textArea);

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

		JButton btnNewButton_2 = new JButton("Edit");
		panel_1.add(btnNewButton_2);

		// TODO: Add logic for Edit button
		addBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO: Open mask with all possible ontology items

			}
		});

		removeBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedTag = textArea.getSelectedText().split(":")[0];

				int delete = JOptionPane.showConfirmDialog(null,
						"Are you sure you want to delete tag " + selectedTag + "?", "GDPR Plugin",
						JOptionPane.YES_NO_OPTION);

				if (delete == JOptionPane.YES_OPTION)
					XMLTagParser.deleteTagFromElement(taskID, selectedTag);
				
				refreshTextArea(textArea);
			}
		});

		repaint();
	}

	private void refreshTextArea(JTextArea textArea) {
		//TODO: Reflect changes on GDPRProperySection
		textArea.setText(XMLTagParser.getElementTagsAndContent(taskID));
	}
}
