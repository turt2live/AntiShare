package com.turt2live.antishare.log;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;

public class LogPanel extends JPanel {

	private static final long serialVersionUID = -2029283584832117230L;
	public JTextArea textArea;
	public JList list;
	public JFrame frame;

	public LogPanel(JFrame frame){
		this.frame = frame;
		JScrollPane scrollPane = new JScrollPane();
		JButton btnOpenFile = new JButton("Open File");
		JScrollPane scrollPane_1 = new JScrollPane();
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
				groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
								.addContainerGap()
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
										.addComponent(btnOpenFile, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 402, GroupLayout.PREFERRED_SIZE)
								.addGap(17))
				);
		groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
								.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
										.addComponent(scrollPane_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 594, Short.MAX_VALUE)
										.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
												.addGap(4)
												.addComponent(btnOpenFile)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 556, Short.MAX_VALUE)))
								.addContainerGap())
				);

		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setTabSize(4);
		textArea.setEditable(false);
		scrollPane_1.setViewportView(textArea);
		list = new JList();
		FrameActions actions = new FrameActions(this);
		btnOpenFile.addActionListener(actions);
		list.addListSelectionListener(actions);
		scrollPane.setViewportView(list);
		setLayout(groupLayout);
	}
}
