package com.turt2live.antishare.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.turt2live.antishare.util.ASUtils.EntityPattern;
import com.turt2live.antishare.util.generic.MobPattern;

public class TestASUtils {

	private File testDirectory = new File("testingdirectory");
	private List<String> knownFiles = new ArrayList<String>();
	private CopyOnWriteArrayList<String> selectiveNames = new CopyOnWriteArrayList<String>();
	private List<String> commas = new ArrayList<String>();
	private Map<String, String> names = new HashMap<String, String>();

	@Before
	public void setUp() throws IOException{
		// Runs before @Test
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
	}

	@After
	public void tearDown(){
		// Runs after @Test
		ASUtils.wipeFolder(testDirectory, null);
	}

	@Test
	public void testSendToPlayer(){
		// TODO
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
		assertEquals(ASUtils.getGameMode("creative"), GameMode.CREATIVE);
		assertEquals(ASUtils.getGameMode("c"), GameMode.CREATIVE);
		assertEquals(ASUtils.getGameMode("1"), GameMode.CREATIVE);

		// Survival
		assertEquals(ASUtils.getGameMode("survival"), GameMode.SURVIVAL);
		assertEquals(ASUtils.getGameMode("s"), GameMode.SURVIVAL);
		assertEquals(ASUtils.getGameMode("0"), GameMode.SURVIVAL);

		// Adventure
		assertEquals(ASUtils.getGameMode("adventure"), GameMode.ADVENTURE);
		assertEquals(ASUtils.getGameMode("a"), GameMode.ADVENTURE);
		assertEquals(ASUtils.getGameMode("2"), GameMode.ADVENTURE);

		// Invalid
		assertNull(ASUtils.getGameMode("NotGameMode"));
		assertNull(ASUtils.getGameMode("  "));
		assertNull(ASUtils.getGameMode("		")); // Has tab
		assertNull(ASUtils.getGameMode(null));

		// Case insensitivity
		assertEquals(ASUtils.getGameMode("CreaTIve"), GameMode.CREATIVE);
		assertEquals(ASUtils.getGameMode("SurviVAL"), GameMode.SURVIVAL);
		assertEquals(ASUtils.getGameMode("ADVenTUre"), GameMode.ADVENTURE);
	}

	@Test
	public void testBlockToString(){
		// TODO
	}

	@Test
	public void testMaterialToString(){
		assertEquals(ASUtils.materialToString(Material.STONE, false), "1:*");
		assertEquals(ASUtils.materialToString(Material.STONE, true), "1");
		assertNull(ASUtils.materialToString(null, false));
		assertNull(ASUtils.materialToString(null, true));
	}

	@Test
	public void testGetWool(){
		assertEquals(ASUtils.getWool("orange wool"), "35:1");
		assertEquals(ASUtils.getWool("white wool"), "35:0");
		assertEquals(ASUtils.getWool("magenta wool"), "35:2");
		assertEquals(ASUtils.getWool("light_blue wool"), "35:3");
		assertEquals(ASUtils.getWool("light blue wool"), "35:3");
		assertEquals(ASUtils.getWool("yellow wool"), "35:4");
		assertEquals(ASUtils.getWool("lime wool"), "35:5");
		assertEquals(ASUtils.getWool("pink wool"), "35:6");
		assertEquals(ASUtils.getWool("gray wool"), "35:7");
		assertEquals(ASUtils.getWool("light_gray wool"), "35:8");
		assertEquals(ASUtils.getWool("light gray wool"), "35:8");
		assertEquals(ASUtils.getWool("cyan wool"), "35:9");
		assertEquals(ASUtils.getWool("purple wool"), "35:10");
		assertEquals(ASUtils.getWool("blue wool"), "35:11");
		assertEquals(ASUtils.getWool("brown wool"), "35:12");
		assertEquals(ASUtils.getWool("green wool"), "35:13");
		assertEquals(ASUtils.getWool("red wool"), "35:14");
		assertEquals(ASUtils.getWool("black wool"), "35:15");
		assertEquals(ASUtils.getWool("notacolor wool"), "35:notacolor");
		assertEquals(ASUtils.getWool("wool"), "35:0");
		assertNull(ASUtils.getWool("notAColor"));
		assertNull(ASUtils.getWool(null));
	}

	@Test
	// ASUtils.getEntityName(Entity)
	public void testGetEntityNameFromEntity(){
		// TODO
	}

	@Test
	public void testGetEntityNameFromString(){
		for(String test : names.keySet()){
			String proper = names.get(test);
			assertEquals(ASUtils.getEntityName(test), proper);
			assertEquals(ASUtils.getEntityName(test.toUpperCase()), proper);
			assertEquals(ASUtils.getEntityName(proper), proper);
			assertEquals(ASUtils.getEntityName(proper.toUpperCase()), proper);
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
		// TODO
	}

	@Test
	public void testCommas(){
		assertEquals(ASUtils.commas(commas), "c0, c1, c2");
		assertEquals(ASUtils.commas(new ArrayList<String>()), "no one");
		assertEquals(ASUtils.commas(null), "no one");
	}

	@Test
	public void testGamemodeAbbreviation(){
		assertEquals(ASUtils.gamemodeAbbreviation(GameMode.CREATIVE, false), "GM = C");
		assertEquals(ASUtils.gamemodeAbbreviation(GameMode.SURVIVAL, false), "GM = S");
		assertEquals(ASUtils.gamemodeAbbreviation(GameMode.ADVENTURE, false), "GM = A");
		assertEquals(ASUtils.gamemodeAbbreviation(GameMode.CREATIVE, true), "C");
		assertEquals(ASUtils.gamemodeAbbreviation(GameMode.SURVIVAL, true), "S");
		assertEquals(ASUtils.gamemodeAbbreviation(GameMode.ADVENTURE, true), "A");
		assertNull(ASUtils.gamemodeAbbreviation(null, false));
		assertNull(ASUtils.gamemodeAbbreviation(null, true));
	}

	@Test
	public void testFileSafeName(){
		assertEquals(ASUtils.fileSafeName("000"), "000");
		assertEquals(ASUtils.fileSafeName("@0@"), "-0-");
		assertEquals(ASUtils.fileSafeName("AaA"), "AaA");
		assertEquals(ASUtils.fileSafeName("A!A"), "A-A");
		assertEquals(ASUtils.fileSafeName("---"), "---");
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
		// TODO
	}

	@Test
	public void testGetMobPattern(){
		assertTrue(ASUtils.getMobPattern(EntityPattern.SNOW_GOLEM) instanceof MobPattern);
		assertTrue(ASUtils.getMobPattern(EntityPattern.IRON_GOLEM) instanceof MobPattern);
		assertTrue(ASUtils.getMobPattern(EntityPattern.WITHER) instanceof MobPattern);
		assertNull(ASUtils.getMobPattern(null));
	}

}
