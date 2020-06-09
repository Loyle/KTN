package fr.loyle.ktn.commands;


import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;

import fr.loyle.ktn.KTN;

public class SetTime implements BasicCommand{
	
	private KTN plugin;
	  
	public SetTime(KTN pl){
		this.plugin = pl;
	}

	@Override
	public boolean onCommand(Player player, String[] args) {
		if(KTN.hasPermission(player, getPermission()).booleanValue()) {
			if(this.plugin.game.getGameManager().getIsStart()) {
				if(args.length == 1 && !args.equals(" ")) {
					this.plugin.game.getGameHandler().setTime(Integer.parseInt(args[0]));
				}
				else {
					player.sendMessage(ChatColor.RED + "Il manque le temps (en min)");
				}
			}
			else {
				player.sendMessage(ChatColor.RED + "La partie n'a pas démarré");
			}
		}
		return true;
	}

	@Override
	public String help(Player p) {
	    if (KTN.hasPermission(p, getPermission()).booleanValue()) {
	        return "/ktn settime <temps> - Définie le temps actuel de l'épisode";
	      }
	      return "";
	}

	@Override
	public String getPermission() {
		return "KTN.admin";
	}
}
