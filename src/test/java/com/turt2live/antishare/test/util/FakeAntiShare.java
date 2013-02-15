package com.turt2live.antishare.test.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.powermock.api.mockito.PowerMockito;

import com.feildmaster.lib.configuration.EnhancedConfiguration;
import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.lang.Locale;
import com.turt2live.antishare.signs.SignList;
import com.turt2live.metrics.EMetrics;

public class FakeAntiShare {

	public static final File DATA_FOLDER = new File("src" + File.separator + "test" + File.separator + "resources");
	public static final File DATA_SOURCE = new File("src" + File.separator + "main" + File.separator + "resources");

	private AntiShare mock;
	public SignList signs = null;

	// No need to test - Logic is "sound" (for now)
	@SuppressWarnings ("deprecation")
	public void prepare(){
		mock = PowerMockito.mock(AntiShare.class);
		try{
			copy(DATA_SOURCE, DATA_FOLDER);
		}catch(IOException e){
			e.printStackTrace();
		}
		list(DATA_FOLDER, 0);
		System.out.println("----------------------");
		list(DATA_SOURCE, 0);
		EnhancedConfiguration config = new EnhancedConfiguration(new File(DATA_FOLDER, "resources" + File.separator + "config.yml"));
		PowerMockito.when(mock.getConfig()).thenReturn(config);
		PowerMockito.when(mock.getPrefix()).thenReturn("[AntiShare]");
		EMetrics fakeMetrics = PowerMockito.mock(EMetrics.class);
		PowerMockito.when(mock.getMetrics()).thenReturn(fakeMetrics);
		try{
			// Locale
			File file = new File(DATA_FOLDER, "locale" + File.separator + Locale.EN_US.getFileName());
			InputStream[] streams = new InputStream[99]; // 100th is in the .thenReturn();
			for(int i = 0; i < streams.length; i++){
				streams[i] = new FileInputStream(file);
			}
			PowerMockito.when(mock.getResource("locale/" + Locale.EN_US.getFileName())).thenReturn(new FileInputStream(file), streams);

			// Item Map
			streams = new InputStream[99]; // 100th is in the .thenReturn();
			file = new File(DATA_FOLDER, "resources" + File.separator + "items.yml");
			for(int i = 0; i < streams.length; i++){
				streams[i] = new FileInputStream(file);
			}
			PowerMockito.when(mock.getResource("resources/items.yml")).thenReturn(new FileInputStream(file), streams);

			// Sign List
			streams = new InputStream[99]; // 100th is in the .thenReturn();
			file = new File(DATA_FOLDER, "resources" + File.separator + "signs.yml");
			for(int i = 0; i < streams.length; i++){
				streams[i] = new FileInputStream(file);
			}
			PowerMockito.when(mock.getResource("resources/signs.yml")).thenReturn(new FileInputStream(file), streams);
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		if(signs != null){
			PowerMockito.when(mock.getSignList()).thenReturn(signs);
		}
		AntiShare.setInstance(null);
		AntiShare.setInstance(mock);
	}

	private void list(File directory, int spaces){
		String space = "";
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < spaces; i++){
			builder.append(" ");
		}
		space = builder.toString();
		File[] files = directory.listFiles();
		if(files != null){
			for(File file : files){
				if(file.isDirectory()){
					System.out.println(space + "+ " + file.getName());
					list(file, spaces + 1);
				}else{
					System.out.println(space + "- " + file.getName());
				}
			}
		}
	}

	private void copy(File sourceLocation, File targetLocation) throws IOException{
		if(sourceLocation.isDirectory()){
			if(!targetLocation.exists()){
				targetLocation.mkdir();
			}

			String[] children = sourceLocation.list();
			for(int i = 0; i < children.length; i++){
				copy(new File(sourceLocation, children[i]),
						new File(targetLocation, children[i]));
			}
		}else{

			InputStream in = new FileInputStream(sourceLocation);
			OutputStream out = new FileOutputStream(targetLocation);

			// Copy the bits from instream to outstream
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0){
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
		}
	}

	public AntiShare get(){
		return mock;
	}

}
