package com.turt2live.antishare.log;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class LogAnalyzer extends JFrame {

	private static final long serialVersionUID = 6205053285347200574L;
	private JPanel contentPane;

	public static void main(String[] args){
		EventQueue.invokeLater(new Runnable(){
			@Override
			public void run(){
				try{
					try{
						UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
					}catch(Exception e){} // Don't handle
					LogAnalyzer frame = new LogAnalyzer();
					frame.setVisible(true);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}

	public LogAnalyzer(){
		setName("AntiShareLogAnalyzer");
		setLocale(Locale.CANADA);
		setResizable(false);
		setTitle("AntiShare Log Analyzer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 599);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		setLocationRelativeTo(null);
		contentPane.add(new LogPanel(this), BorderLayout.CENTER);
	}

}
