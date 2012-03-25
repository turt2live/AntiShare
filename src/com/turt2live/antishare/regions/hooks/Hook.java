package com.turt2live.antishare.regions.hooks;

import com.turt2live.antishare.regions.Selection;

public interface Hook {

	public boolean hasRegion(Selection location);

	public boolean exists();

	public String getName();
}
