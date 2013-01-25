package com.turt2live.antishare.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.material.Bed;
import org.bukkit.material.Door;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.turt2live.antishare.test.CraftWolf;
import com.turt2live.antishare.util.ASUtils.EntityPattern;
import com.turt2live.antishare.util.generic.MobPattern;

@SuppressWarnings ("deprecation")
@RunWith (PowerMockRunner.class)
@PrepareForTest ({Bukkit.class})
public class TestASUtils {

	private File testDirectory = new File("testingdirectory");
	private List<String> knownFiles = new ArrayList<String>();
	private CopyOnWriteArrayList<String> selectiveNames = new CopyOnWriteArrayList<String>();
	private List<String> commas = new ArrayList<String>();
	private Map<String, String> names = new HashMap<String, String>();
	private Player player;
	private Player players[] = new Player[5];
	private Entity wolf = new CraftWolf();
	private Block stoneBlock, woodBlock, doorTop, doorBottom, bedHead, bedFoot, doorRel1, doorRel2, bedRel1, bedRel2;

	@Before
	// Runs before EACH test
	public void setUp() throws IOException{
		testDirectory.mkdirs();
		for(int i = 0; i < 10; i++){
			File file = new File(testDirectory, "file" + i + ".txt");
			file.createNewFile();
			knownFiles.add(file.getName());
			File folder = new File(testDirectory, "folder" + i);
			folder.mkdirs();
			knownFiles.add(folder.getName());
			for(int x = 0; x < 10; x++){
				file = new File(folder, "file" + x + ".txt");
				file.createNewFile();
			}
		}

		// Initial names
		selectiveNames.add("file1.txt");
		selectiveNames.add("fileX.txt");

		// Commas
		for(int i = 0; i < 3; i++){
			commas.add("c" + i);
		}

		// Names
		names.put("blaze", "blaze");
		names.put("cavespider", "cave spider");
		names.put("cave spider", "cave spider");
		names.put("chicken", "chicken");
		names.put("cow", "cow");
		names.put("creeper", "creeper");
		names.put("enderdragon", "ender dragon");
		names.put("ender dragon", "ender dragon");
		names.put("enderman", "enderman");
		names.put("ghast", "ghast");
		names.put("giant", "giant");
		names.put("irongolem", "iron golem");
		names.put("iron golem", "iron golem");
		names.put("mushroomcow", "mooshroom");
		names.put("mushroom cow", "mooshroom");
		names.put("mooshroom", "mooshroom");
		names.put("ocelot", "ocelot");
		names.put("cat", "ocelot");
		names.put("pig", "pig");
		names.put("pigzombie", "pigman");
		names.put("zombiepigman", "pigman");
		names.put("pig zombie", "pigman");
		names.put("zombie pigman", "pigman");
		names.put("pigman", "pigman");
		names.put("sheep", "sheep");
		names.put("silverfish", "silverfish");
		names.put("skeleton", "skeleton");
		names.put("slime", "slime");
		names.put("magmacube", "magma cube");
		names.put("magma cube", "magma cube");
		names.put("spider", "spider");
		names.put("snowman", "snowman");
		names.put("squid", "squid");
		names.put("villager", "villager");
		names.put("testificate", "villager");
		names.put("wolf", "wolf");
		names.put("zombie", "zombie");
		names.put("witch", "witch");
		names.put("wither", "wither boss");
		names.put("witherboss", "wither boss");
		names.put("wither boss", "wither boss");
		names.put("bat", "bat");

		// Players
		player = mock(Player.class);
		for(int i = 0; i < players.length; i++){
			Player p = mock(Player.class);
			GameMode gm = GameMode.CREATIVE;
			if(i > 2 && i < 4){
				gm = GameMode.SURVIVAL;
			}else if(i >= 4){
				gm = GameMode.ADVENTURE;
			}
			when(p.getGameMode()).thenReturn(gm);
			players[i] = p;
		}

		// Class setup
		PowerMockito.mockStatic(Bukkit.class);
		when(Bukkit.getOnlinePlayers()).thenReturn(players);
		stoneBlock = mock(Block.class);
		woodBlock = mock(Block.class);
		when(stoneBlock.getType()).thenReturn(Material.STONE);
		when(stoneBlock.getData()).thenReturn((byte) 0);
		when(woodBlock.getType()).thenReturn(Material.WOOD);
		when(woodBlock.getData()).thenReturn((byte) 3);
		when(stoneBlock.getTypeId()).thenReturn(1);
		when(woodBlock.getTypeId()).thenReturn(5);

		// Setup beds/doors
		doorTop = mock(Block.class);
		doorBottom = mock(Block.class);
		bedHead = mock(Block.class);
		bedFoot = mock(Block.class);
		doorRel1 = mock(Block.class);
		bedRel1 = mock(Block.class);
		doorRel2 = mock(Block.class);
		bedRel2 = mock(Block.class);
		when(doorTop.getType()).thenReturn(Material.IRON_DOOR_BLOCK);
		when(doorBottom.getType()).thenReturn(Material.IRON_DOOR_BLOCK);
		when(bedHead.getType()).thenReturn(Material.BED_BLOCK);
		when(bedFoot.getType()).thenReturn(Material.BED_BLOCK);
		when(doorRel1.getType()).thenReturn(Material.STONE);
		when(doorRel2.getType()).thenReturn(Material.WOOD);
		when(bedRel1.getType()).thenReturn(Material.FIRE);
		when(bedRel2.getType()).thenReturn(Material.ANVIL);
		BlockState bs1 = mock(BlockState.class), bs2 = mock(BlockState.class), bs3 = mock(BlockState.class), bs4 = mock(BlockState.class);
		Door door1 = mock(Door.class), door2 = mock(Door.class);
		Bed bed1 = mock(Bed.class), bed2 = mock(Bed.class);
		when(door1.isTopHalf()).thenReturn(true);
		when(door2.isTopHalf()).thenReturn(false);
		when(bed1.isHeadOfBed()).thenReturn(true);
		when(bed2.isHeadOfBed()).thenReturn(false);
		when(bed1.getFacing()).thenReturn(BlockFace.EAST);
		when(bed2.getFacing()).thenReturn(BlockFace.EAST);
		when(bs1.getData()).thenReturn(door1);
		when(bs2.getData()).thenReturn(door2);
		when(bs3.getData()).thenReturn(bed1);
		when(bs4.getData()).thenReturn(bed2);
		when(doorTop.getState()).thenReturn(bs1);
		when(doorBottom.getState()).thenReturn(bs2);
		when(bedFoot.getState()).thenReturn(bs4);
		when(bedHead.getState()).thenReturn(bs3);
		when(doorTop.getRelative(BlockFace.DOWN)).thenReturn(doorRel1);
		when(doorBottom.getRelative(BlockFace.UP)).thenReturn(doorRel2);
		when(bedHead.getRelative(BlockFace.WEST)).thenReturn(bedRel1);
		when(bedFoot.getRelative(BlockFace.EAST)).thenReturn(bedRel2);
	}

