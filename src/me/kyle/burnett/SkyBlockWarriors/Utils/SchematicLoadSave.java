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

import me.kyle.burnett.SkyBlockWarriors.Main;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class SchematicLoadSave {
	
	public static Main plugin;
	public SchematicLoadSave(Main instance){
		plugin = instance;
	}
	
	WorldEditPlugin we = getWorldEdit();
	
	public WorldEditPlugin getWorldEdit() {
		
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");

		if ((plugin == null) || (!(plugin instanceof WorldEditPlugin))) {
			return null;
		}

		return (WorldEditPlugin) plugin;
	}

	public boolean loadIslandSchematic(World world, File file, Integer arena) throws DataException, IOException, MaxChangedBlocksException {
		
		
		SchematicFormat format = SchematicFormat.getFormat(file);
		
		if (format == null) {
			System.out.println("Null Schematic."); 
			return false;
		}
		
		EditSession es = new EditSession(new BukkitWorld(world), 999999999);
		
		CuboidClipboard cc = format.load(file);
		
		Vector v = new Vector(Main.Arena.getDouble(arena + ".BlockX"), Main.Arena.getDouble(arena + ".BlockY"), Main.Arena.getDouble(arena + ".BlockZ"));
		
		cc.paste(es, v, false);
		
		System.out.println("Loaded Schematic.");

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
		
		File filePath = new File(plugin.getDataFolder() + File.separator + "Schematics");
		File file = new File(plugin.getDataFolder() + File.separator + "Schematics" + File.separator + arena + ".schematic");
		
		
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
		
		return true;
	}
	
	
	public boolean doesSelectionExist(Player p){
		
		
		Selection sel = we.getSelection(p);
		
		if(sel == null){
			return false;
		}
		
		return true;
	}

}

/*public boolean saveArena(Player p, Integer arena){
	
	WorldEdit instance = WorldEdit.getInstance();
	
	LocalSession session = instance.getSession(p.getName());

	CuboidClipboard clipboard = null;
	try {
		clipboard = session.getClipboard();
		
	} catch (EmptyClipboardException e1) {
		
		e1.printStackTrace();
		return false;
	}
	
	SchematicFormat f = SchematicFormat.MCEDIT;
	
	File filePath = new File(plugin.getDataFolder() + File.separator + "Schematics");
	File file = new File(plugin.getDataFolder() + File.separator + "Schematics" + File.separator + arena + ".schematic");
	
	
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
	
	return true;
}*/
