package com.turt2live.antishare.conversations.region;

import java.util.Vector;

import org.bukkit.ChatColor;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;

import com.turt2live.antishare.ASUtils;
import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.conversations.ASMenu;
import com.turt2live.antishare.conversations.MainMenu;
import com.turt2live.antishare.regions.ASRegion;

public class EditRegionMenu extends ASMenu {

	private int page = 1;
	private int maxPages = 0;
	private int resultsPerPage = 6; // For ease of changing
	private Vector<ASRegion> regions;

	public EditRegionMenu(int page, AntiShare plugin){
		this.page = Math.abs(page);
		regions = plugin.storage.getAllRegions();
		maxPages = (int) Math.ceil(regions.size() / resultsPerPage);
		if(maxPages < 1){
			maxPages = 1;
		}
	}

	@Override
	public String getPromptText(ConversationContext context){
		displayMenu(context.getForWhom());
		return "Enter '" + ChatColor.DARK_AQUA + "edit <region #>" + ChatColor.WHITE + "' or a page number:";
	}

	@Override
	public void displayMenu(Conversable target){
		String pagenation = ChatColor.DARK_GREEN + "=======[ " + ChatColor.GREEN + "Edit Regions " + ChatColor.DARK_GREEN + "|" + ChatColor.GREEN + " Page " + page + "/" + maxPages + ChatColor.DARK_GREEN + " ]=======";
		ASUtils.sendToConversable(target, pagenation);
		for(int i = ((page - 1) * resultsPerPage); i < (resultsPerPage < regions.size() ? (resultsPerPage * page) : regions.size()); i++){
			ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "#" + (i + 1) + " " + ChatColor.GOLD + regions.get(i).getName()
					+ ChatColor.YELLOW + " Created By: " + ChatColor.AQUA + regions.get(i).getWhoSet()
					+ ChatColor.YELLOW + " World: " + ChatColor.AQUA + regions.get(i).getWorld().getName());
		}
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String input){
		input = input.trim();
		AntiShare plugin = (AntiShare) context.getPlugin();
		if(input.equalsIgnoreCase("back")){
			return new MainMenu();
		}
		try{
			int newPage = Integer.parseInt(input);
			return new EditRegionMenu(newPage, plugin);
		}catch(Exception e){ //Not a page
			if(input.toLowerCase().startsWith("edit")){
				input = input.substring(4).replace("#", "").trim();
				try{
					if(regions.size() >= Integer.parseInt(input)){
						return new RegionEditor(regions.get(Integer.parseInt(input)));
					}else{
						ASUtils.sendToConversable(context.getForWhom(), ChatColor.DARK_RED + "=======[ " + ChatColor.RED + "Invalid Option" + ChatColor.DARK_RED + " ]=======");
						ASUtils.sendToConversable(context.getForWhom(), ChatColor.RED + "That region does not exist!");
						return new EditRegionMenu(page, plugin);
					}
				}catch(Exception e1){
					ASUtils.sendToConversable(context.getForWhom(), ChatColor.DARK_RED + "=======[ " + ChatColor.RED + "Invalid Option" + ChatColor.DARK_RED + " ]=======");
					ASUtils.sendToConversable(context.getForWhom(), ChatColor.RED + "That region does not exist!");
					return new EditRegionMenu(page, plugin);
				}
			}
		}
		return new EditRegionMenu(page, plugin);
	}

	@Override
	protected String getFailedValidationText(ConversationContext context, String invalidInput){
		ASUtils.sendToConversable(context.getForWhom(), ChatColor.DARK_RED + "=======[ " + ChatColor.RED + "Invalid Option" + ChatColor.DARK_RED + " ]=======");
		return "That is not a number or that page does not exist!";
	}

	@Override
	protected boolean isInputValid(ConversationContext context, String input){
		input = input.toLowerCase().trim();
		if(input.startsWith("edit")){
			return true;
		}
		try{
			int newPage = Integer.parseInt(input);
			if(maxPages < newPage){
				return false;
			}
			return true;
		}catch(Exception e){
			return false;
		}
	}
}
