package com.turt2live.antishare.gui;

import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class PlaceholderPanel extends JPanel {

	public PlaceholderPanel(){
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel lblComingEventually = new JLabel("Coming Eventually! ");
		lblComingEventually.setFont(new Font("Ebrima", Font.ITALIC, 54));
		add(lblComingEventually);

	}

}
