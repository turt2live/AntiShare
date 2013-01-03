package com.turt2live.antishare.storage;

import java.io.File;

class NullObjectSaver extends ObjectSaver {

	public NullObjectSaver(){
		super(null, null, null, null, false);
	}

	@Override
	public void run(){}

	@Override
	void save(File dir, String fname, String key){}

	@Override
	double getPercent(){
		return 100;
	}

}
