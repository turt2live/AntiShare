package com.turt2live.antishare.test.util;

import java.io.File;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

public class CleanupAfterTest extends RunListener {

	@Override
	// Called before any tests have been run.
	public void testRunStarted(Description description){}

	@Override
	// Called when all tests have finished
	public void testRunFinished(Result result){

		// Cleanup files
		File[] files = new File[] {
				//new File("items.yml"),
				new File("signs.yml"),
				new File("locale")
		};

		for(File file : files){
			if(file.exists()){
				if(file.isDirectory()){
					wipeFolder(file);
				}else{
					file.delete();
				}
			}
		}
	}

	private void wipeFolder(File file){
		File[] files = file.listFiles();
		if(files != null){
			for(File f : files){
				if(f.isDirectory()){
					wipeFolder(f);
					f.delete();
				}else{
					f.delete();
				}
			}
		}
		file.delete();
	}

}
