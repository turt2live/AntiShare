package com.turt2live.antishare.pail.pane;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.turt2live.antishare.AntiShare;

/**
 * Configuration pane
 * 
 * @author turt2live
 */
public class ConfigPane extends JPanel {

	private static final long serialVersionUID = 1870305294715678836L;
	private AntiShare plugin = AntiShare.getInstance();

	@Override
	public void paint(Graphics g){
		super.paint(g);

		BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = image.createGraphics();
		g2.setColor(Color.RED);
		g2.drawString("Settings Coming Soon", 10, 30);
		g2.drawString("AntiShare version " + plugin.getDescription().getVersion() + " build " + plugin.getBuild(), 10, 50);

		g.drawImage(image, 0, 0, null);
	}

}
