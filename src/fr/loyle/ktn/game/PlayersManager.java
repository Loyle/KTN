package fr.loyle.ktn.game;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import fr.loyle.ktn.KTN;
import fr.loyle.ktn.libraries.Title;

public class PlayersManager {

	private HashMap<String, Player> players = new HashMap<>();
	private HashMap<String, Player> spectators = new HashMap<>();
	private String path = "KTN.";

	public KTN plugin;

	public PlayersManager(KTN pl) {
		this.plugin = pl;
	}

	// Gestionnaire des joueurs
	// Add joueur � la connexion, (on v�rifie si on peut d�marrer la partie)
	public void addPlayer(Player player) {
		this.players.put(player.getName(), player);

		Double X = this.plugin.getConfig().getDouble(path + "Lobby.Spawn.X");
		Double Y = this.plugin.getConfig().getDouble(path + "Lobby.Spawn.Y");
		Double Z = this.plugin.getConfig().getDouble(path + "Lobby.Spawn.Z");
		Float Yaw = (float) this.plugin.getConfig().getDouble(path + "Lobby.Spawn.YAW");
		Float Pitch = (float) this.plugin.getConfig().getDouble(path + "Lobby.Spawn.PITCH");
		World World = Bukkit.getServer().getWorld(this.plugin.getConfig().getString(path + "Lobby.WorldName"));

		Location l = new Location(World, X, Y, Z, Yaw, Pitch);
		player.teleport(l);

		
		Title title = new Title(ChatColor.RED + "Kill The Noob", ChatColor.GOLD + "Bienvenue sur KTN", 1, 3, 1);
		title.send(player);
		
		this.plugin.getServer().broadcastMessage(ChatColor.YELLOW + player.getName() + " a rejoint la partie (" + this.plugin.game.getPlayerManager().getNumberPlayers() + "/" + this.plugin.game.getGameManager().getMaxPlayers() + ")");
		
		player.setGameMode(GameMode.ADVENTURE);
		player.setHealth(20.0);
		player.setFoodLevel(100000);
		player.setSaturation(100000);
		
		for(Player playerlist : this.plugin.getServer().getOnlinePlayers()) {
			playerlist.setScoreboard(this.plugin.scoreboard.getScoreboard());
		}
		
		this.plugin.scoreboard.loadSidebar(this.plugin.getConfig().getInt(path+"EpisodeTime"), 0, 1);
		this.initInventory(player);
	}
	public void initInventory(Player player) {
		player.getInventory().clear();
		if(this.plugin.getConfig().getBoolean(path + "PlayWithTeams")) {
			for(int i = 0 ; i <= this.plugin.getConfig().getInt(path+"Teams.Numbers")-1 ; i++) {
				Byte dataID = this.ConvertColorToID(i);
				PlayerInventory inv = player.getInventory();
				ItemStack Banner = new ItemStack(Material.BANNER, 1, dataID);
				
				ItemMeta metaBanner = Banner.getItemMeta();
				metaBanner.setDisplayName(ChatColor.GOLD + "Rejoindre la team "+ this.plugin.game.getTeamsManager().getColor(i) + this.plugin.game.getTeamsManager().getName(i));
				Banner.setItemMeta(metaBanner);
				inv.setItem(i,Banner);
			}
		}
	}

	// Remove joueur (lorsqu'il se d�connecte)
	public void removePlayer(Player player) {
		this.plugin.game.getTeamsManager().leaveTeam(player);
		this.players.remove(player.getName());
	}

	// Pour r�cuperer le nombre de joueurs actuellement dans la partie
	public int getNumberPlayers() {
		return this.players.size();
	}

	// Pour get les joueurs actuellement dans la partie
	public Collection<Player> getPlayers() {
		return Collections.unmodifiableCollection(this.players.values());
	}
	
	public void addSpectators(Player player) {
		this.spectators.put(player.getName(), player);
	}
	public void removeSpectators(Player player) {
		this.spectators.remove(player.getName());
	}
	public boolean checkPlayer(Player player) {
		return this.players.containsKey(player.getName());
	}
	@SuppressWarnings("deprecation")
	public void CheckPotions(Player player) {
		ItemStack[] contents = player.getInventory().getContents();
        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (item.getType() == Material.POTION && (item.getData().getData() == 8233 || item.getData().getData() == 8297 || item.getData().getData() == 16425)) {
            	int remove = item.getAmount() - item.getAmount();
            	if(item.getAmount() - 1 > 0) {
            		item.setAmount(remove);
            	}
            	else {
            		item.setAmount(0);
            	}
                break;
            }
        }
	}
	
	private Byte ConvertColorToID(int teamID) {
		switch (this.plugin.getConfig().getString(path+"TeamsList."+teamID+".Color")) {
		case "&c":
			return 1;
		case "&0":
			return 0;
		case "&e":
			return 11;
		case "&9":
			return 4;
		case "&a":
			return 10;
		case "&d":
			return 13;
		case "&b":
			return 12;
		case "&7":
			return 8;
		default:
			return 15;
		}
	}

	public void addPlayerInGame(Player player) {
		this.players.put(player.getName(), player);
	}
}
