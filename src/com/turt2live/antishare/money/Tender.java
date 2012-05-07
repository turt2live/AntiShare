package com.turt2live.antishare.money;

import org.bukkit.entity.Player;

import com.turt2live.antishare.AntiShare;

/**
 * A class for money (Award or Fine)
 * 
 * @author turt2live
 */
public abstract class Tender {

	/**
	 * An enum to represent tender type
	 * 
	 * @author turt2live
	 * 
	 */
	public static enum TenderType{
		BLOCK_BREAK("actions.block-break", "Block Break"),
		BLOCK_PLACE("actions.block-place", "Block Place"),
		ITEM_DROP("actions.item-drop", "Item Drop"),
		ITME_PICKUP("actions.item-pickup", "Item Pickup"),
		DEATH("actions.player-death", "Player Death"),
		RIGHT_CLICK("actions.right-click", "Right Click"),
		USE("actions.use", "Use"),
		COMMAND("actions.command", "Command"),
		HIT_PLAYER("actions.player-hit-player", "Player Hit Player"),
		HIT_MOB("actions.player-hit-mob", "Player Hit Mob");

		private String key, name;

		private TenderType(String key, String name){
			this.key = key;
			this.name = name;
		}

		/**
		 * Gets the configuration key from root in the fines.yml
		 * 
		 * @return the configuration path
		 */
		public String getConfigurationKey(){
			return key;
		}

		/**
		 * Gets the name of the action. Used by trackers
		 * 
		 * @return the name
		 */
		public String getName(){
			return name;
		}
	}

	private double amount;
	private TenderType type;
	private boolean enabled;
	protected AntiShare plugin = AntiShare.getInstance();

	/**
	 * Creates a new Tender
	 * 
	 * @param type the type
	 * @param amount the amount
	 * @param enabled true to enable
	 */
	public Tender(TenderType type, double amount, boolean enabled){
		this.type = type;
		this.amount = amount;
		this.enabled = enabled;
	}

	/**
	 * Determines if this tender is enabled
	 * 
	 * @return true if enabled
	 */
	public boolean isEnabled(){
		return enabled;
	}

	/**
	 * Gets the amount of this tender
	 * 
	 * @return the amount
	 */
	public double getAmount(){
		return amount;
	}

	/**
	 * Gets the type of this tender
	 * 
	 * @return the type
	 */
	public TenderType getType(){
		return type;
	}

	/**
	 * Applies the tender to the player
	 * 
	 * @param player
	 */
	public abstract void apply(Player player);
}
