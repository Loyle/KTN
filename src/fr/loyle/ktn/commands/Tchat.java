package fr.loyle.ktn.commands;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;

import fr.loyle.ktn.KTN;

public class Tchat implements BasicCommand {

	private KTN plugin;

	public Tchat(KTN pl) {
		this.plugin = pl;
	}

	@Override
	public boolean onCommand(Player player, String[] args) {
		if (this.plugin.game.getGameManager().getIsStart() == true) {
			if (this.plugin.game.getModsManager().getTaupeMod().isTaupe(player)) {

				if (this.plugin.game.getModsManager().getTaupeMod().isInTchat(player)) {
					this.plugin.game.getModsManager().getTaupeMod().removeTaupeInTchat(player);

					player.sendMessage(ChatColor.RED + "Vous avez quitté le tchat des taupes");
				}
				else {
					this.plugin.game.getModsManager().getTaupeMod().addTaupeInTchat(player);

					player.sendMessage(ChatColor.RED + "Vous avez rejoint le tchat des taupes, vous pouvez maintenant parler normalement dans le tchat pour communiquer avec vos alliés");
				}
			}
			else {
				player.sendMessage(ChatColor.RED + "Vous n'êtes pas une taupe !");
			}
		}
		else {
			player.sendMessage(ChatColor.RED + "La partie n'a pas démarré");
		}
		return true;
	}

	@Override
	public String help(Player p) {
		if (KTN.hasPermission(p, getPermission()).booleanValue()) {
			return "/ktn t  - Enter/Leave taupe tchat";
		}
		return "";
	}

	@Override
	public String getPermission() {
		return "";
	}
}
