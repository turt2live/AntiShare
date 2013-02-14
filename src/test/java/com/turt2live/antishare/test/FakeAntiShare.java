package com.turt2live.antishare.test;

import static org.mockito.Matchers.anyString;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.powermock.api.mockito.PowerMockito;

import com.feildmaster.lib.configuration.EnhancedConfiguration;
import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.lang.Locale;

public class FakeAntiShare {

	private AntiShare mock;

	public void prepare(){
		mock = PowerMockito.mock(AntiShare.class);
		final File dataFolder = new File("src" + File.separator + "main" + File.separator + "resources");
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

}
