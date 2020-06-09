package fr.loyle.ktn.commands;


import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;

import fr.loyle.ktn.KTN;

public class JoinTeam implements BasicCommand{
	
	private KTN plugin;
	  
	public JoinTeam(KTN pl){
		this.plugin = pl;
	}

	@Override
	public boolean onCommand(Player player, String[] args) {
		if(args.length == 1 && !args.equals(" ") && this.plugin.game.getGameManager().getIsStart() == false) {
			this.plugin.game.getTeamsManager().joinTeam(Integer.valueOf(args[0]), player);
		}
		else {
			player.sendMessage(ChatColor.RED + "Il manque l'ID de la team / Partie déjà en cours");
		}
		return true;
	}

	@Override
	public String help(Player p) {
	    if (KTN.hasPermission(p, getPermission()).booleanValue()) {
	        return "/ktn jointeam <id> - Rejoindre une team";
	      }
	      return "";
	}

	@Override
	public String getPermission() {
		return "KTN.admin";
	}
}
