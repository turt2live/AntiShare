package com.turt2live.antishare.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.logging.Level;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.AntiShare.LogType;
import com.turt2live.antishare.ErrorLog;

// Original copyright notice for this file.
/******************************************************************************
 * Multiverse 2 Copyright (c) the Multiverse Team 2011. *
 * Multiverse 2 is licensed under the BSD License. *
 * For more information please check the README.md file included *
 * with this project. *
 ******************************************************************************/
// The readme.md file is located at https://github.com/Multiverse/Multiverse-Core/blob/master/README.md

/*
 * Code adapated and modified from Multiverse-Core
 * (URL: https://github.com/Multiverse/Multiverse-Core/blob/master/src/main/java/com/onarandombox/MultiverseCore/utils/webpaste/PastebinPasteService.java)
 * This code is modified for use in AntiShare.
 * This code is released under the BSD License
 */

/**
 * Pastes to {@code pastebin.com}.
 */
public class PasteBin {

	private boolean isPrivate;

	public PasteBin(boolean isPrivate){
		this.isPrivate = isPrivate;
	}

	public URL getPostURL(){
		try{
			return new URL("http://pastebin.com/api/api_post.php");
		}catch(MalformedURLException e){
			return null; // should never hit here
		}
	}

	public String encodeData(String data, String title){
		try{
			String encData = URLEncoder.encode("api_dev_key", "UTF-8") + "=" + URLEncoder.encode("c26bcc280fb09c0d9f2dd0d0c5acb584", "UTF-8");
			encData += "&" + URLEncoder.encode("api_option", "UTF-8") + "=" + URLEncoder.encode("paste", "UTF-8");
			encData += "&" + URLEncoder.encode("api_paste_code", "UTF-8") + "=" + URLEncoder.encode(data, "UTF-8");
			encData += "&" + URLEncoder.encode("api_paste_private", "UTF-8") + "=" + URLEncoder.encode(this.isPrivate ? "1" : "0", "UTF-8");
			encData += "&" + URLEncoder.encode("api_paste_name", "UTF-8") + "=" + URLEncoder.encode(title, "UTF-8");
			encData += "&" + URLEncoder.encode("api_paste_format", "UTF-8") + "=" + URLEncoder.encode("text", "UTF-8");
			return encData;
		}catch(UnsupportedEncodingException e){
			return ""; // should never hit here
		}
	}

	public String postData(String encodedData, URL url){
		OutputStreamWriter wr = null;
		BufferedReader rd = null;
		try{
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(encodedData);
			wr.flush();

			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			String pastebinUrl = "";
			while ((line = rd.readLine()) != null){
				pastebinUrl = line;
			}
			return pastebinUrl;
		}catch(Exception e){
			AntiShare.getInstance().getMessenger().log("AntiShare encountered and error. Please report this to turt2live.", Level.SEVERE, LogType.ERROR);
			AntiShare.getInstance().getMessenger().log("Please see " + ErrorLog.print(e) + " for the full error.", Level.SEVERE, LogType.ERROR);
		}finally{
			if(wr != null){
				try{
					wr.close();
				}catch(IOException ignore){}
			}
			if(rd != null){
				try{
					rd.close();
				}catch(IOException ignore){}
			}
		}
		return "Failed";
	}
}
