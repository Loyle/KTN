package fr.loyle.ktn.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import fr.loyle.ktn.KTN;

public class TeamsManager {
	private String path = "KTN.";
	private Teams[] TeamsList;
	private KTN plugin;
	List<Player> players = new ArrayList<>();

	public TeamsManager(KTN pl) {
		this.plugin = pl;

		this.TeamsList = new Teams[this.plugin.getConfig().getInt(path + "Teams.Numbers")];

		for (int i = 0; i <= this.plugin.getConfig().getInt(path + "Teams.Numbers") - 1; i++) {
			this.TeamsList[i] = new Teams(this.plugin, this.plugin.getConfig().getString(path + "TeamsList." + i + ".Name"), this.plugin.getConfig().getString(path + "TeamsList." + i + ".Name"), this.plugin.getConfig().getString(path + "TeamsList." + i + ".Color"), i, "");
			System.out.println("[KTN] Load team " + i + " : " + this.TeamsList[i].getDisplayName());

		}
	}

	public void joinTeam(int teamID, Player player) {
		this.leaveTeam(player);
		this.TeamsList[teamID].addPlayer(player);
		player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Vous avez rejoint la team " + this.TeamsList[teamID].getColor() + this.TeamsList[teamID].getDisplayName());
	}

	public Teams[] getTeams() {
		return this.TeamsList;
	}

	public String getName(int teamID) {
		return this.TeamsList[teamID].getDisplayName();
	}

	public String getColor(int teamID) {
		return this.TeamsList[teamID].getColor();
	}

	public void leaveTeam(Player player) {
		for (int i = 0; i <= this.plugin.getConfig().getInt(path + "Teams.Numbers") - 1; i++) {
			this.TeamsList[i].removePlayer(player);
		}
	}

	public int getNumberAliveTeams() {
		int number = 0;
		for (int i = 0; i <= this.plugin.getConfig().getInt(path + "Teams.Numbers") - 1; i++) {
			if (this.TeamsList[i].isAlive()) {
				number++;
			}
		}
		if (this.plugin.getConfig().getBoolean(path + "TaupeMode.Activated") && this.plugin.getConfig().getBoolean(path + "PlayWithTeams")) {
			number += this.plugin.game.getModsManager().getTaupeMod().getAliveTaupeTeams();
		}

		return number;
	}

	public int getTeamOfPlayer(Player player) {
		for (int i = 0; i <= this.plugin.getConfig().getInt(path + "Teams.Numbers") - 1; i++) {
			if (this.TeamsList[i].checkPlayerIsIn(player)) {
				return i;
			}
		}
		return -1;
	}

	public void randomize(int numberTeam) {
		int boucle;
		if (numberTeam > this.plugin.getConfig().getInt(path + "Teams.Numbers")) {
			boucle = this.plugin.getConfig().getInt(path + "Teams.Numbers");
		}
		else {
			boucle = numberTeam;
		}

		this.players.clear();
		for (Player player : this.plugin.getServer().getOnlinePlayers()) {
			this.players.add(player);
		}
		while (this.players.isEmpty() == false) {
			for (int i = 0; i <= boucle - 1; i++) {
				Collections.shuffle(this.players);
				if (this.players.isEmpty() == false) {
					this.joinTeam(i, this.players.get(0));
					this.players.remove(0);
				}
				else {
					break;
				}
			}
		}
	}
}