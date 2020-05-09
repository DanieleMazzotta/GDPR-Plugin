package it.uni.eclipse.bpmn2.gdpr.extendModel;

import java.io.IOException;

public class XMLModify {
	public static void main(String[] args) throws IOException {
		PropertyDescriptor pd = new PropertyDescriptor();
		pd.getFrame().setLocationRelativeTo(null);
		pd.getFrame().setVisible(true);
		pd.getFrame().setResizable(false);
	}
}