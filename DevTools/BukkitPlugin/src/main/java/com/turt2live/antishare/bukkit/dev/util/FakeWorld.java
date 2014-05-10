/*******************************************************************************
 * Copyright (C) 2014 Travis Ralston (turt2live)
 *
 * This software is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package com.turt2live.antishare.bukkit.dev.util;

import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * A fake world
 *
 * @author turt2live
 */
public class FakeWorld implements World {

    private String name;

    public FakeWorld(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    // ==== ---------------------------- ====
    // ====  Start implement of nothing  ====
    // ==== ---------------------------- ====

    @Override
    public Block getBlockAt(int i, int i2, int i3) {
        return null;
    }

    @Override
    public Block getBlockAt(Location location) {
        return null;
    }

    @Override
    public int getBlockTypeIdAt(int i, int i2, int i3) {
        return 0;
    }

    @Override
    public int getBlockTypeIdAt(Location location) {
        return 0;
    }

    @Override
    public int getHighestBlockYAt(int i, int i2) {
        return 0;
    }

    @Override
    public int getHighestBlockYAt(Location location) {
        return 0;
    }

    @Override
    public Block getHighestBlockAt(int i, int i2) {
        return null;
    }

    @Override
    public Block getHighestBlockAt(Location location) {
        return null;
    }

    @Override
    public Chunk getChunkAt(int i, int i2) {
        return null;
    }

    @Override
    public Chunk getChunkAt(Location location) {
        return null;
    }

    @Override
    public Chunk getChunkAt(Block block) {
        return null;
    }

    @Override
    public boolean isChunkLoaded(Chunk chunk) {
        return false;
    }

    @Override
    public Chunk[] getLoadedChunks() {
        return new Chunk[0];
    }

    @Override
    public void loadChunk(Chunk chunk) {

    }

    @Override
    public boolean isChunkLoaded(int i, int i2) {
        return false;
    }

    @Override
    public boolean isChunkInUse(int i, int i2) {
        return false;
    }

    @Override
    public void loadChunk(int i, int i2) {

    }

    @Override
    public boolean loadChunk(int i, int i2, boolean b) {
        return false;
    }

    @Override
    public boolean unloadChunk(Chunk chunk) {
        return false;
    }

    @Override
    public boolean unloadChunk(int i, int i2) {
        return false;
    }

    @Override
    public boolean unloadChunk(int i, int i2, boolean b) {
        return false;
    }

    @Override
    public boolean unloadChunk(int i, int i2, boolean b, boolean b2) {
        return false;
    }

    @Override
    public boolean unloadChunkRequest(int i, int i2) {
        return false;
    }

    @Override
    public boolean unloadChunkRequest(int i, int i2, boolean b) {
        return false;
    }

    @Override
    public boolean regenerateChunk(int i, int i2) {
        return false;
    }

    @Override
    public boolean refreshChunk(int i, int i2) {
        return false;
    }

    @Override
    public Item dropItem(Location location, ItemStack itemStack) {
        return null;
    }

    @Override
    public Item dropItemNaturally(Location location, ItemStack itemStack) {
        return null;
    }

    @Override
    public Arrow spawnArrow(Location location, Vector vector, float v, float v2) {
        return null;
    }

    @Override
    public boolean generateTree(Location location, TreeType treeType) {
        return false;
    }

    @Override
    public boolean generateTree(Location location, TreeType treeType, BlockChangeDelegate blockChangeDelegate) {
        return false;
    }

    @Override
    public Entity spawnEntity(Location location, EntityType entityType) {
        return null;
    }

    @Override
    public LivingEntity spawnCreature(Location location, EntityType entityType) {
        return null;
    }

    @Override
    public LivingEntity spawnCreature(Location location, CreatureType creatureType) {
        return null;
    }

    @Override
    public LightningStrike strikeLightning(Location location) {
        return null;
    }

    @Override
    public LightningStrike strikeLightningEffect(Location location) {
        return null;
    }

    @Override
    public List<Entity> getEntities() {
        return null;
    }

    @Override
    public List<LivingEntity> getLivingEntities() {
        return null;
    }

    @Override
    public <T extends Entity> Collection<T> getEntitiesByClass(Class<T>... classes) {
        return null;
    }

    @Override
    public <T extends Entity> Collection<T> getEntitiesByClass(Class<T> tClass) {
        return null;
    }

    @Override
    public Collection<Entity> getEntitiesByClasses(Class<?>... classes) {
        return null;
    }

    @Override
    public List<Player> getPlayers() {
        return null;
    }

    @Override
    public UUID getUID() {
        return null;
    }

    @Override
    public Location getSpawnLocation() {
        return null;
    }

    @Override
    public boolean setSpawnLocation(int i, int i2, int i3) {
        return false;
    }

    @Override
    public long getTime() {
        return 0;
    }

    @Override
    public void setTime(long l) {

    }

    @Override
    public long getFullTime() {
        return 0;
    }

    @Override
    public void setFullTime(long l) {

    }

    @Override
    public boolean hasStorm() {
        return false;
    }

    @Override
    public void setStorm(boolean b) {

    }

    @Override
    public int getWeatherDuration() {
        return 0;
    }

    @Override
    public void setWeatherDuration(int i) {

    }

    @Override
    public boolean isThundering() {
        return false;
    }

    @Override
    public void setThundering(boolean b) {

    }

    @Override
    public int getThunderDuration() {
        return 0;
    }

    @Override
    public void setThunderDuration(int i) {

    }

    @Override
    public boolean createExplosion(double v, double v2, double v3, float v4) {
        return false;
    }

    @Override
    public boolean createExplosion(double v, double v2, double v3, float v4, boolean b) {
        return false;
    }

    @Override
    public boolean createExplosion(double v, double v2, double v3, float v4, boolean b, boolean b2) {
        return false;
    }

    @Override
    public boolean createExplosion(Location location, float v) {
        return false;
    }

    @Override
    public boolean createExplosion(Location location, float v, boolean b) {
        return false;
    }

    @Override
    public Environment getEnvironment() {
        return null;
    }

    @Override
    public long getSeed() {
        return 0;
    }

    @Override
    public boolean getPVP() {
        return false;
    }

    @Override
    public void setPVP(boolean b) {

    }

    @Override
    public ChunkGenerator getGenerator() {
        return null;
    }

    @Override
    public void save() {

    }

    @Override
    public List<BlockPopulator> getPopulators() {
        return null;
    }

    @Override
    public <T extends Entity> T spawn(Location location, Class<T> tClass) throws IllegalArgumentException {
        return null;
    }

    @Override
    public FallingBlock spawnFallingBlock(Location location, Material material, byte b) throws IllegalArgumentException {
        return null;
    }

    @Override
    public FallingBlock spawnFallingBlock(Location location, int i, byte b) throws IllegalArgumentException {
        return null;
    }

    @Override
    public void playEffect(Location location, Effect effect, int i) {

    }

    @Override
    public void playEffect(Location location, Effect effect, int i, int i2) {

    }

    @Override
    public <T> void playEffect(Location location, Effect effect, T t) {

    }

    @Override
    public <T> void playEffect(Location location, Effect effect, T t, int i) {

    }

    @Override
    public ChunkSnapshot getEmptyChunkSnapshot(int i, int i2, boolean b, boolean b2) {
        return null;
    }

    @Override
    public void setSpawnFlags(boolean b, boolean b2) {

    }

    @Override
    public boolean getAllowAnimals() {
        return false;
    }

    @Override
    public boolean getAllowMonsters() {
        return false;
    }

    @Override
    public Biome getBiome(int i, int i2) {
        return null;
    }

    @Override
    public void setBiome(int i, int i2, Biome biome) {

    }

    @Override
    public double getTemperature(int i, int i2) {
        return 0;
    }

    @Override
    public double getHumidity(int i, int i2) {
        return 0;
    }

    @Override
    public int getMaxHeight() {
        return 0;
    }

    @Override
    public int getSeaLevel() {
        return 0;
    }

    @Override
    public boolean getKeepSpawnInMemory() {
        return false;
    }

    @Override
    public void setKeepSpawnInMemory(boolean b) {

    }

    @Override
    public boolean isAutoSave() {
        return false;
    }

    @Override
    public void setAutoSave(boolean b) {

    }

    @Override
    public void setDifficulty(Difficulty difficulty) {

    }

    @Override
    public Difficulty getDifficulty() {
        return null;
    }

    @Override
    public File getWorldFolder() {
        return null;
    }

    @Override
    public WorldType getWorldType() {
        return null;
    }

    @Override
    public boolean canGenerateStructures() {
        return false;
    }

    @Override
    public long getTicksPerAnimalSpawns() {
        return 0;
    }

    @Override
    public void setTicksPerAnimalSpawns(int i) {

    }

    @Override
    public long getTicksPerMonsterSpawns() {
        return 0;
    }

    @Override
    public void setTicksPerMonsterSpawns(int i) {

    }

    @Override
    public int getMonsterSpawnLimit() {
        return 0;
    }

    @Override
    public void setMonsterSpawnLimit(int i) {

    }

    @Override
    public int getAnimalSpawnLimit() {
        return 0;
    }

    @Override
    public void setAnimalSpawnLimit(int i) {

    }

    @Override
    public int getWaterAnimalSpawnLimit() {
        return 0;
    }

    @Override
    public void setWaterAnimalSpawnLimit(int i) {

    }

    @Override
    public int getAmbientSpawnLimit() {
        return 0;
    }

    @Override
    public void setAmbientSpawnLimit(int i) {

    }

    @Override
    public void playSound(Location location, Sound sound, float v, float v2) {

    }

    @Override
    public String[] getGameRules() {
        return new String[0];
    }

    @Override
    public String getGameRuleValue(String s) {
        return null;
    }

    @Override
    public boolean setGameRuleValue(String s, String s2) {
        return false;
    }

    @Override
    public boolean isGameRule(String s) {
        return false;
    }

    @Override
    public void setMetadata(String s, MetadataValue metadataValue) {

    }

    @Override
    public List<MetadataValue> getMetadata(String s) {
        return null;
    }

    @Override
    public boolean hasMetadata(String s) {
        return false;
    }

    @Override
    public void removeMetadata(String s, Plugin plugin) {

    }

    @Override
    public void sendPluginMessage(Plugin plugin, String s, byte[] bytes) {

    }

    @Override
    public Set<String> getListeningPluginChannels() {
        return null;
    }
}
