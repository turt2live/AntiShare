package com.turt2live.antishare.testobjects;

import com.turt2live.antishare.inventory.ASItem;

public class UnitASItem extends ASItem {

    private int id;

    public UnitASItem(int id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof UnitASItem)) return false;
        return ((UnitASItem) o).id == this.id;
    }

    @Override
    public ASItem clone() {
        return new UnitASItem(this.id);
    }

    public int getId() {
        return id;
    }
}
