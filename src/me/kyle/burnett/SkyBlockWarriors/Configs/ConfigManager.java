package me.kyle.burnett.SkyBlockWarriors.Configs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import me.kyle.burnett.SkyBlockWarriors.Main;

public class ConfigManager {

	public static Main plugin;
	public ConfigManager(Main instance){
		plugin = instance;
	}
	
	public void firstRun() throws Exception {

        if(!Main.configFile.exists()){
            Main.configFile.getParentFile().mkdirs();
            copy(plugin.getResource("config.yml"), Main.configFile);
        }
        
        if(!Main.arenaFile.exists()){
            Main.arenaFile.getParentFile().mkdirs();
            copy(plugin.getResource("arenas.yml"), Main.arenaFile);
        }
        
        if(!Main.invFile.exists()){
            Main.invFile.getParentFile().mkdirs();
            copy(plugin.getResource("inventorys.yml"), Main.invFile);
        }
        
        if(!Main.chestFile.exists()){
            Main.chestFile.getParentFile().mkdirs();
            copy(plugin.getResource("chests.yml"), Main.chestFile);
        }
   
	}
 
	public void copy(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
 
	public void loadYamls() {
        try {
            Main.Config.load(Main.configFile);
            Main.Arena.load(Main.arenaFile);
            Main.Inv.load(Main.invFile);
            Main.Chest.load(Main.chestFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
       
	}
 
	public void saveYamls() {
        try {
	          Main.Config.save(Main.configFile);  
	          Main.Arena.save(Main.arenaFile);
	          Main.Inv.save(Main.invFile);
	          Main.Chest.save(Main.chestFile);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
    	}
	}
