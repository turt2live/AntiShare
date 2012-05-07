package com.turt2live.antishare.money;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.feildmaster.lib.configuration.EnhancedConfiguration;
import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.AntiShare.LogType;
import com.turt2live.antishare.metrics.TenderAmountTracker;
import com.turt2live.antishare.metrics.TenderTracker;
import com.turt2live.antishare.metrics.TrackerList.TrackerType;
import com.turt2live.antishare.money.Tender.TenderType;

/**
 * Manages awards and fines
 * 
 * @author turt2live
 */
public class MoneyManager {

	private List<Award> awards = new ArrayList<Award>();
	private List<Fine> fines = new ArrayList<Fine>();
	private AntiShare plugin;
	private boolean doAwards = false, doFines = false, tab = false;

	/**
	 * Creates a new Money Manager
	 */
	public MoneyManager(){
		plugin = AntiShare.getInstance();
		reload();
	}

	/**
	 * Reloads the manager
	 */
	public void reload(){
		// Load config
		EnhancedConfiguration money = new EnhancedConfiguration(new File(plugin.getDataFolder(), "fines.yml"), plugin);
		money.loadDefaults(plugin.getResource("resources/fines.yml"));
		if(money.needsUpdate()){
			money.saveDefaults();
		}
		money.load();

		// Set settings
		doAwards = money.getBoolean("awards-enabled");
		doFines = money.getBoolean("fines-enabled");
		tab = money.getBoolean("keep-tab");

		// Prepare
		awards.clear();
		fines.clear();

		// Load tender
		int finesLoaded = 0;
		int awardsLoaded = 0;
		for(TenderType type : TenderType.values()){
			String path = type.getConfigurationKey();
			boolean doFine = money.getBoolean(path + ".do-fine");
			boolean doAward = money.getBoolean(path + ".do-award");
			double fine = money.getDouble(path + ".fine");
			double award = money.getDouble(path + ".award");
			double noMoney = money.getString(path + ".no-money").equalsIgnoreCase("default") ? fine : money.getDouble(path + ".no-money");
			Award a = new Award(type, award, doAward);
			Fine f = new Fine(type, fine, doFine, noMoney);
			awards.add(a);
			fines.add(f);

			// Record stats
			if(doFine){
				finesLoaded++;
			}
			if(doAward){
				awardsLoaded++;
			}

			// Configure trackers
			// Award tracker
			if(plugin.getTrackers().getTracker(TrackerType.AWARD, type.getName()) == null){
				TenderTracker tracker = new TenderTracker(type.getName(), TrackerType.AWARD, a);
				plugin.getTrackers().add(tracker);
			}else{
				((TenderTracker) plugin.getTrackers().getTracker(TrackerType.AWARD, type.getName())).updateTender(a);
			}
			// Award Amount tracker
			if(plugin.getTrackers().getTracker(TrackerType.AWARD_AMOUNT, type.getName()) == null){
				TenderAmountTracker tracker = new TenderAmountTracker(type.getName(), TrackerType.AWARD_AMOUNT, a);
				plugin.getTrackers().add(tracker);
			}else{
				((TenderAmountTracker) plugin.getTrackers().getTracker(TrackerType.AWARD_AMOUNT, type.getName())).updateTender(a);
			}
			// Fine tracker
			if(plugin.getTrackers().getTracker(TrackerType.FINE, type.getName()) == null){
				TenderTracker tracker = new TenderTracker(type.getName(), TrackerType.FINE, f);
				plugin.getTrackers().add(tracker);
			}else{
				((TenderTracker) plugin.getTrackers().getTracker(TrackerType.FINE, type.getName())).updateTender(f);
			}
			// Fine Amount tracker
			if(plugin.getTrackers().getTracker(TrackerType.FINE_AMOUNT, type.getName()) == null){
				TenderAmountTracker tracker = new TenderAmountTracker(type.getName(), TrackerType.FINE_AMOUNT, f);
				plugin.getTrackers().add(tracker);
			}else{
				((TenderAmountTracker) plugin.getTrackers().getTracker(TrackerType.FINE_AMOUNT, type.getName())).updateTender(f);
			}
		}

		// Spam console
		plugin.getMessenger().log("Fines Loaded: " + finesLoaded, Level.INFO, LogType.INFO);
		plugin.getMessenger().log("Awards Loaded: " + awardsLoaded, Level.INFO, LogType.INFO);
	}

	/**
	 * Saves the money manager
	 */
	public void save(){
		// TODO: Save
	}

}
