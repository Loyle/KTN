package fr.loyle.ktn.commands;


import org.bukkit.entity.Player;

import fr.loyle.ktn.KTN;

public class Start implements BasicCommand{
	
	private KTN plugin;
	  
	public Start(KTN pl){
		this.plugin = pl;
	}

	@Override
	public boolean onCommand(Player player, String[] args) {
		if(KTN.hasPermission(player, getPermission()).booleanValue()) {
			this.plugin.game.getGameManager().checkStart(player);
		}
		return true;
	}

	@Override
	public String help(Player p) {
	    if (KTN.hasPermission(p, getPermission()).booleanValue()) {
	        return "/ktn start - Démarre la partie";
	      }
	      return "";
	}

	@Override
	public String getPermission() {
		return "KTN.admin";
	}
}
