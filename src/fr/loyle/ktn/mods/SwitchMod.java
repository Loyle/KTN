package fr.loyle.ktn.mods;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import fr.loyle.ktn.KTN;
import fr.loyle.ktn.game.Teams;

public class SwitchMod {
	private KTN plugin;
	private String path = "KTN.";
	private Location firstlocation = null;
	private int firstteam = -1;
	private Player lastplayer = null;

	public SwitchMod(KTN pl) {
		this.plugin = pl;
	}

	public void check(int Episode, int MinutesLeft, int SecondLeft) {
		if (this.plugin.getConfig().getInt(path + "SwitchMode.StartEpisode") <= Episode && (this.plugin.getConfig().getInt(path + "SwitchMode.EndEpisode")) >= Episode && MinutesLeft == (this.plugin.getConfig().getInt(path + "SwitchMode.SwitchTime") - 1) && SecondLeft == 59) {
			this.switchPlayers();
		}
	}

	public void switchPlayers() {
		for (Teams teams : this.plugin.game.getTeamsManager().getTeams()) {
			if (teams.getSize() != 0) {
				Player player = this.getRandomPlayer(teams);
				if (this.firstlocation == null) {
					this.firstteam = this.plugin.game.getTeamsManager().getTeamOfPlayer(player);
					this.firstlocation = player.getLocation();
					this.lastplayer = player;
				}
				else {
					this.lastplayer.teleport(player.getLocation());
					this.plugin.game.getTeamsManager().joinTeam(this.plugin.game.getTeamsManager().getTeamOfPlayer(player), this.lastplayer);
					this.lastplayer = player;
				}
			}
		}

		this.lastplayer.teleport(this.firstlocation);
		this.plugin.game.getTeamsManager().joinTeam(this.firstteam, this.lastplayer);
		this.lastplayer = null;
		this.firstlocation = null;
		this.firstteam = -1;
	}

	public Player getRandomPlayer(Teams team) {
		Player[] list = new Player[team.getSize()];

		int i = 0;
		for (Player player : team.getPlayers()) {
			list[i] = player;
			i++;
		}
		int select;
		if(team.getSize() > 1) {
			select = (int) (Math.random() * ((team.getSize()) - 0 ));	
		}
		else {
			select = 0;
		}
		

		return list[select];
	}
}
