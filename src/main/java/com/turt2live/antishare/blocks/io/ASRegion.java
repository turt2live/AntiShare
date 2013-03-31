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

// TODO: Document
public class ASRegion{

	public static class BlockInfo{
		public final Location location;
		public final GameMode gamemode;
		public final byte raw;

		private BlockInfo(Location location, GameMode gamemode, byte raw){
			this.location = location;
			this.gamemode = gamemode;
			this.raw = raw;
		}
	}

	public static final Pattern pattern = Pattern.compile(" ");
	public static final byte CREATIVE_BYTE = 0x1;
	public static final byte SURVIVAL_BYTE = 0x2;
	public static final byte ADVENTURE_BYTE = 0x3;
	public static final byte NO_GAMEMODE_BYTE = 0x0;
	private FileOutputStream output;
	private FileInputStream input;
	private FileChannel channel;
	private ByteBuffer buffer = ByteBuffer.allocateDirect(13);
	private boolean write = false;

	private byte gamemodeToByte(GameMode gamemode){
		if(gamemode == null){
			return 0;
		}
		switch (gamemode){
		case CREATIVE:
			return CREATIVE_BYTE;
		case SURVIVAL:
			return SURVIVAL_BYTE;
		case ADVENTURE:
			return ADVENTURE_BYTE;
		default:
			return NO_GAMEMODE_BYTE;
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
		return new BlockInfo(new Location(world, x, y, z), value, bite);
	}

	public void close() throws IOException{
		if(write){
			output.close();
		}else{
			input.close();
		}
	}

}
