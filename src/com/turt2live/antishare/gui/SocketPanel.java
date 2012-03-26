package com.turt2live.antishare.gui;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SocketPanel extends JPanel {

	private static final long serialVersionUID = 6170366482639333037L;
	private JTextField port;
	private JTextField ip;

	public SocketPanel(){
		setLayout(null);

		JLabel lblAntisharesGuiCouldnt = new JLabel("<html><body><b>AntiShare's GUI couldn't connect to the plugin!</b><br>Please verify the information below and reconnect if you would like to use this feature.</body></html>");
		lblAntisharesGuiCouldnt.setBounds(16, 13, 388, 48);
		add(lblAntisharesGuiCouldnt);

		JLabel label = new JLabel("");
		label.setBounds(388, 235, 0, 0);
		add(label);

		JLabel label_1 = new JLabel("");
		label_1.setBounds(388, 235, 0, 0);
		add(label_1);

		JLabel label_2 = new JLabel("");
		label_2.setBounds(388, 235, 0, 0);
		add(label_2);

		JLabel lblIp = new JLabel("IP:");
		lblIp.setBounds(36, 74, 16, 16);
		add(lblIp);

		ip = new JTextField();
		ip.setBounds(74, 70, 88, 25);
		ip.setText("localhost");
		add(ip);
		ip.setColumns(10);

		JLabel lblPort = new JLabel("Port:");
		lblPort.setBounds(36, 96, 28, 16);
		add(lblPort);

		port = new JTextField();
		port.setBounds(74, 92, 88, 25);
		port.setText("4096");
		add(port);
		port.setColumns(10);

		JButton btnConnect = new JButton("Connect");
		btnConnect.setBounds(28, 125, 134, 25);
		add(btnConnect);

		JLabel label_3 = new JLabel("");
		label_3.setBounds(687, 235, 0, 0);
		add(label_3);

	}

}
