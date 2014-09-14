/*******************************************************************************
 * Copyright (C) 2014 Travis Ralston (turt2live)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package com.turt2live.antishare.io.flatfile;

import com.turt2live.antishare.engine.Engine;
import com.turt2live.antishare.io.BlockStore;
import com.turt2live.antishare.object.ASLocation;
import com.turt2live.antishare.object.attribute.ObjectType;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Converts files of one block size to another block size
 *
 * @author turt2live
 */
// TODO: Unit test
public class FileBlockSizeConverter {

    private FileBlockManager managerFrom, managerTo;
    private boolean saveOnExit = true;

    /**
     * Creates a new FileBlockSizeConverter. This takes the passed "from size" value and searches the
     * "from folder" for files with a matching block size. If a matching block size is found, the data
     * is imported to the "to folder" using the "to size" as a flag. If the data in the destination will
     * be overwritten by what is in the from data, the to data will win and a message will be printed to the
     * Engine's default logger. The "save" flag is used to save the data once importing has been completed. If
     * false, it is assumed manual handling of an implementing FileBlockSizeConverter is done.
     *
     * @param fromSize   the size to go from, cannot be the same as toSize and must be positive
     * @param toSize     the size to go to, cannot be the same as fromSize and must be positive
     * @param fromFolder the folder to search, must exist and cannot be null
     * @param toFolder   the folder to import to, must exist and cannot be null
     * @param save       true to save data after importing
     */
    public FileBlockSizeConverter(int fromSize, int toSize, File fromFolder, File toFolder, boolean save) {
        if (fromSize <= 0 || toSize <= 0 || toSize == fromSize || fromFolder == null || toFolder == null
                || !fromFolder.exists() || !toFolder.exists() || !fromFolder.isDirectory() || !toFolder.isDirectory())
            throw new IllegalArgumentException();

        this.saveOnExit = save;
        this.managerFrom = new FileBlockManager(fromSize, fromFolder);
        this.managerTo = new FileBlockManager(toSize, toFolder);
    }

    /**
     * Starts the conversion on the current thread
     *
     * @return the number of locations converted
     */
    public long doConversion() {
        long conversions = 0;
        List<BlockStore> stores = managerFrom.loadAll();

        for (BlockStore store : stores) {
            Map<ASLocation, ObjectType> types = store.getAll();
            for (Map.Entry<ASLocation, ObjectType> entry : types.entrySet()) {
                ASLocation location = entry.getKey();
                ObjectType type = entry.getValue();

                ObjectType existing = managerTo.getBlockType(location);
                if (existing != type && existing != ObjectType.UNKNOWN) {
                    Engine.getInstance().getLogger().warning("Duplicate found at " + location.toString() + ", ignoring");
                } else {
                    managerTo.setBlockType(location, type);
                    conversions++;
                }
            }
        }

        if (saveOnExit) {
            managerTo.saveAll();
        }

        return conversions;
    }

}
