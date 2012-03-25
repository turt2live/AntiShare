package com.turt2live.antishare.gui;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.io.File;
import java.util.Locale;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.turt2live.antishare.gui.loganalyzer.LogPanel;

public class AntiShareGUI extends JFrame {

	private static final long serialVersionUID = 6205053285347200574L;
	private JPanel contentPane;
	private File antishare;
	private AntiShareConfiguration config;

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
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
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

		antishare = new File(System.getProperty("user.dir"), "AntiShare");
		if(!antishare.exists()){
			JOptionPane.showMessageDialog(null, "I couldn't find your AntiShare plugin folder!\nPlease guide me to it.", "Cannot Find Folder", JOptionPane.ERROR_MESSAGE);
			boolean valid = false;
			while (!valid){
				JFileChooser open = new JFileChooser();
				open.setApproveButtonText("Open Folder");
				open.setDialogTitle("Find AntiShare Plugin Folder");
				open.setCurrentDirectory(new File(System.getProperty("user.dir")));
				open.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int result = open.showOpenDialog(null);
				if(result == JFileChooser.CANCEL_OPTION){
					int exit = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
					if(exit == JOptionPane.YES_OPTION){
						System.exit(0);
					}
				}else{
					antishare = open.getSelectedFile();
					config = new AntiShareConfiguration(new File(antishare, "config.yml"));
					if(!config.isValid()){
						JOptionPane.showMessageDialog(null, "The selected folder has an invalid configuration :(\nPlease guide me to the valid folder.", "Invalid Folder", JOptionPane.ERROR_MESSAGE);
					}else{
						valid = true;
					}
				}
			}
		}else{
			config = new AntiShareConfiguration(new File(antishare, "config.yml"));
			if(!config.isValid()){
				JOptionPane.showMessageDialog(null, "I found your AntiShare directory, but it has an invalid configuration :(\nPlease guide me to the valid folder.", "Invalid Folder", JOptionPane.ERROR_MESSAGE);
				boolean valid = false;
				while (!valid){
					JFileChooser open = new JFileChooser();
					open.setApproveButtonText("Open Folder");
					open.setDialogTitle("Find AntiShare Plugin Folder");
					open.setCurrentDirectory(new File(System.getProperty("user.dir")));
					open.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					int result = open.showOpenDialog(null);
					if(result == JFileChooser.CANCEL_OPTION){
						int exit = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
						if(exit == JOptionPane.YES_OPTION){
							System.exit(0);
						}
					}else{
						antishare = open.getSelectedFile();
						config = new AntiShareConfiguration(new File(antishare, "config.yml"));
						if(!config.isValid()){
							JOptionPane.showMessageDialog(null, "The selected folder has an invalid configuration :(\nPlease guide me to the valid folder.", "Invalid Folder", JOptionPane.ERROR_MESSAGE);
						}else{
							valid = true;
						}
					}
				}
			}
		}
		addWindowListener(new FrameActions());
	}

	public File getPluginFolder(){
		return antishare;
	}

	public AntiShareConfiguration getConfig(){
		return config;
	}
}
