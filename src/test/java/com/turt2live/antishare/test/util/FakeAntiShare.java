package com.turt2live.antishare.test.util;

import static org.mockito.Matchers.anyString;

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

public class FakeAntiShare {

	private AntiShare mock;

	// No need to test - Logic is "sound" (for now)
	public void prepare(){
		mock = PowerMockito.mock(AntiShare.class);
		final File dataFolder = new File("src" + File.separator + "test" + File.separator + "resources");
		final File dataSource = new File("src" + File.separator + "main" + File.separator + "resources");
		try{
			copy(dataSource, dataFolder);
		}catch(IOException e){
			e.printStackTrace();
		}
		EnhancedConfiguration config = new EnhancedConfiguration(new File(dataFolder, "resources" + File.separator + "config.yml"));
		PowerMockito.when(mock.getConfig()).thenReturn(config);
		PowerMockito.when(mock.getPrefix()).thenReturn("[AntiShare]");
		try{
			// Generate 100 streams to use
			File file = new File(dataFolder, "locale" + File.separator + Locale.EN_US.getFileName());
			InputStream[] streams = new InputStream[99]; // 100th is in the .thenReturn();
			for(int i = 0; i < streams.length; i++){
				streams[i] = new FileInputStream(file);
			}
			PowerMockito.when(mock.getResource(anyString())).thenReturn(new FileInputStream(file), streams);
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		if(AntiShare.getInstance() == null){
			AntiShare.setInstance(mock);
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
