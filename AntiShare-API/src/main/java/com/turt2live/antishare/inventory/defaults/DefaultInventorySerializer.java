package com.turt2live.antishare.inventory.defaults;

import com.turt2live.antishare.inventory.ASItem;
import com.turt2live.antishare.inventory.InventorySerializer;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

/**
 * Default inventory serializer. Only parses {@link com.turt2live.antishare.inventory.defaults.DefaultASItem} items
 *
 * @author turt2live
 */
public class DefaultInventorySerializer implements InventorySerializer {

    @Override
    public String toJson(ASItem item) {
        if (item == null || !(item instanceof DefaultASItem))
            throw new IllegalArgumentException("item cannot be null and must be a DefaultASItem");
        DefaultASItem i = (DefaultASItem) item;
        JSONObject json = new JSONObject();
        json.put("id", i.getId());
        return json.toJSONString();
    }

    @Override
    public ASItem fromJson(String json) {
        if (json == null) throw new IllegalArgumentException("JSON cannot be null");
        Object o = JSONValue.parse(json);
        if (o == null || !(o instanceof JSONObject)) throw new IllegalArgumentException("Invalid JSON");

        JSONObject result = (JSONObject) o;
        Object id = result.get("id");
        if (id != null && id instanceof Integer) {
            return new DefaultASItem((Integer) id);
        }

        throw new IllegalArgumentException("Invalid JSON object");
    }
}
