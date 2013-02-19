package com.turt2live.antishare.io;

// TODO: Document
public interface Saveable {

	public void onSave();

	public SaveWrapper getSaveLocation();

	@Override
	public String toString();

}
