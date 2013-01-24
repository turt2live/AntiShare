package com.turt2live.antishare.test;

import java.util.List;
import java.util.UUID;

import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class CraftWolf implements Entity {

	@Override
	public void setMetadata(String metadataKey, MetadataValue newMetadataValue){
		throw new UnsupportedOperationException("Invalid class use");
	}

	@Override
	public List<MetadataValue> getMetadata(String metadataKey){
		throw new UnsupportedOperationException("Invalid class use");
	}

	@Override
	public boolean hasMetadata(String metadataKey){
		throw new UnsupportedOperationException("Invalid class use");
	}

	@Override
	public void removeMetadata(String metadataKey, Plugin owningPlugin){
		throw new UnsupportedOperationException("Invalid class use");
	}

	@Override
	public Location getLocation(){
		throw new UnsupportedOperationException("Invalid class use");
	}

	@Override
	public Location getLocation(Location loc){
		throw new UnsupportedOperationException("Invalid class use");
	}

	@Override
	public void setVelocity(Vector velocity){
		throw new UnsupportedOperationException("Invalid class use");
	}

	@Override
	public Vector getVelocity(){
		throw new UnsupportedOperationException("Invalid class use");
	}

	@Override
	public World getWorld(){
		throw new UnsupportedOperationException("Invalid class use");
	}

	@Override
	public boolean teleport(Location location){
		throw new UnsupportedOperationException("Invalid class use");
	}

	@Override
	public boolean teleport(Location location, TeleportCause cause){
		throw new UnsupportedOperationException("Invalid class use");
	}

	@Override
	public boolean teleport(Entity destination){
		throw new UnsupportedOperationException("Invalid class use");
	}

	@Override
	public boolean teleport(Entity destination, TeleportCause cause){
		throw new UnsupportedOperationException("Invalid class use");
	}

	@Override
	public List<Entity> getNearbyEntities(double x, double y, double z){
		throw new UnsupportedOperationException("Invalid class use");
	}

	@Override
	public int getEntityId(){
		throw new UnsupportedOperationException("Invalid class use");
	}

	@Override
	public int getFireTicks(){
		throw new UnsupportedOperationException("Invalid class use");
	}

	@Override
	public int getMaxFireTicks(){
		throw new UnsupportedOperationException("Invalid class use");
	}

	@Override
	public void setFireTicks(int ticks){
		throw new UnsupportedOperationException("Invalid class use");
	}

	@Override
	public void remove(){
		throw new UnsupportedOperationException("Invalid class use");
	}

	@Override
	public boolean isDead(){
		throw new UnsupportedOperationException("Invalid class use");
	}

	@Override
	public boolean isValid(){
		throw new UnsupportedOperationException("Invalid class use");
	}

	@Override
	public Server getServer(){
		throw new UnsupportedOperationException("Invalid class use");
	}

	@Override
	public Entity getPassenger(){
		throw new UnsupportedOperationException("Invalid class use");
	}

	@Override
	public boolean setPassenger(Entity passenger){
		throw new UnsupportedOperationException("Invalid class use");
	}

	@Override
	public boolean isEmpty(){
		throw new UnsupportedOperationException("Invalid class use");
	}

	@Override
	public boolean eject(){
		throw new UnsupportedOperationException("Invalid class use");
	}

	@Override
	public float getFallDistance(){
		throw new UnsupportedOperationException("Invalid class use");
	}

	@Override
	public void setFallDistance(float distance){
		throw new UnsupportedOperationException("Invalid class use");
	}

	@Override
	public void setLastDamageCause(EntityDamageEvent event){
		throw new UnsupportedOperationException("Invalid class use");
	}

	@Override
	public EntityDamageEvent getLastDamageCause(){
		throw new UnsupportedOperationException("Invalid class use");
	}

	@Override
	public UUID getUniqueId(){
		throw new UnsupportedOperationException("Invalid class use");
	}

	@Override
	public int getTicksLived(){
		throw new UnsupportedOperationException("Invalid class use");
	}

	@Override
	public void setTicksLived(int value){
		throw new UnsupportedOperationException("Invalid class use");
	}

	@Override
	public void playEffect(EntityEffect type){
		throw new UnsupportedOperationException("Invalid class use");
	}

	@Override
	public EntityType getType(){
		throw new UnsupportedOperationException("Invalid class use");
	}

	@Override
	public boolean isInsideVehicle(){
		throw new UnsupportedOperationException("Invalid class use");
	}

	@Override
	public boolean leaveVehicle(){
		throw new UnsupportedOperationException("Invalid class use");
	}

	@Override
	public Entity getVehicle(){
		throw new UnsupportedOperationException("Invalid class use");
	}

}
