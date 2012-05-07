package com.turt2live.antishare.metrics;

import com.turt2live.antishare.metrics.TrackerList.TrackerType;
import com.turt2live.antishare.money.Tender;

/**
 * Specific tracker for tender (fines/awards)
 * 
 * @author turt2live
 */
public class TenderAmountTracker extends Tracker {

	private Tender tender;

	/**
	 * Creates a new Region Tracker
	 * 
	 * @param name the tracker name
	 * @param type the type
	 * @param tender the tender
	 */
	public TenderAmountTracker(String name, TrackerType type, Tender tender){
		super(name, type);
		this.tender = tender;
	}

	@Override
	public int getValue(){
		return (int) Math.round(tender.getAmount());
	}

	/**
	 * Updates the tender of this tracker
	 * 
	 * @param tender the new tender
	 */
	public void updateTender(Tender tender){
		this.tender = tender;
	}

}
