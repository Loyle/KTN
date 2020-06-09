package fr.loyle.ktn.commands;


import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;

import fr.loyle.ktn.KTN;

public class SetLobby implements BasicCommand{
	
	private KTN plugin;
	  
	public SetLobby(KTN pl){
		this.plugin = pl;
	}

	@Override
	public boolean onCommand(Player player, String[] args) {
		
		if(KTN.hasPermission(player, getPermission()).booleanValue()) {
			this.plugin.game.getGameManager().setLobbySpawnLocation(player.getLocation());
			player.sendMessage(ChatColor.GREEN + "lobby définit avec succès");
		}
		return true;
	}

	@Override
	public String help(Player p) {
	    if (KTN.hasPermission(p, getPermission()).booleanValue()) {
	        return "/ktn setlobby - Définit le lobby";
	      }
	      return "";
	}

	@Override
	public String getPermission() {
		return "KTN.admin";
	}
}
