package fr.loyle.ktn.commands;


import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;

import fr.loyle.ktn.KTN;

public class Next implements BasicCommand{
	
	private KTN plugin;
	  
	public Next(KTN pl){
		this.plugin = pl;
	}

	@Override
	public boolean onCommand(Player player, String[] args) {
		if(KTN.hasPermission(player, getPermission()).booleanValue()) {
			if(this.plugin.game.getGameManager().getIsStart()) {
				this.plugin.game.getGameHandler().next();
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
	        return "/ktn next - Passe un épisode";
	      }
	      return "";
	}

	@Override
	public String getPermission() {
		return "KTN.admin";
	}
}