	@After
	// Runs after EACH test
	public void tearDown(){
		ASUtils.wipeFolder(testDirectory, null);
	}

	@Test
	public void testSendToPlayer(){
		ASUtils.sendToPlayer(player, "no message", false);
		verify(player, never()).sendMessage(anyString());
		ASUtils.sendToPlayer(player, "test message", false);
		verify(player, times(1)).sendMessage(anyString());
	}

	@Test
	public void testGetBoolean(){
		// True
		assertTrue(ASUtils.getBoolean("true"));
		assertTrue(ASUtils.getBoolean("t"));
		assertTrue(ASUtils.getBoolean("on"));
		assertTrue(ASUtils.getBoolean("active"));
		assertTrue(ASUtils.getBoolean("1"));

		// False
		assertFalse(ASUtils.getBoolean("false"));
		assertFalse(ASUtils.getBoolean("f"));
		assertFalse(ASUtils.getBoolean("off"));
		assertFalse(ASUtils.getBoolean("inactive"));
		assertFalse(ASUtils.getBoolean("0"));

		// Invalid
		assertNull(ASUtils.getBoolean("thisShouldBeNull"));
		assertNull(ASUtils.getBoolean("not-a-boolean"));
		assertNull(ASUtils.getBoolean(null));
		assertNull(ASUtils.getBoolean(""));
		assertNull(ASUtils.getBoolean(" "));
		assertNull(ASUtils.getBoolean("		")); // Has a tab character in it

		// Case insensitivity
		assertTrue(ASUtils.getBoolean("TrUe"));
		assertFalse(ASUtils.getBoolean("FaLsE"));
		assertNull(ASUtils.getBoolean("NullValue"));
	}

