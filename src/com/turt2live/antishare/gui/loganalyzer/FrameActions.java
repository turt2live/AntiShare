package com.turt2live.antishare.gui.loganalyzer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class FrameActions implements ActionListener, ListSelectionListener {

	private LogPanel panel;
	private LogFile log;

	public FrameActions(LogPanel panel){
		this.panel = panel;
	}

	@Override
	public void actionPerformed(ActionEvent event){
		JFileChooser open = new JFileChooser();
		open.setApproveButtonText("Open Log");
		open.setDialogTitle("Open AntiShare Log");
		open.setCurrentDirectory(new File(System.getProperty("user.dir")));
		open.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int result = open.showOpenDialog(null);
		if(result == JFileChooser.APPROVE_OPTION){
			LogFile log = new LogFile(open.getSelectedFile());
			if(log.invalid()){
				JOptionPane.showMessageDialog(null, "That is not an AntiShare log!", "Invalid File", JOptionPane.ERROR_MESSAGE);
			}else{
				this.log = log;
				this.log.displayTo(panel.list);
				panel.frame.setTitle("AntiShare Log Analyzer (" + log.file.getName() + ")");
			}
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent event){
		if(log != null){
			log.display(panel.list.getSelectedIndex(), panel.text);
		}else{
			System.out.println("Null");
		}
	}
}
