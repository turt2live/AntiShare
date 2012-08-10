package com.turt2live.antishare.api;

import java.io.File;

import org.bukkit.command.CommandSender;

import com.turt2live.antishare.ASUtils;
import com.turt2live.antishare.AntiShare;

public class ErrorFile extends DebugFile {

	public ErrorFile(File file){
		super(file);
		title = "AntiShare Error File [" + AntiShare.getVersion() + "]";
	}

	@Override
	public void save(CommandSender sender){
		if(AntiShare.getInstance().getConfig().getBoolean("other.error-reporting")){
			super.save(null);
			if(sender != null){
				ASUtils.sendToPlayer(sender, "Error file is located online at: " + url, true);
			}
		}
	}

}
