package fr.loyle.ktn.listener;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import fr.loyle.ktn.KTN;

public class PlayerListener implements Listener {

	private KTN plugin;
	private String path = "KTN.";
	private int deathTask;
	private Player playerToKick;
	private int kickCountDown = 0;

	public PlayerListener(KTN pl) {
		this.plugin = pl;
	}

	// EVENT connexion: on ajoute le joueur à la liste des participants
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerLogin(PlayerLoginEvent e) {
		Player player = e.getPlayer();
		if (this.plugin.game.getGameManager().getIsStart() == true) {
			if (this.plugin.game.getPlayerManager().checkPlayer(player)) {
				this.plugin.game.getPlayerManager().addPlayerInGame(player);
			}
			else if (this.plugin.getConfig().getBoolean(path + "AllowReconnect") && this.plugin.game.getPlayerManager().checkPlayer(player) == false) {
				player.sendMessage(ChatColor.RED + "Vous regardez la partie");
			}
			else if (this.plugin.getConfig().getBoolean(path + "AllowReconnect") == false && this.plugin.game.getPlayerManager().checkPlayer(player) == false) {
				e.setKickMessage("Partie en cours... accès interdit");
				e.setResult(Result.KICK_OTHER);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		if (this.plugin.game.getGameManager().getIsStart() == false) {
			this.plugin.game.getPlayerManager().addPlayer(player);
		}
	}

	// EVENT déconnexion: On supprime le joueur de la liste des participants
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDisconnect(PlayerQuitEvent e) {
		if (this.plugin.game.getGameManager().getIsStart() == false) {
			Player player = e.getPlayer();
			this.plugin.game.getPlayerManager().removePlayer(player);
			e.setQuitMessage(ChatColor.YELLOW + player.getName() + " a quitté la partie (" + this.plugin.game.getPlayerManager().getNumberPlayers() + "/" + this.plugin.game.getGameManager().getMaxPlayers() + ")");
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onWeahterChange(WeatherChangeEvent e) {
		if (!this.plugin.getConfig().getBoolean(path + "Weather")) {
			if (this.plugin.game.getGameManager().getIsStart()) {
				e.setCancelled(true);
			}
			else {
				e.getWorld().setThunderDuration(0);
				e.getWorld().setThundering(false);
				e.getWorld().setWeatherDuration(0);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();

		e.getDrops().add(new ItemStack(Material.GOLDEN_APPLE));

		for (Player player : this.plugin.game.getPlayerManager().getPlayers()) {
			player.playSound(player.getLocation(), Sound.WITHER_SPAWN, 20, 1);
		}
		this.plugin.game.getPlayerManager().removePlayer(p);
		this.plugin.game.getPlayerManager().addSpectators(p);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		final Player player = e.getPlayer();
		new BukkitRunnable() {
			@Override
			public void run() {
				Double X = PlayerListener.this.plugin.getConfig().getDouble(path + "Lobby.Spawn.X");
				Double Y = PlayerListener.this.plugin.getConfig().getDouble(path + "Lobby.Spawn.Y");
				Double Z = PlayerListener.this.plugin.getConfig().getDouble(path + "Lobby.Spawn.Z");
				Float Yaw = (float) PlayerListener.this.plugin.getConfig().getDouble(path + "Lobby.Spawn.YAW");
				Float Pitch = (float) PlayerListener.this.plugin.getConfig().getDouble(path + "Lobby.Spawn.PITCH");
				World World = Bukkit.getServer().getWorld(PlayerListener.this.plugin.getConfig().getString(path + "Lobby.WorldName"));

				Location l = new Location(World, X, Y, Z, Yaw, Pitch);
				player.teleport(l);
				player.setGameMode(GameMode.SPECTATOR);

				PlayerListener.this.playerToKick = player;
				PlayerListener.this.kickCountDown = PlayerListener.this.plugin.getConfig().getInt(path + "KickOnDeath.After");

				if (PlayerListener.this.plugin.getConfig().getBoolean(path + "KickOnDeath.Kick")) {
					PlayerListener.this.deathTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(PlayerListener.this.plugin, new Runnable() {
						public void run() {
							if (PlayerListener.this.kickCountDown <= -1) {
								PlayerListener.this.playerToKick.kickPlayer("JayJay");
								Bukkit.getScheduler().cancelTask(PlayerListener.this.deathTask);
							}
							else {
								PlayerListener.this.kickCountDown--;
							}
						}
					}, 0L, 20L);
				}
			}
		}.runTaskLater(this.plugin, 1);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			if (!this.plugin.game.getGameManager().getDamageStatus()) {
				e.setCancelled(true);
			}
			else if (this.plugin.game.getGameManager().getIsStart() == false) {
				e.setCancelled(true);
			}
			return;
		}
		return;
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST)
	public void craftItem(PrepareItemCraftEvent e) {
		Material itemType = e.getRecipe().getResult().getType();
		Byte itemData = e.getRecipe().getResult().getData().getData();
		if (itemType == Material.GOLDEN_APPLE && itemData == 1) {
			e.getInventory().setResult(new ItemStack(Material.AIR));
		}
		
		if(!this.plugin.getConfig().getBoolean(path + "DiamondCraft.Helmet") && itemType == Material.DIAMOND_HELMET) {
			e.getInventory().setResult(new ItemStack(Material.AIR));
		}
		if(!this.plugin.getConfig().getBoolean(path + "DiamondCraft.ChestPlate") && itemType == Material.DIAMOND_CHESTPLATE) {
			e.getInventory().setResult(new ItemStack(Material.AIR));
		}
		if(!this.plugin.getConfig().getBoolean(path + "DiamondCraft.Leggings") && itemType == Material.DIAMOND_LEGGINGS) {
			e.getInventory().setResult(new ItemStack(Material.AIR));
		}
		if(!this.plugin.getConfig().getBoolean(path + "DiamondCraft.Boots") && itemType == Material.DIAMOND_BOOTS) {
			e.getInventory().setResult(new ItemStack(Material.AIR));
		}
		if(!this.plugin.getConfig().getBoolean(path + "DiamondCraft.Sword") && itemType == Material.DIAMOND_SWORD) {
			e.getInventory().setResult(new ItemStack(Material.AIR));
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		if (this.plugin.game.getGameManager().getIsStart() == false) {
			Player player = e.getPlayer();
			Action action = e.getAction();

			ItemStack hand = player.getItemInHand();
			if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
				for (int i = 0; i <= this.plugin.getConfig().getInt(path + "Teams.Numbers") - 1; i++) {
					if (hand.getType().equals(Material.BANNER) && hand.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Rejoindre la team " + this.plugin.game.getTeamsManager().getColor(i) + this.plugin.game.getTeamsManager().getName(i))) {
						this.plugin.game.getTeamsManager().joinTeam(i, player);
						e.setCancelled(true);
						return;
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerMoveItem(InventoryClickEvent e) {
		ItemStack clicked = e.getCurrentItem();
		Player player = (Player) e.getWhoClicked();
		if (e.getCurrentItem() == null) {
			return;
		}

		if (this.plugin.game.getGameManager().getIsStart() == false) {

			for (int i = 0; i <= this.plugin.getConfig().getInt(path + "Teams.Numbers") - 1; i++) {
				if (clicked.getType().equals(Material.BANNER) && clicked.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Rejoindre la team " + this.plugin.game.getTeamsManager().getColor(i) + this.plugin.game.getTeamsManager().getName(i))) {
					this.plugin.game.getTeamsManager().joinTeam(i, player);
					e.setCancelled(true);
					return;
				}
			}
		}

		if (this.plugin.game.getGameManager().getIsStart() == true) {
			if (clicked.getType().equals(Material.GLOWSTONE_DUST) && this.plugin.getConfig().getBoolean(path + "PotionLvlDeux") == false) {
				player.getInventory().remove(clicked);
				player.updateInventory();
				e.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDropItem(PlayerDropItemEvent e) {
		if (this.plugin.game.getGameManager().getIsStart() == false) {
			ItemStack drop = e.getItemDrop().getItemStack();
			for (int i = 0; i <= this.plugin.getConfig().getInt(path + "Teams.Numbers") - 1; i++) {
				if (drop.getType().equals(Material.BANNER) && drop.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Rejoindre la team " + this.plugin.game.getTeamsManager().getColor(i) + this.plugin.game.getTeamsManager().getName(i))) {
					e.setCancelled(true);
					return;
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerSendMessage(AsyncPlayerChatEvent e) {
		String message = e.getMessage();
		Player player = e.getPlayer();

		if (this.plugin.getConfig().getBoolean(path + "TaupeMode.Activated") && this.plugin.getConfig().getBoolean(path + "PlayWithTeams")) {
			if (this.plugin.game.getModsManager().getTaupeMod().isInTchat(player)) {
				this.plugin.game.getModsManager().getTaupeMod().sendMsgToTaupes(player, message);
				e.setCancelled(true);
			}
			else {
				int teamID = this.plugin.game.getTeamsManager().getTeamOfPlayer(player);
				if (teamID != -1) {
					this.plugin.getServer().broadcastMessage(this.plugin.game.getTeamsManager().getColor(teamID) + "<" + player.getName() + "> " + ChatColor.WHITE + message);
					e.setCancelled(true);
				}
			}
		}
		else {
			int teamID = this.plugin.game.getTeamsManager().getTeamOfPlayer(player);
			if (teamID != -1) {
				this.plugin.getServer().broadcastMessage(this.plugin.game.getTeamsManager().getColor(teamID) + "<" + player.getName() + "> " + ChatColor.WHITE + message);
				e.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerPickupItem(PlayerPickupItemEvent e) {
		if (this.plugin.game.getGameManager().getIsStart() == true) {
			ItemStack drop = e.getItem().getItemStack();
			if (drop.getType().equals(Material.GLOWSTONE_DUST) && this.plugin.getConfig().getBoolean(path + "PotionLvlDeux") == false) {
				e.getItem().remove();
				e.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBrew(BrewEvent e) {
		if (this.plugin.game.getGameManager().getIsStart() == true && this.plugin.getConfig().getBoolean(path + "PotionLvlDeux") == false) {
			BrewerInventory inv = e.getContents();

			if (inv.getIngredient().getType().equals(Material.GLOWSTONE_DUST)) {
				e.getContents().clear();
				e.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void potionStrengthNerf(EntityDamageByEntityEvent event) {
		if ((event.getDamager() instanceof Player)) {
			Player player = (Player) event.getDamager();
			if (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
				for (PotionEffect Effect : player.getActivePotionEffects()) {
					if (Effect.getType().equals(PotionEffectType.INCREASE_DAMAGE)) {
						double DamagePercentage = (Effect.getAmplifier() + 1) * 1.3D + 1.0D;
						int NewDamage;
						if (event.getDamage() / DamagePercentage <= 1.0D) {
							NewDamage = (Effect.getAmplifier() + 1) * 3 + 1;
						}
						else {
							NewDamage = (int) (event.getDamage() / DamagePercentage) + (Effect.getAmplifier() + 1) * 3;
						}
						event.setDamage(NewDamage);
						break;
					}
				}
			}
		}
	}
}

