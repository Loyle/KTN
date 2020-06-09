package fr.loyle.ktn.mods;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Team;

import fr.loyle.ktn.KTN;
import fr.loyle.ktn.game.Teams;

public class TaupeMod {
	private KTN plugin;
	private String path = "KTN.";
	private List<Player> taupes = new ArrayList<>();
	private List<Player> tchat = new ArrayList<>();
	private Team[] taupeTeams;
	private int nbTaupeTeam;
	private int totalTaupe;
	private int taupeInEachTeam;
	Map<Player, Integer> players = new HashMap<Player, Integer>();
	private boolean canReveal;

	public TaupeMod(KTN pl) {
		this.plugin = pl;

		this.canReveal = false;

		this.nbTaupeTeam = this.plugin.getConfig().getInt(path + "TaupeMode.NumberTaupeTeams");

		this.taupeTeams = new Team[this.nbTaupeTeam];

		for (int i = 0; i < this.nbTaupeTeam; i++) {
			this.taupeTeams[i] = this.plugin.scoreboard.createTeam("Taupe" + (i + 1), "Taupe" + (i + 1), this.colorize("&c"), 1000 + (i + 1), "[Taupe" + (i + 1) + "] ");
			System.out.println("[KTN] Load de la team Taupe" + (i + 1));
		}
	}

	public void check(int Episode, int MinutesLeft, int SecondLeft) {
		if (this.plugin.getConfig().getInt(path + "TaupeMode.TaupeSelect") == Episode && (this.plugin.getConfig().getInt(path + "EpisodeTime") - 1) == MinutesLeft && SecondLeft == 59) {
			this.selectTaupe();
			for(Player player : this.taupes) {
				player.sendMessage(ChatColor.RED + "ATTENTION, vous êtes une taupe ! \n- /ktn t : pour le tchat \n- /ktn reveal : pour vous reveal \n- /ktn kit : pour récupérer votre kit");
			}
			this.canReveal = true;
		}
		if (this.plugin.getConfig().getInt(path + "TaupeMode.AutoReveal") == Episode && (this.plugin.getConfig().getInt(path + "EpisodeTime") - 1) == MinutesLeft && SecondLeft == 59) {
			this.revealAll();
			this.canReveal = false;
		}
	}

	private void selectTaupe() {
		this.taupes.clear();

		for (Teams team : this.plugin.game.getTeamsManager().getTeams()) {
			if (team.getSize() != 0) {
				List<Player> randomTable = new ArrayList<>();
				for (Player player : team.getPlayers()) {
					randomTable.add(player);
				}
				Collections.shuffle(randomTable);
				int i = 0;
				while (i < this.plugin.getConfig().getInt(path + "TaupeMode.TaupePerTeam")) {
					this.taupes.add(randomTable.get(i));
					i++;
				}

			}
		}

		this.totalTaupe = this.taupes.size();
		this.taupeInEachTeam = this.totalTaupe / this.nbTaupeTeam;

		int teamID = 0;
		int counter = 0;
		for (int i = 0; i < this.totalTaupe; i++) {
			counter++;
			this.players.put(this.taupes.get(i), teamID);

			if (counter >= this.taupeInEachTeam) {
				counter = 0;
				teamID++;
			}
		}
	}

	@SuppressWarnings("deprecation")
	private void revealAll() {
		for (Player player : this.taupes) {
			if (this.taupeTeams[this.players.get(player)].getPlayers().contains(player) == false) {

				this.taupeTeams[this.players.get(player)].addPlayer(player);
				this.plugin.game.getTeamsManager().leaveTeam(player);
				this.plugin.getServer().broadcastMessage(ChatColor.BOLD + "" + ChatColor.RED + player.getDisplayName() + " est révélé être une taupe !");
			}
		}
		for (Player p : this.plugin.game.getPlayerManager().getPlayers()) {
			p.playSound(p.getLocation(), Sound.GHAST_SCREAM, 20, 1);
		}
	}

	@SuppressWarnings("deprecation")
	public void reveal(Player player) {
		if (this.taupes.contains(player) && this.canReveal) {
			this.taupeTeams[this.players.get(player)].addPlayer(player);
			this.plugin.game.getTeamsManager().leaveTeam(player);
			this.plugin.getServer().broadcastMessage(ChatColor.BOLD + "" + ChatColor.RED + player.getDisplayName() + " s'est révélé être une taupe !");

			player.getWorld().dropItem(player.getLocation(), new ItemStack(Material.GOLDEN_APPLE));

			for (Player p : this.plugin.game.getPlayerManager().getPlayers()) {
				p.playSound(p.getLocation(), Sound.GHAST_SCREAM, 20, 1);
			}
		}
		else {
			System.out.println(this.canReveal);
			System.out.println(this.taupes.contains(player));
			player.sendMessage(ChatColor.RED + "Vous n'êtes pas une taupe !");
		}
	}

	public String colorize(String message) {
		return message.replaceAll("&([a-f0-9])", ChatColor.COLOR_CHAR + "$1");
	}

	public int getAliveTaupeTeams() {
		int alive = 0;
		for (int i = 0; i < this.nbTaupeTeam; i++) {
			if (this.taupeTeams[i].getSize() > 0) {
				alive++;
			}
		}
		return alive;
	}

	public void addTaupeInTchat(Player player) {
		this.tchat.add(player);
	}

	public void removeTaupeInTchat(Player player) {
		this.tchat.remove(player);
	}

	public boolean isInTchat(Player player) {
		return this.tchat.contains(player);
	}

	public void sendMsgToTaupes(Player p, String message) {
		int playerTeam = this.players.get(p);
		for (Player player : this.taupes) {
			if (this.players.get(player) == playerTeam) {
				player.sendMessage(ChatColor.RED + "<" + p.getDisplayName() + "> " + message);
			}
		}
	}

	public boolean isTaupe(Player player) {
		return this.taupes.contains(player);
	}
}
