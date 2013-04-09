package com.turt2live.antishare.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.regex.Pattern;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;

import com.turt2live.antishare.util.Key;

/**
 * Represents a block region file<br>
 * <p>
 * <b>Block File Format:</b><br>
 * | int X | int Y | int Z | byte GAMEMODE |<br>
 * <br>
 * <b>Entity File Format: </b><br>
 * | int X | int Y | int Z | byte GAMEMODE | byte ENTITY TYPE |
 * </p>
 * 
 * @author turt2live
 */
/* TODO: Eventual plans:
 * - Save multiple chunks in a file
 */
public class ASRFile{

	public static final Pattern SPLIT_PATTERN = Pattern.compile(" ");
	public static final byte CREATIVE_BYTE = 0x1;
	public static final byte SURVIVAL_BYTE = 0x2;
	public static final byte ADVENTURE_BYTE = 0x3;
	public static final byte UNKNOWN_OBJECT_BYTE = 0x0;
	public static final byte ITEM_FRAME_BYTE = 0x1;
	public static final byte PAINTING_BYTE = 0x2;
	private FileOutputStream output;
	private FileInputStream input;
	private FileChannel channel;
	private ByteBuffer buffer = null;
	private boolean write = false;

	/**
	 * Creates a new ASRegion file
	 * 
	 * @param isEntity true to make an entity file, false otherwise
	 */
	public ASRFile(boolean isEntity){
		buffer = ByteBuffer.allocateDirect(isEntity ? 14 : 13);
	}

	private byte gamemodeToByte(GameMode gamemode){
		if(gamemode == null){
			return UNKNOWN_OBJECT_BYTE;
		}
		switch (gamemode){
		case CREATIVE:
			return CREATIVE_BYTE;
		case SURVIVAL:
			return SURVIVAL_BYTE;
		case ADVENTURE:
			return ADVENTURE_BYTE;
		default:
			return UNKNOWN_OBJECT_BYTE;
		}
	}

	private GameMode byteToGamemode(byte bite){
		switch (bite){
		case CREATIVE_BYTE:
			return GameMode.CREATIVE;
		case SURVIVAL_BYTE:
			return GameMode.SURVIVAL;
		case ADVENTURE_BYTE:
			return GameMode.ADVENTURE;
		default:
			return null;
		}
	}

	private byte entityToByte(EntityType type){
		if(type == null){
			return UNKNOWN_OBJECT_BYTE;
		}
		switch (type){
		case PAINTING:
			return PAINTING_BYTE;
		case ITEM_FRAME:
			return ITEM_FRAME_BYTE;
		default:
			return UNKNOWN_OBJECT_BYTE;
		}
	}

	private EntityType byteToEntity(byte bite){
		switch (bite){
		case PAINTING_BYTE:
			return EntityType.PAINTING;
		case ITEM_FRAME_BYTE:
			return EntityType.ITEM_FRAME;
		default:
			return null;
		}
	}

	/**
	 * Prepares the file for read or write
	 * 
	 * @param file the file to prepare
	 * @param write true to write to the file, false otherwise
	 * @throws FileNotFoundException thrown if the file is missing
	 */
	public void prepare(File file, boolean write) throws FileNotFoundException{
		if(write){
			output = new FileOutputStream(file, false);
			channel = output.getChannel();
		}else{
			input = new FileInputStream(file);
			channel = input.getChannel();
		}
		this.write = write;
	}

	/**
	 * Writes a block to file
	 * 
	 * @param location the location of the block
	 * @param gamemode the gamemode of the block
	 * @throws IOException thrown if something happens
	 */
	public void write(Location location, GameMode gamemode) throws IOException{
		write(location.getBlockX(), location.getBlockY(), location.getBlockZ(), gamemode);
	}

	/**
	 * Writes a block location to file
	 * 
	 * @param x the x location
	 * @param y the y location
	 * @param z the z location
	 * @param gamemode the gamemode to write
	 * @throws IOException thrown if something happens
	 */
	public void write(int x, int y, int z, GameMode gamemode) throws IOException{
		buffer.clear();
		buffer.putInt(x);
		buffer.putInt(y);
		buffer.putInt(z);
		buffer.put(gamemodeToByte(gamemode));
		buffer.flip();
		channel.write(buffer);
	}

	/**
	 * Writes an entity to file
	 * 
	 * @param location the location of the entity
	 * @param gamemode the gamemode of the entity
	 * @param entity the entity type of the entity
	 * @throws IOException thrown if something happens
	 */
	public void write(Location location, GameMode gamemode, EntityType entity) throws IOException{
		write(location.getBlockX(), location.getBlockY(), location.getBlockZ(), gamemode, entity);
	}

	/**
	 * Writes an entity to file
	 * 
	 * @param x the x location
	 * @param y the y location
	 * @param z the z location
	 * @param gamemode the gamemode of the entity
	 * @param entity the entity type of the entity
	 * @throws IOException thrown if something happens
	 */
	public void write(int x, int y, int z, GameMode gamemode, EntityType entity) throws IOException{
		buffer.clear();
		buffer.putInt(x);
		buffer.putInt(y);
		buffer.putInt(z);
		buffer.put(gamemodeToByte(gamemode));
		buffer.put(entityToByte(entity));
		buffer.flip();
		channel.write(buffer);
	}

	/**
	 * Gets the next block in the file
	 * 
	 * @param world the world for location creation/reading
	 * @return the entry (a block) or null if EOF has been reached / nothing was read
	 * @throws IOException thrown if something happens
	 */
	public Key getNext(World world) throws IOException{
		int read = channel.read(buffer);
		if(read <= 0){
			return null;
		}
		buffer.position(0);
		int x = buffer.getInt(), y = buffer.getInt(), z = buffer.getInt();
		byte bite = buffer.get();
		GameMode value = byteToGamemode(bite);
		buffer.clear();
		return new Key(x, y, z, value);
	}

	/**
	 * Gets the next entity in the file
	 * 
	 * @param world the world for location creation/reading
	 * @return the entry (an entity) or null if EOF has been reached / nothing was read
	 * @throws IOException thrown if something happens
	 */
	public Key getNextEntity(World world) throws IOException{
		int read = channel.read(buffer);
		if(read <= 0){
			return null;
		}
		buffer.position(0);
		int x = buffer.getInt(), y = buffer.getInt(), z = buffer.getInt();
		byte bite = buffer.get(), biteEntity = buffer.get();
		GameMode value = byteToGamemode(bite);
		EntityType entity = byteToEntity(biteEntity);
		buffer.clear();
		return new Key(x, y, z, value, entity);
	}

	/**
	 * Closes the ASRegion, saving it to disk if needed
	 * 
	 * @throws IOException thrown if something goes wrong
	 */
	public void close() throws IOException{
		if(write){
			output.close();
		}else{
			input.close();
		}
	}

}
