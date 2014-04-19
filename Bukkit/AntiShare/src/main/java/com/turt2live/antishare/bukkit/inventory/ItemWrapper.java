package com.turt2live.antishare.bukkit.inventory;

import com.turt2live.antishare.inventory.ASItem;
import org.bukkit.inventory.ItemStack;

/**
 * Item wrapper for Bukkit items
 *
 * @author turt2live
 */
public class ItemWrapper extends ASItem {

    private ItemStack stack;

    /**
     * Creates a new item wrapper
     *
     * @param stack the stack to wrap, cannot be null
     */
    public ItemWrapper(ItemStack stack) {
        if (stack == null) throw new IllegalArgumentException("stack cannot be null");

        this.stack = stack;
    }

    /**
     * Gets the item stack represented by this wrapper
     *
     * @return the item stack
     */
    public ItemStack getStack() {
        return stack;
    }

    @Override
    public int hashCode() {
        return stack.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof ItemWrapper)) return false;

        ItemWrapper that = (ItemWrapper) o;

        if (!stack.equals(that.stack)) return false;

        return true;
    }

    @Override
    public ItemWrapper clone() {
        return new ItemWrapper(stack.clone());
    }
}