	@Test
	public void testGetGameMode(){
		// Creative
		assertEquals(GameMode.CREATIVE, ASUtils.getGameMode("creative"));
		assertEquals(GameMode.CREATIVE, ASUtils.getGameMode("c"));
		assertEquals(GameMode.CREATIVE, ASUtils.getGameMode("1"));

		// Survival
		assertEquals(GameMode.SURVIVAL, ASUtils.getGameMode("survival"));
		assertEquals(GameMode.SURVIVAL, ASUtils.getGameMode("s"));
		assertEquals(GameMode.SURVIVAL, ASUtils.getGameMode("0"));

		// Adventure
		assertEquals(GameMode.ADVENTURE, ASUtils.getGameMode("adventure"));
		assertEquals(GameMode.ADVENTURE, ASUtils.getGameMode("a"));
		assertEquals(GameMode.ADVENTURE, ASUtils.getGameMode("2"));

		// Invalid
		assertNull(ASUtils.getGameMode("NotGameMode"));
		assertNull(ASUtils.getGameMode("  "));
		assertNull(ASUtils.getGameMode("		")); // Has tab
		assertNull(ASUtils.getGameMode(null));

		// Case insensitivity
		assertEquals(GameMode.CREATIVE, ASUtils.getGameMode("CreaTIve"));
		assertEquals(GameMode.SURVIVAL, ASUtils.getGameMode("SurviVAL"));
		assertEquals(GameMode.ADVENTURE, ASUtils.getGameMode("ADVenTUre"));
	}

	@Test
	public void testBlockToString(){
		assertEquals("1", ASUtils.blockToString(stoneBlock, true));
		assertEquals("1:0", ASUtils.blockToString(stoneBlock, false));
		assertEquals("5:3", ASUtils.blockToString(woodBlock, true));
		assertEquals("5:3", ASUtils.blockToString(woodBlock, false));
	}

	@Test
	public void testMaterialToString(){
		assertEquals("1:*", ASUtils.materialToString(Material.STONE, false));
		assertEquals("1", ASUtils.materialToString(Material.STONE, true));
		assertNull(ASUtils.materialToString(null, false));
		assertNull(ASUtils.materialToString(null, true));
	}

	@Test
	public void testGetWool(){
		assertEquals("35:1", ASUtils.getWool("orange wool"));
		assertEquals("35:0", ASUtils.getWool("white wool"));
		assertEquals("35:2", ASUtils.getWool("magenta wool"));
		assertEquals("35:3", ASUtils.getWool("light_blue wool"));
		assertEquals("35:3", ASUtils.getWool("light blue wool"));
		assertEquals("35:4", ASUtils.getWool("yellow wool"));
		assertEquals("35:5", ASUtils.getWool("lime wool"));
		assertEquals("35:6", ASUtils.getWool("pink wool"));
		assertEquals("35:7", ASUtils.getWool("gray wool"));
		assertEquals("35:8", ASUtils.getWool("light_gray wool"));
		assertEquals("35:8", ASUtils.getWool("light gray wool"));
		assertEquals("35:9", ASUtils.getWool("cyan wool"));
		assertEquals("35:10", ASUtils.getWool("purple wool"));
		assertEquals("35:11", ASUtils.getWool("blue wool"));
		assertEquals("35:12", ASUtils.getWool("brown wool"));
		assertEquals("35:13", ASUtils.getWool("green wool"));
		assertEquals("35:14", ASUtils.getWool("red wool"));
		assertEquals("35:15", ASUtils.getWool("black wool"));
		assertEquals("35:notacolor", ASUtils.getWool("notacolor wool"));
		assertEquals("35:0", ASUtils.getWool("wool"));
		assertNull(ASUtils.getWool("notAColor"));
		assertNull(ASUtils.getWool(null));
	}

