package com.turt2live.antishare.lang;

public enum LocaleMessage{

	UPDATE_READY("update-ready"),
	UPDATE_LINK("update-link"),
	BUG_FILES_REMOVE("bug-files-removed");

	private String node;

	private LocaleMessage(String node){
		this.node = node;
	}

	public String getConfigurationNode(){
		return node;
	}

}
