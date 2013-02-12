package com.turt2live.antishare.pail;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import me.escapeNT.pail.Pail;

import com.turt2live.antishare.AntiShare;

public class PailPane extends JPanel {

	private static final long serialVersionUID = 3497861298959219768L;

	private AntiShare plugin = AntiShare.getInstance();
	private Pail pail;

	public PailPane(Pail pail){
		this.pail = pail;
	}

	@Override
	public void paint(Graphics g){
		super.paint(g);

		BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = image.createGraphics();
		g2.drawString("AntiShare version " + plugin.getDescription().getVersion() + " build " + plugin.getBuild(), 10, 10);
		g2.drawString("Pail version " + this.pail.getDescription().getVersion(), 10, 40);

		g.drawImage(image, 0, 0, null);
	}

}
