package com.webmets.forceop;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{
		
	public void onEnable(){
		getServer().getPluginManager().registerEvents(new ForceOP(this), this);
	}

}
