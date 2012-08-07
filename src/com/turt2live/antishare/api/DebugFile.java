package com.turt2live.antishare.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import com.turt2live.antishare.ASUtils;
import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.AntiShare.LogType;
import com.turt2live.antishare.ErrorLog;
import com.turt2live.antishare.xmail.XMailMail;

public class DebugFile {

	protected String data, url, title;
	protected PasteBin pastebin;

	public DebugFile(File file){
		StringBuilder buffer = new StringBuilder();
		try{
			BufferedReader in = new BufferedReader(new FileReader(file));
			String line;
			while ((line = in.readLine()) != null){
				buffer.append(line).append("\n");
			}
			in.close();
		}catch(Exception e){
			AntiShare.getInstance().getMessenger().log("AntiShare encountered and error. Please report this to turt2live.", Level.SEVERE, LogType.ERROR);
			AntiShare.getInstance().getMessenger().log("Please see " + ErrorLog.print(e) + " for the full error.", Level.SEVERE, LogType.ERROR);
		}
		pastebin = new PasteBin(true);
		title = "AntiShare Debug File [" + AntiShare.getVersion() + "]";
		data = buffer.toString();
	}

	public void save(CommandSender sender){
		String enc = pastebin.encodeData(data, title);
		url = pastebin.postData(enc, pastebin.getPostURL());
		if(sender != null){
			ASUtils.sendToPlayer(sender, "Debug file is located online at: " + url, true);
		}
	}

	public void alert(){
		Plugin xmail = AntiShare.getInstance().getServer().getPluginManager().getPlugin("xMail");
		if(xmail != null){
			new XMailMail("AntiShare", "New paste: " + title + " (URL = " + url + ")").send();
		}else{
			// We can post it ourselves
			post(new Variable("pid", "1"), new Variable("uid", UUID.randomUUID().toString()), new Variable("ident", "S"), new Variable("to", "AntiShare"),
					new Variable("from", "CONSOLE@" + ASUtils.getIp()), new Variable("message", "New paste: " + title + " (URL = " + url + ")"),
					new Variable("apikey", "null"));
		}
	}

	private void post(Variable... variables){
		String url = "http://xmail.turt2live.com/mail/index.php";
		String data = "";

		// Encode the mode
		try{
			data = URLEncoder.encode("mode", "UTF-8") + "=" + URLEncoder.encode("SEND", "UTF-8");
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}

		// Encode the variables (if any)
		for(Variable var : variables){
			try{
				data += "&" + URLEncoder.encode(var.key, "UTF-8") + "=" + URLEncoder.encode(var.value, "UTF-8");
			}catch(UnsupportedEncodingException e){
				e.printStackTrace();
			}
		}
		Variable version = new Variable("version", "ANTISHARE");
		try{
			data += "&" + URLEncoder.encode(version.key, "UTF-8") + "=" + URLEncoder.encode(version.value, "UTF-8");
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}

		try{
			// Setup connection
			URL con = new URL(url);
			URLConnection connection = con.openConnection();
			connection.setDoOutput(true);

			// Setup write
			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
			writer.write(data);
			writer.flush();

			// Setup read
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String responseLine;

			// Build a response
			String response = null;
			List<String> messages = new ArrayList<String>();
			while ((responseLine = reader.readLine()) != null){
				if(response == null){
					response = responseLine;
				}else{
					messages.add(responseLine);
				}
			}

			// Cleanup
			writer.close();
			reader.close();

			// Print response
			if(AntiShare.getInstance().getConfig().getBoolean("other.debug")){
				AntiShare.getInstance().getLogger().info(response);
			}
		}catch(UnknownHostException e){
			AntiShare.getInstance().getLogger().severe("Exception occured in Debug File[1]. Please report to turt2live.");
		}catch(IOException e){
			AntiShare.getInstance().getLogger().severe("Exception occured in Debug File[2]. Please report to turt2live.");
		}
	}

}
