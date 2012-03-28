package com.turt2live.antishare.enums;

public enum NotificationType{

	// ILLEGAL actions
	ILLEGAL_BLOCK_PLACE("notifications.illegal.block_place"),
	ILLEGAL_BLOCK_BREAK("notifications.illegal.block_break"),
	ILLEGAL_CREATIVE_BLOCK_BREAK("notifications.illegal.creative_block_break"),
	ILLEGAL_SURVIVAL_BLOCK_BREAK("notifications.illegal.survival_block_break"),
	ILLEGAL_WORLD_CHANGE("notifications.illegal.world_transfer"),
	ILLEGAL_COMMAND("notifications.illegal.command"),
	ILLEGAL_DEATH("notifications.illegal.death"),
	ILLEGAL_DROP_ITEM("notifications.illegal.drop_item"),
	ILLEGAL_INTERACTION("notifications.illegal.interact"),
	ILLEGAL_PLAYER_PVP("notifications.illegal.pvp"),
	ILLEGAL_MOB_PVP("notifications.illegal.mob-pvp"),
	ILLEGAL_EGG("notifications.illegal.egg"),
	ILLEGAL_BEDROCK("notifications.illegal.bedrock_attempt"),
	ILLEGAL_EXP_BOTTLE("notifications.illegal.exp_bottle"),
	ILLEGAL_ITEM_THROW_INTO_REGION("notifications.illegal.drop_item_to_region"),
	ILLEGAL_FIRE_CHARGE("notifications.illegal.fire_charge"),
	ILLEGAL_BUCKET("notifications.illegal.bucket"),
	ILLEGAL_FIRE("notifications.illegal.fire"),
	ILLEGAL_TNT_PLACE("notifications.illegal.tnt-place"),
	ILLEGAL_REGION_ITEM("notifications.illegal.region_item"),

	// LEGAL actions
	LEGAL_BLOCK_PLACE("notifications.legal.block_place"),
	LEGAL_BLOCK_BREAK("notifications.legal.block_break"),
	LEGAL_CREATIVE_BLOCK_BREAK("notifications.legal.creative_block_break"),
	LEGAL_SURVIVAL_BLOCK_BREAK("notifications.legal.survival_block_break"),
	LEGAL_WORLD_CHANGE("notifications.legal.world_transfer"),
	LEGAL_COMMAND("notifications.legal.command"),
	LEGAL_DEATH("notifications.legal.death"),
	LEGAL_DROP_ITEM("notifications.legal.drop_item"),
	LEGAL_INTERACTION("notifications.legal.interact"),
	LEGAL_PLAYER_PVP("notifications.legal.pvp"),
	LEGAL_MOB_PVP("notifications.legal.mob-pvp"),
	LEGAL_EGG("notifications.legal.egg"),
	LEGAL_BEDROCK("notifications.legal.bedrock_attempt"),
	LEGAL_EXP_BOTTLE("notifications.legal.exp_bottle"),
	LEGAL_ITEM_THROW_INTO_REGION("notifications.legal.drop_item_to_region"),
	LEGAL_FIRE_CHARGE("notifications.legal.fire_charge"),
	LEGAL_BUCKET("notifications.legal.bucket"),
	LEGAL_FIRE("notifications.legal.fire"),
	LEGAL_TNT_PLACE("notifications.legal.tnt-place"),
	LEGAL_REGION_ITEM("notifications.legal.region_item"),

	// GENERAL actions
	GAMEMODE_CHANGE("notifications.general.gamemode_change"),
	REGION_ENTER("notifications.general.region_enter"),
	REGION_EXIT("notifications.general.region_exit"),
	TNT_CREATIVE_EXPLOSION("notifications.general.tnt_creative_explosion");

	private String configValue;

	NotificationType(String configValue){
		this.configValue = configValue;
	}

	public String getConfigValue(){
		return configValue;
	}
}
