package me.kyle.burnett.SkyBlockWarriors.Utils;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalPlayer;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.schematic.SchematicFormat;

import java.io.File;
import java.io.IOException;

import me.kyle.burnett.SkyBlockWarriors.GameManager;
import me.kyle.burnett.SkyBlockWarriors.Main;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class WorldEditUtility {
	
	static WorldEditUtility instance = new WorldEditUtility();
	
	public static WorldEditUtility getInstance(){
		return instance;
	}
	
	WorldEditPlugin we = getWorldEdit();
	
	public WorldEditPlugin getWorldEdit() {
		
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");

		if ((plugin == null) || (!(plugin instanceof WorldEditPlugin))) {
			return null;
		}

		return (WorldEditPlugin) plugin;
	}

	public boolean loadIslandSchematic(Integer arena) throws DataException, IOException, MaxChangedBlocksException {
		
		File file = new File(Main.getInstance().getDataFolder() + File.separator + "Schematics" + File.separator + arena + ".schematic");

		World world = Bukkit.getServer().getWorld(Main.getInstance().Arena.getString(arena + ".World"));
		
		SchematicFormat format = SchematicFormat.getFormat(file);
		
		if (format == null) {
			System.out.println("Null Schematic."); 
			return false;
		}
		
		EditSession es = new EditSession(new BukkitWorld(world), 999999999);
		
		CuboidClipboard cc = format.load(file);
		
		Vector v = new Vector(Main.getInstance().Arena.getDouble(arena + ".OriginX"), Main.getInstance().Arena.getDouble(arena + ".OriginY"), Main.getInstance().Arena.getDouble(arena + ".OriginZ"));
		
		cc.paste(es, v, false);
		
		System.out.println("Loaded Schematic.");

		return true;
	}
	
	public boolean regenAllIslands(){
		
		int amount = GameManager.getInstance().getArenaAmount();
		
		for(int x = 0; x < amount; x++){
			
			File file = new File(Main.getInstance().getDataFolder() + File.separator + "Schematics" + File.separator + x + ".schematic");
			
			World world = Bukkit.getServer().getWorld(Main.getInstance().Arena.getString(x + "World"));
			
			if(world == null){
				return false;
			}
			
			SchematicFormat format = SchematicFormat.getFormat(file);
			
			if (format == null) {
				System.out.println("Null Schematic."); 
				return false;
			}
			
			EditSession es = new EditSession(new BukkitWorld(world), 999999999);
			
			CuboidClipboard cc = null;
			try {
				cc = format.load(file);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (DataException e) {
				e.printStackTrace();
			}
			
			Vector v = new Vector(Main.getInstance().Arena.getDouble(x + ".OriginX"), Main.getInstance().Arena.getDouble(x + ".OriginY"), Main.getInstance().Arena.getDouble(x + ".OriginZ"));
			
			try {
				cc.paste(es, v, false);
			} catch (MaxChangedBlocksException e) {
				e.printStackTrace();
			}
			
		}
		return true;
	}
	  
	public boolean saveArena(Player p, Integer arena){
		
		WorldEdit instance = WorldEdit.getInstance();
		LocalSession session = instance.getSession(p.getName());
		LocalPlayer player = we.wrapPlayer(p);
		EditSession es = new EditSession(new BukkitWorld(p.getWorld()), 999999999);
		Region region = null;
		
		try {
			region = session.getSelection(session.getSelectionWorld());
		} catch (IncompleteRegionException e1) {
			e1.printStackTrace();
		}
		
	    Vector min = region.getMinimumPoint();
	    Vector max = region.getMaximumPoint();
	    Vector pos = null;
	  
		try {
			pos = session.getPlacementPosition(player);
		} catch (IncompleteRegionException e1) {
			e1.printStackTrace();
		}

	    CuboidClipboard clipboard = new CuboidClipboard(max.subtract(min).add(new Vector(1, 1, 1)),min, min.subtract(pos));
	  
	    clipboard.copy(es);
		
		SchematicFormat f = SchematicFormat.MCEDIT;
		
		File filePath = new File(Main.getInstance().getDataFolder() + File.separator + "Schematics");
		File file = new File(Main.getInstance().getDataFolder() + File.separator + "Schematics" + File.separator + arena + ".schematic");
		
		if(!file.exists()){
			
			filePath.mkdirs();
			
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			f.save(clipboard, file);
		} catch (IOException | DataException e) {
			e.printStackTrace();
		}
		Main.getInstance().Arena.set(arena + ".OriginX", p.getLocation().getBlockX());
		Main.getInstance().Arena.set(arena + ".OriginY", p.getLocation().getBlockY());
		Main.getInstance().Arena.set(arena + ".OriginZ", p.getLocation().getBlockZ());
		Main.getInstance().Arena.set(arena + ".MaxX", min.getBlockX());
		Main.getInstance().Arena.set(arena + ".MaxY", min.getBlockY());
		Main.getInstance().Arena.set(arena + ".MaxZ", min.getBlockZ());
		Main.getInstance().Arena.set(arena + ".MinX", min.getBlockX());
		Main.getInstance().Arena.set(arena + ".MinY", min.getBlockY());
		Main.getInstance().Arena.set(arena + ".MinZ", min.getBlockZ());
		Main.getInstance().Arena.set(arena + ".World", p.getWorld().getName());

		return true;
	}
	
	public boolean overrideSave(Player p, Integer arena){
		
		WorldEdit instance = WorldEdit.getInstance();
		LocalSession session = instance.getSession(p.getName());
		LocalPlayer player = we.wrapPlayer(p);
		EditSession es = new EditSession(new BukkitWorld(p.getWorld()), 999999999);
		Region region = null;
		
		try {
			region = session.getSelection(session.getSelectionWorld());
		} catch (IncompleteRegionException e1) {
			e1.printStackTrace();
		}
		
	    Vector min = region.getMinimumPoint();
	    Vector max = region.getMaximumPoint();
	    Vector pos = null;
	  
		try {
			pos = session.getPlacementPosition(player);
		} catch (IncompleteRegionException e1) {
			e1.printStackTrace();
		}

	    CuboidClipboard clipboard = new CuboidClipboard(max.subtract(min).add(new Vector(1, 1, 1)),min, min.subtract(pos));
	  
	    clipboard.copy(es);
		
		SchematicFormat f = SchematicFormat.MCEDIT;
		
		File filePath = new File(Main.getInstance().getDataFolder() + File.separator + "Schematics");
		File file = new File(Main.getInstance().getDataFolder() + File.separator + "Schematics" + File.separator + arena + ".schematic");
		
		if(!file.exists()){
			
			filePath.mkdirs();
			
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			f.save(clipboard, file);
		} catch (IOException | DataException e) {
			e.printStackTrace();
		}
		Main.getInstance().Arena.set(arena + ".OriginX", p.getLocation().getBlockX());
		Main.getInstance().Arena.set(arena + ".OriginY", p.getLocation().getBlockY());
		Main.getInstance().Arena.set(arena + ".OriginZ", p.getLocation().getBlockZ());
		Main.getInstance().Arena.set(arena + ".MaxX", min.getBlockX());
		Main.getInstance().Arena.set(arena + ".MaxY", min.getBlockY());
		Main.getInstance().Arena.set(arena + ".MaxZ", min.getBlockZ());
		Main.getInstance().Arena.set(arena + ".MinX", min.getBlockX());
		Main.getInstance().Arena.set(arena + ".MinY", min.getBlockY());
		Main.getInstance().Arena.set(arena + ".MinZ", min.getBlockZ());
		Main.getInstance().Arena.set(arena + ".World", p.getWorld().getName());

		return true;
	}
	
	public void resaveArena(Integer arena, Player p){
		
		World world = Bukkit.getServer().getWorld(Main.getInstance().Arena.getString(arena + ".World"));
		
		int maxX = Main.getInstance().Arena.getInt(arena + ".MaxX");
		int maxY = Main.getInstance().Arena.getInt(arena + ".MaxY");
		int maxZ = Main.getInstance().Arena.getInt(arena + ".MaxZ");
		int minX = Main.getInstance().Arena.getInt(arena + ".MinX");
		int minY = Main.getInstance().Arena.getInt(arena + ".MinY");
		int minZ = Main.getInstance().Arena.getInt(arena + ".MinZ");
		int x,y,z;
		
		x = Main.getInstance().Arena.getInt(arena + ".OriginX");
		y = Main.getInstance().Arena.getInt(arena + ".OriginY");
		z = Main.getInstance().Arena.getInt(arena + ".OriginZ");
		
	    Vector min = new Vector(maxX, maxY, maxZ);
	    Vector max = new Vector(minX, minY, minZ);
	    Vector pos = new Vector(x, y, z);
	    
	    EditSession es = new EditSession(new BukkitWorld(world), 999999999);
	    
	    CuboidClipboard clipboard = new CuboidClipboard(max.subtract(min).add(new Vector(1, 1, 1)),min, min.subtract(pos));
		  
		File filePath = new File(Main.getInstance().getDataFolder() + File.separator + "Schematics");
		File file = new File(Main.getInstance().getDataFolder() + File.separator + "Schematics" + File.separator + arena + ".schematic");
		
		clipboard.copy(es);
		
		SchematicFormat f = SchematicFormat.MCEDIT;
		
		if(file.exists()){
			file.delete();
		}

		if(!file.exists()){
			
			filePath.mkdirs();
			
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			f.save(clipboard, file);
		} catch (IOException | DataException e) {
			e.printStackTrace();
		}
	}
	
	public boolean doesSelectionExist(Player p){
		
		
		Selection sel = we.getSelection(p);
		
		if(sel == null){
			return false;
		}
		
		return true;
	}

	public boolean isChest(Player p){
		
		Selection sel = we.getSelection(p);
		
		if(!(sel.getArea() > 1)){
						
			if(sel.getMaximumPoint().getBlock().equals(Material.CHEST)){
				return true;
			}
		}
		
		return false;
	}
	
	public Location getChestLocation(Player p){
		
		Selection sel = we.getSelection(p);
		
		if(!(sel.getArea() > 1)){
						
			if(sel.getMaximumPoint().getBlock().equals(Material.CHEST)){
				return new Location(sel.getWorld(), sel.getMaximumPoint().getBlockX(), sel.getMaximumPoint().getBlockY(), sel.getMaximumPoint().getBlockZ());
			}
		}
		
		return null;
	}
}