	@Test
	public void testGetEntityNameFromEntity(){
		assertEquals("wolf", ASUtils.getEntityName(wolf));
		assertNull(ASUtils.getEntityName((Entity) null));
	}

	@Test
	public void testGetEntityNameFromString(){
		for(String test : names.keySet()){
			String proper = names.get(test);
			assertEquals(proper, ASUtils.getEntityName(test));
			assertEquals(proper, ASUtils.getEntityName(test.toUpperCase()));
			assertEquals(proper, ASUtils.getEntityName(proper));
			assertEquals(proper, ASUtils.getEntityName(proper.toUpperCase()));
		}
		assertNull(ASUtils.getEntityName("NotAMob"));
		assertNull(ASUtils.getEntityName((String) null));
	}

	@Test
	public void testAllEntities(){
		assertTrue(ASUtils.allEntities().size() == 42);
	}

	@Test
	public void testFindGameModePlayers(){
		List<String> creative = ASUtils.findGameModePlayers(GameMode.CREATIVE); // 3
		List<String> survival = ASUtils.findGameModePlayers(GameMode.SURVIVAL); // 1
		List<String> adventure = ASUtils.findGameModePlayers(GameMode.ADVENTURE); // 1
		assertTrue(creative.size() == 3);
		assertTrue(survival.size() == 1);
		assertTrue(adventure.size() == 1);
	}

	@Test
	public void testCommas(){
		assertEquals("c0, c1, c2", ASUtils.commas(commas));
		assertEquals("no one", ASUtils.commas(new ArrayList<String>()));
		assertEquals("no one", ASUtils.commas(null));
	}

	@Test
	public void testGamemodeAbbreviation(){
		assertEquals("GM = C", ASUtils.gamemodeAbbreviation(GameMode.CREATIVE, false));
		assertEquals("GM = S", ASUtils.gamemodeAbbreviation(GameMode.SURVIVAL, false));
		assertEquals("GM = A", ASUtils.gamemodeAbbreviation(GameMode.ADVENTURE, false));
		assertEquals("C", ASUtils.gamemodeAbbreviation(GameMode.CREATIVE, true));
		assertEquals("S", ASUtils.gamemodeAbbreviation(GameMode.SURVIVAL, true));
		assertEquals("A", ASUtils.gamemodeAbbreviation(GameMode.ADVENTURE, true));
		assertNull(ASUtils.gamemodeAbbreviation(null, false));
		assertNull(ASUtils.gamemodeAbbreviation(null, true));
	}

	@Test
	public void testFileSafeName(){
		assertEquals("000", ASUtils.fileSafeName("000"));
		assertEquals("-0-", ASUtils.fileSafeName("@0@"));
		assertEquals("AaA", ASUtils.fileSafeName("AaA"));
		assertEquals("A-A", ASUtils.fileSafeName("A!A"));
		assertEquals("---", ASUtils.fileSafeName("---"));
	}

