package fr.loyle.ktn.commands;


import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;

import fr.loyle.ktn.KTN;

public class Reveal implements BasicCommand{
	
	private KTN plugin;
	  
	public Reveal(KTN pl){
		this.plugin = pl;
	}

	@Override
	public boolean onCommand(Player player, String[] args) {
		if(KTN.hasPermission(player, getPermission()).booleanValue()) {
			if(this.plugin.game.getGameManager().getIsStart()) {
				this.plugin.game.getModsManager().getTaupeMod().reveal(player);
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
	        return "/ktn reveal - Ce révéler être une taupe !";
	      }
	      return "";
	}

	@Override
	public String getPermission() {
		return "";
	}
}
