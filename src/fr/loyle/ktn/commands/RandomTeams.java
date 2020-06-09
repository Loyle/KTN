package fr.loyle.ktn.commands;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;

import fr.loyle.ktn.KTN;

public class RandomTeams implements BasicCommand {

	private KTN plugin;

	public RandomTeams(KTN pl) {
		this.plugin = pl;
	}

	@Override
	public boolean onCommand(Player player, String[] args) {
		if (KTN.hasPermission(player, getPermission()).booleanValue()) {
			if (this.plugin.game.getGameManager().getIsStart() == false) {
				if (args.length == 1 && !args.equals(" ")) {
					this.plugin.game.getTeamsManager().randomize(Integer.parseInt(args[0]));
				}
				else {
					player.sendMessage(ChatColor.RED + "Il manque le numbre de teams a remplir");
				}
			}
			else {
				player.sendMessage(ChatColor.RED + "La partie a démarré");
			}
		}
		return true;
	}

	@Override
	public String help(Player p) {
		if (KTN.hasPermission(p, getPermission()).booleanValue()) {
			return "/ktn random <number team> - Random teams";
		}
		return "";
	}

	@Override
	public String getPermission() {
		return "KTN.admin";
	}
}
