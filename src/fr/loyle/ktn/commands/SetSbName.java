package fr.loyle.ktn.commands;


import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;

import fr.loyle.ktn.KTN;

public class SetSbName implements BasicCommand{
	
	private KTN plugin;
	private String path = "KTN.";
	  
	public SetSbName(KTN pl){
		this.plugin = pl;
	}

	@Override
	public boolean onCommand(Player player, String[] args) {
		if(KTN.hasPermission(player, getPermission()).booleanValue()) {
			if(args.length >= 1 && !args.equals(" ")) {
				String name = "";
				for(int i =0; i < args.length; i++ ) {
					name+= (" " + args[i]);
				}
				this.plugin.getConfig().set(path + "ScoreBoardTitle", name);
				//this.plugin.saveConfig();
				this.plugin.scoreboard.loadSidebar(this.plugin.getConfig().getInt(path+"EpisodeTime"), 0, 1);
				
				player.sendMessage(ChatColor.GOLD + "Nom changé");
			}
			else {
				player.sendMessage(ChatColor.RED + "Il manque le nom");
			}
		}
		return true;
	}

	@Override
	public String help(Player p) {
	    if (KTN.hasPermission(p, getPermission()).booleanValue()) {
	        return "/ktn setSbName <name> - Changer le nom du scoreboard";
	      }
	      return "";
	}

	@Override
	public String getPermission() {
		return "KTN.admin";
	}
}
