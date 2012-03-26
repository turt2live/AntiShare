package com.turt2live.antishare.gui;

import java.awt.Font;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class PlaceholderPanel extends JPanel {

	private static final long serialVersionUID = 2934922357302818102L;
	private JEditorPane editorPane_1;

	public PlaceholderPanel(){
		setLayout(null);

		JLabel lblComingSoon = new JLabel("<html><body><h1>Coming Soon!</h1></body></html>");
		lblComingSoon.setBounds(7, 7, 178, 57);
		lblComingSoon.setFont(new Font("Meiryo", Font.BOLD, 22));
		add(lblComingSoon);

		JLabel lblthisPartOf = new JLabel("<html><body><b>This part of AntiShare is in beta!</b> Because of this there may be major bugs or lack of support. If you have suggestions please email me at turt2live@turt2live.com!</body></html>");
		lblthisPartOf.setBounds(7, 68, 673, 32);
		add(lblthisPartOf);

		try{
			editorPane_1 = new JEditorPane(new URL("http://antishare.turt2live.com/todo.html"));
			editorPane_1.setBounds(7, 104, 673, 493);
			editorPane_1.setBackground(UIManager.getColor("Button.light"));
		}catch(MalformedURLException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		editorPane_1.setEditable(false);
		add(editorPane_1);

	}

}
