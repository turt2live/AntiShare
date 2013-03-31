package com.turt2live.antishare.blocks.io;

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

// TODO: Document
public class ASRegion{

	public static class BlockInfo{
		public final Location location;
		public final GameMode gamemode;
		public final byte rawGM, rawEntity;
		public final EntityType entity;

		private BlockInfo(Location location, GameMode gamemode, EntityType type, byte raw, byte rawEntity){
			this.location = location;
			this.gamemode = gamemode;
			this.rawGM = raw;
			this.rawEntity = rawEntity;
			this.entity = type;
		}
	}

	public static final Pattern pattern = Pattern.compile(" ");
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

	public ASRegion(boolean isEntity){
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

	public void write(Location location, GameMode gamemode) throws IOException{
		buffer.clear();
		buffer.putInt(location.getBlockX());
		buffer.putInt(location.getBlockY());
		buffer.putInt(location.getBlockZ());
		buffer.put(gamemodeToByte(gamemode));
		buffer.flip();
		channel.write(buffer);
	}

	public void write(Location location, GameMode gamemode, EntityType entity) throws IOException{
		buffer.clear();
		buffer.putInt(location.getBlockX());
		buffer.putInt(location.getBlockY());
		buffer.putInt(location.getBlockZ());
		buffer.put(gamemodeToByte(gamemode));
		buffer.put(entityToByte(entity));
		buffer.flip();
		channel.write(buffer);
	}

	public BlockInfo getNext(World world) throws IOException{
		int read = channel.read(buffer);
		if(read <= 0){
			return null;
		}
		buffer.position(0);
		int x = buffer.getInt(), y = buffer.getInt(), z = buffer.getInt();
		byte bite = buffer.get();
		GameMode value = byteToGamemode(bite);
		buffer.clear();
		return new BlockInfo(new Location(world, x, y, z), value, null, bite, (byte) 0x0);
	}

	public BlockInfo getNextEntity(World world) throws IOException{
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
		return new BlockInfo(new Location(world, x, y, z), value, entity, bite, biteEntity);
	}

	public void close() throws IOException{
		if(write){
			output.close();
		}else{
			input.close();
		}
	}

}
