package com.turt2live.antishare.bukkit.lists;

import com.turt2live.antishare.ABlock;
import com.turt2live.antishare.TrackedState;
import com.turt2live.antishare.bukkit.MaterialProvider;
import com.turt2live.antishare.bukkit.impl.BukkitBlock;
import com.turt2live.antishare.engine.BlockTypeList;
import com.turt2live.antishare.engine.RejectionList;
import org.bukkit.block.Block;

import java.util.List;

public class BukkitBlockList extends GenericBlockInformationList implements BlockTypeList, RejectionList {

    private ListType type = ListType.CUSTOM;

    public BukkitBlockList(MaterialProvider provider) {
        this(provider,null);
    }

    public BukkitBlockList(MaterialProvider provider, ListType type) {
        super(provider);
        if(type==null)type = ListType.CUSTOM;
        this.type = type;
    }

    @Override
    public boolean isTracked(ABlock block) {
        return getState(block) == TrackedState.INCLUDED;
    }

    @Override
    public boolean isBlocked(ABlock block) {
        return isTracked(block);
    }

    @Override
    public TrackedState getState(ABlock ablock) {
        if (ablock == null || !(ablock instanceof BukkitBlock)) return TrackedState.NOT_PRESENT;
        Block block = ((BukkitBlock) ablock).getBlock();
        BlockInformation highest = null;
        TrackedState state = TrackedState.NOT_PRESENT;

        List<BlockInformation> informations = generateBlockInfo(block);
        for (BlockInformation information : informations) {
            if (highest != null && state != TrackedState.NOT_PRESENT) {
                if (highest.isHigher(information)) continue; // Highest is still superior
            }
            TrackedState other = getState(information);
            highest = information;
            state = other;
        }

        return state;
    }

    @Override
    public ListType getType() {
        return type;
    }

}
