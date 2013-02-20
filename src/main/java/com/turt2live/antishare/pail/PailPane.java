package com.turt2live.antishare.pail;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import me.escapeNT.pail.Pail;

import com.turt2live.antishare.AntiShare;

public class PailPane extends JPanel {

	private static final long serialVersionUID = 3497861298959219768L;
	private static Pail pail;
	private AntiShare plugin = AntiShare.getInstance();

	/**
	 * Creates a new pail pane
	 * 
	 * @param pail the pail object
	 */
	public PailPane(Pail pail){
		PailPane.pail = pail;
		//		ConfigPane config = new ConfigPane();
		//		ListPane blockLists = new ListPane();
		//
		//		setLayout(new BorderLayout(0, 0));
		//
		//		JTabbedPane tabs = new JTabbedPane();
		//		tabs.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
		//		tabs.setTabPlacement(JTabbedPane.LEFT);
		//
		//		tabs.addTab(translate("Configuration"), config);
		//		tabs.addTab(translate("Block/Track Lists"), blockLists);
		//
		//		add(tabs, BorderLayout.CENTER);
		//		validate();
	}

	@Override
	public void paint(Graphics g){
		super.paint(g);

		BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = image.createGraphics();
		g2.setColor(Color.BLACK);
		g2.drawString("Settings Coming Soon", 10, 30);
		g2.drawString("AntiShare version " + plugin.getDescription().getVersion() + " build " + plugin.getBuild(), 10, 50);

		g.drawImage(image, 0, 0, null);
	}

	/**
	 * Translates text
	 * 
	 * @param english the english text
	 * @return the translated text
	 */
	public static String translate(String english){
		return pail.translate(english);
	}

}
