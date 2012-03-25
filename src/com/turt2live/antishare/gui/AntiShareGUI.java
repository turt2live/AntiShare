package com.turt2live.antishare.gui;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.turt2live.antishare.gui.loganalyzer.LogPanel;

public class AntiShareGUI extends JFrame {

	private static final long serialVersionUID = 6205053285347200574L;
	private JPanel contentPane;

	public static void main(String[] args){
		EventQueue.invokeLater(new Runnable(){
			@Override
			public void run(){
				try{
					try{
						//UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
						UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					}catch(Exception e){} // Don't handle
					AntiShareGUI frame = new AntiShareGUI();
					frame.setVisible(true);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}

	public AntiShareGUI(){
		setName("AntiShare");
		setLocale(Locale.CANADA);
		setResizable(false);
		setTitle("AntiShare");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 704, 599);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(0, 1, 0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane);

		JPanel panel_1 = new PlaceholderPanel();
		tabbedPane.addTab("Configuration", null, panel_1, null);

		JPanel panel_2 = new PlaceholderPanel();
		tabbedPane.addTab("Permissions", null, panel_2, null);

		JPanel panel = new LogPanel(this);
		tabbedPane.addTab("Log Analyzer", null, panel, null);

		JPanel panel_3 = new PlaceholderPanel();
		tabbedPane.addTab("Help", null, panel_3, null);

		tabbedPane.setSelectedIndex(0);
		setLocationRelativeTo(null);
	}

}
