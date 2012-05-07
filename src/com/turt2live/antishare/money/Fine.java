package com.turt2live.antishare.money;

import org.bukkit.entity.Player;

import com.turt2live.antishare.metrics.TrackerList.TrackerType;
import com.turt2live.antishare.permissions.PermissionNodes;

/**
 * Award for doing something
 * 
 * @author turt2live
 */
public class Fine extends Tender {

	private double overcharge;

	/**
	 * Creates a new fine
	 * 
	 * @param type the type
	 * @param amount the amount (positive to remove from account)
	 * @param enabled true to enable
	 * @param overcharge the amount to charge if the account has less than or equal to zero
	 */
	public Fine(TenderType type, double amount, boolean enabled, double overcharge){
		super(type, amount, enabled);
		this.overcharge = overcharge;
	}

	@Override
	public void apply(Player player){
		if(!isEnabled() || plugin.getPermissions().has(player, PermissionNodes.MONEY_NO_FINE)){
			return;
		}

		// TODO: Apply to account with tab if needed

		// Increment statistic
		plugin.getTrackers().getTracker(TrackerType.FINE_GIVEN).increment(1); // Does not have a name!
	}

}
