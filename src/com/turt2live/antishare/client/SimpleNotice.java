package com.turt2live.antishare.client;

import org.bukkit.entity.Player;

import com.turt2live.antishare.AntiShare;

public class SimpleNotice {

	public void onEnable(){
		AntiShare.getInstance().getServer().getMessenger().registerOutgoingPluginChannel(AntiShare.getInstance(), "SimpleNotice");
	}

	public boolean sendPluginMessage(Player player, String message){
		if(player == null || message == null){
			return false;
		}
		if(!player.getListeningPluginChannels().contains("SimpleNotice")){
			return false;
		}
		try{
			player.sendPluginMessage(AntiShare.getInstance(), "SimpleNotice", message.getBytes("UTF-8"));
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

}
