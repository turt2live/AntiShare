package com.turt2live.antishare;

public class ASNotification {

	public static void sendNotification(NotificationType type, AntiShare plugin){
		switch (type){
		// ILLEGAL actions
		case ILLEGAL_BLOCK_PLACE:
			break;
		case ILLEGAL_BLOCK_BREAK:
			break;
		case ILLEGAL_CREATIVE_BLOCK_BREAK:
			break;
		case ILLEGAL_WORLD_CHANGE:
			break;
		case ILLEGAL_COMMAND:
			break;
		case ILLEGAL_DEATH:
			break;
		case ILLEGAL_DROP_ITEM:
			break;
		case ILLEGAL_INTERACTION:
			break;
		case ILLEGAL_PLAYER_PVP:
			break;
		case ILLEGAL_MOB_PVP:
			break;
		case ILLEGAL_EGG:
			break;
		case ILLEGAL_BEDROCK:
			break;

		// LEGAL actions
		case LEGAL_BLOCK_PLACE:
			break;
		case LEGAL_BLOCK_BREAK:
			break;
		case LEGAL_CREATIVE_BLOCK_BREAK:
			break;
		case LEGAL_WORLD_CHANGE:
			break;
		case LEGAL_COMMAND:
			break;
		case LEGAL_DEATH:
			break;
		case LEGAL_DROP_ITEM:
			break;
		case LEGAL_INTERACTION:
			break;
		case LEGAL_PLAYER_PVP:
			break;
		case LEGAL_MOB_PVP:
			break;
		case LEGAL_EGG:
			break;
		case LEGAL_BEDROCK:
			break;

		// GENERAL actions
		case GAMEMODE_INVENTORY_CHANGE:
			break;
		}
	}

}
