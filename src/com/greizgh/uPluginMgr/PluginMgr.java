package com.greizgh.uPluginMgr;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginMgr extends JavaPlugin{
	private static Logger logger = Logger.getLogger("Minecraft");
	PluginDescriptionFile plugdesc;
	PluginManager pm;
	private boolean fail=false;
	public static void log(Level level, String message) {
		logger.log(level, "[uPluginMgr] " + message);
	}

	@Override
	public void onDisable() {
		log(Level.INFO, plugdesc.getName() + " disabled.");
	}

	@Override
	public void onEnable() {
		plugdesc = this.getDescription();
		pm = this.getServer().getPluginManager();
		log(Level.INFO, plugdesc.getName() + " v"+plugdesc.getVersion()+" enabled.");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Player player = null;
		if (sender instanceof Player){
			player = (Player) sender;
		}
		if (cmd.getName().equalsIgnoreCase("upm")) {
			if (args.length < 2) {
				sender.sendMessage("Please use /uPM load|unload|reload plugin_name");
			}
			else {
				if (args[0].equalsIgnoreCase("load")
						|| args[0].equalsIgnoreCase("l")) {
					if (player==null || player.hasPermission("uPluginMgr.load")){
						load(args[1]);
						if (!fail) {
							sender.sendMessage(ChatColor.DARK_PURPLE
									+ "[uPluginMgr] loaded " + args[1]
									+ ChatColor.WHITE);
						}
					}
				}
				if (args[0].equalsIgnoreCase("reload")
						|| args[0].equalsIgnoreCase("r")) {
					if (player==null || player.hasPermission("uPluginMgr.reload")){
						unload(args[1]);
						load(args[1]);
						if (!fail) {
							sender.sendMessage(ChatColor.DARK_PURPLE
									+ "[uPluginMgr] reloaded " + args[1]
									+ ChatColor.WHITE);
						}
					}
				}
				if (args[0].equalsIgnoreCase("unload")
						|| args[0].equalsIgnoreCase("u")) {
					if (player==null || player.hasPermission("uPluginMgr.unload")){
						unload(args[1]);
						if (!fail) {
							sender.sendMessage(ChatColor.DARK_PURPLE
									+ "[uPluginMgr] unloaded " + args[1]
									+ ChatColor.WHITE);
						}
					}
				}
			}
			if (fail){
				sender.sendMessage(ChatColor.DARK_PURPLE+"[uPluginMgr] something went wrong..."+ChatColor.WHITE);
				fail=false;
			}
			return true;
		}
		return false;
	}
	
	private void load(String name){
		try {
			pm.enablePlugin(pm.getPlugin(name));
		} catch (Exception e) {
			log(Level.SEVERE, "Unable to load plugin "+name);
			fail = true;
		}
	}
	private void unload(String name){
		try {
			pm.disablePlugin(pm.getPlugin(name));
		} catch (Exception e) {
			log(Level.SEVERE, "Unable to unload plugin "+name);
			fail=true;
		}
	}

}