	@Test
	public void testWipeFolder() throws Exception{
		// Wipe selective files
		ASUtils.wipeFolder(testDirectory, selectiveNames);

		// Verify
		if(!testDirectory.exists()){
			throw new Exception("Folder deleted too early");
		}
		for(File file : testDirectory.listFiles()){
			String fname = file.getName();
			if(selectiveNames.contains(fname)){
				throw new Exception("Failure to delete file: " + fname);
			}else if(!knownFiles.contains(fname)){
				throw new Exception("Unexpected file: " + fname);
			}
		}

		// Wipe all items
		ASUtils.wipeFolder(testDirectory, null);

		// Verify
		if(!testDirectory.exists()){
			throw new Exception("Folder deleted too early");
		}
		for(File file : testDirectory.listFiles()){
			if(file.isFile()){
				throw new Exception("File should be deleted: " + file.getName());
			}
		}

		// Wipe nothing
		ASUtils.wipeFolder(null, null);
		ASUtils.wipeFolder(null, selectiveNames);

		// == Nested files/folders ==

		// Wipe selective items
		ASUtils.wipeFolder(testDirectory, selectiveNames);

		// Verify
		if(!testDirectory.exists()){
			throw new Exception("Folder deleted too early");
		}
		for(File file : testDirectory.listFiles()){
			String fname = file.getName();
			if(selectiveNames.contains(fname)){
				throw new Exception("Failure to delete file: " + fname);
			}else if(!knownFiles.contains(fname)){
				throw new Exception("Unexpected file: " + fname);
			}
		}

		// Wipe folder names too
		selectiveNames.add("folder1");
		selectiveNames.add("folderX");
		ASUtils.wipeFolder(testDirectory, selectiveNames);

		// Verify
		if(!testDirectory.exists()){
			throw new Exception("Folder deleted too early");
		}
		for(File file : testDirectory.listFiles()){
			String fname = file.getName();
			if(selectiveNames.contains(fname)){
				throw new Exception("Failure to delete file: " + fname);
			}else if(!knownFiles.contains(fname)){
				throw new Exception("Unexpected file: " + fname);
			}
		}

		// Wipe all items
		ASUtils.wipeFolder(testDirectory, null);

		// Verify
		if(!testDirectory.exists()){
			throw new Exception("Folder deleted too early");
		}
		assertTrue(testDirectory.listFiles().length == 0);
	}

	@Test
	// ASUtils.giveTool(Material, Player)
	public void testGiveTool(){
		// TODO
	}

	@Test
	// ASUtils.giveTool(Material, Player, int)
	public void testGiveToolWithSlot(){
		// TODO
	}

	@Test
	public void testMultipleBlocks(){
		/*
		 * D TOP = DOOR REL 1
		 * D BOT = DOOR REL 2
		 * B HED = BED REL 1
		 * B FOT = BED REL 2
		 * 
		 * D REL 1 = STONE
		 * D REL 2 = WOOD
		 * B REL 1 = FIRE
		 * B REL 2 = ANVIL
		 */
		assertNull(ASUtils.multipleBlocks(woodBlock));
		assertNull(ASUtils.multipleBlocks(null));
		assertNotNull(ASUtils.multipleBlocks(bedHead));
		assertNotNull(ASUtils.multipleBlocks(bedFoot));
		assertNotNull(ASUtils.multipleBlocks(doorTop));
		assertNotNull(ASUtils.multipleBlocks(doorBottom));
		assertEquals(Material.FIRE, ASUtils.multipleBlocks(bedHead).getType());
		assertEquals(Material.ANVIL, ASUtils.multipleBlocks(bedFoot).getType());
		assertEquals(Material.STONE, ASUtils.multipleBlocks(doorTop).getType());
		assertEquals(Material.WOOD, ASUtils.multipleBlocks(doorBottom).getType());
	}

	@Test
	public void testGetMobPattern(){
		assertTrue(ASUtils.getMobPattern(EntityPattern.SNOW_GOLEM) instanceof MobPattern);
		assertTrue(ASUtils.getMobPattern(EntityPattern.IRON_GOLEM) instanceof MobPattern);
		assertTrue(ASUtils.getMobPattern(EntityPattern.WITHER) instanceof MobPattern);
		assertNull(ASUtils.getMobPattern(null));
	}

}
