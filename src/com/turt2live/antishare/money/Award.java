package com.turt2live.antishare.money;

import org.bukkit.entity.Player;

import com.turt2live.antishare.metrics.TrackerList.TrackerType;
import com.turt2live.antishare.permissions.PermissionNodes;

/**
 * Award for doing something
 * 
 * @author turt2live
 */
public class Award extends Tender {

	/**
	 * Creates a new award
	 * 
	 * @param type the type
	 * @param amount the amount (positive to add to account)
	 * @param enabled true to enable
	 */
	public Award(TenderType type, double amount, boolean enabled){
		super(type, amount, enabled);
	}

	@Override
	public void apply(Player player){
		if(!isEnabled() || plugin.getPermissions().has(player, PermissionNodes.MONEY_NO_AWARD)){
			return;
		}

		// TODO: Apply to account

		// Increment statistic
		plugin.getTrackers().getTracker(TrackerType.AWARD_GIVEN).increment(1); // Does not have a name!
	}

}
