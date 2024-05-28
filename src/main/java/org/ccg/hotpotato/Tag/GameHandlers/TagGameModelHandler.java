package org.ccg.hotpotato.Tag.GameHandlers;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.ccg.hotpotato.Disposable.IDisposable;
import org.ccg.hotpotato.HotPotato;
import org.ccg.hotpotato.Tag.Data.TagData;
import org.ccg.hotpotato.Tag.Events.GameStagesEvents.TagGameEnded;
import org.ccg.hotpotato.Tag.Events.GameStagesEvents.TagGameStartedPreparing;
import org.ccg.hotpotato.Tag.Events.GameStagesEvents.TagRoundStarted;
import org.ccg.hotpotato.Tag.Events.GameStagesEvents.TagRoundStartedPreparing;
import org.ccg.hotpotato.Tag.Events.PlayerEvents.PlayerEliminated;
import org.ccg.hotpotato.Tag.Events.PlayerEvents.PlayerTaggedSuccessfully;
import org.ccg.hotpotato.Tag.TagGameManager;

import java.util.HashSet;
import java.util.Objects;

public class TagGameModelHandler implements Listener, IDisposable {
    private final TagData _data;
    private final HashSet<OfflinePlayer> disconnectedWhilePreparing = new HashSet<>();

    public TagGameModelHandler(TagData data) {
        _data = data;
        HotPotato.register(this);
    }

    @Override
    public void Dispose() {
        HotPotato.unregister(this);
    }

    private void PreparePlayer(Player p) {
        p.teleport(_data.getWorldData().getSpawnLocation());
        p.setGameMode(org.bukkit.GameMode.SURVIVAL);
        p.setHealth(20);
        p.setFoodLevel(20);
        p.setSaturation(20);
        p.setExp(0);
        p.setLevel(0);
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);
        p.setAllowFlight(false);
        p.setFlying(false);
        p.setFireTicks(0);
        p.setFallDistance(0);
        p.setVelocity(p.getVelocity().setY(0));
        p.setWalkSpeed(0.2f);
        p.setFlySpeed(0.1f);
        p.setCollidable(true);
        p.setInvulnerable(false);
        p.setInvisible(false);
        p.closeInventory();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onGameStartedPreparing(TagGameStartedPreparing e) {
        if (!Objects.equals(e.getTagData().getGameID(), _data.getGameID())) return;
        Bukkit.getLogger().severe("Preparing players");
        for (Player p : _data.getCurrentPlayers()) {
            PreparePlayer(p);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onRoundStartedPreparing(TagRoundStartedPreparing e) {
        if (!Objects.equals(e.getTagData().getGameID(), _data.getGameID())) return;
        for (Player p : _data.getCurrentPlayers()) {
            PreparePlayer(p);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onRoundStarted(TagRoundStarted e) {
        if (!Objects.equals(e.getTagData().getGameID(), _data.getGameID())) return;
        for (OfflinePlayer player : disconnectedWhilePreparing) {
            Bukkit.getPluginManager().callEvent(new PlayerEliminated(player.getPlayer(), _data, PlayerEliminated.EliminationCause.Disconnected));
        }
        disconnectedWhilePreparing.clear();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onGameEnd(TagGameEnded e) {
        if (!Objects.equals(e.getTagData().getGameID(), _data.getGameID())) return;
        for (String playerName : _data.getJoinedPlayersNames()) {
            Player onlinePlayer = Bukkit.getPlayer(playerName);
            if (onlinePlayer != null) PreparePlayer(onlinePlayer);
        }
        Dispose();
    }

    @EventHandler
    private void onSuccessfulTagged(PlayerTaggedSuccessfully e) {
        if (!_data.getCurrentPlayers().contains(e.get_receiver()) || !_data.getCurrentPlayers().contains(e.get_tagger()))
            return;
        _data.getRunners().remove(e.get_receiver());
        _data.getRunners().add(e.get_tagger());
        _data.getHunters().remove(e.get_tagger());
        _data.getHunters().add(e.get_receiver());
    }

    @EventHandler
    private void onPlayerEliminated(PlayerEliminated e) {
        if (!_data.getJoinedPlayersNames().contains(e.get_player().getName())) {
            return;
        }
        _data.getCurrentPlayers().remove(e.get_player());
        _data.getHunters().remove(e.get_player());
        _data.getRunners().remove(e.get_player());
        _data.getSpectators().add(e.get_player());
        e.get_player().setGameMode(org.bukkit.GameMode.SPECTATOR);
    }

    @EventHandler
    void onJoin(PlayerJoinEvent e) {
        if (!_data.getJoinedPlayersNames().contains(e.getPlayer().getName())) return;
        if (_data.getGameState() == TagData.GameState.PreparingGame) {
            for (OfflinePlayer offlinePlayer : disconnectedWhilePreparing) {
                if (Objects.equals(offlinePlayer.getName(), e.getPlayer().getName())) {
                    disconnectedWhilePreparing.remove(offlinePlayer);
                    _data.getCurrentPlayers().add(e.getPlayer());
                    return;
                }
            }
        }
    }

    @EventHandler
    void onLeave(PlayerQuitEvent e) {
        if (!_data.getJoinedPlayersNames().contains(e.getPlayer().getName())) return;
        if (_data.getGameState() == TagData.GameState.PreparingGame) {
            disconnectedWhilePreparing.add(e.getPlayer());
            _data.getCurrentPlayers().remove(e.getPlayer());
            TagGameManager.disconnectedFromTagPlayers.put(e.getPlayer().getName(), _data.getGameID());
        } else {
            Bukkit.getPluginManager().callEvent(new PlayerEliminated(e.getPlayer(), _data, PlayerEliminated.EliminationCause.Disconnected));
            TagGameManager.disconnectedFromTagPlayers.put(e.getPlayer().getName(), _data.getGameID());
        }
    }

    @EventHandler
    void onKicked(PlayerKickEvent e) {
        if (!_data.getCurrentPlayers().contains(e.getPlayer())) return;
        Bukkit.getPluginManager().callEvent(new PlayerEliminated(e.getPlayer(), _data, PlayerEliminated.EliminationCause.Kicked));
    }

}
