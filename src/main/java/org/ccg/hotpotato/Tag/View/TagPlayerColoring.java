package org.ccg.hotpotato.Tag.View;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.Team;
import org.ccg.hotpotato.Disposable.IDisposable;
import org.ccg.hotpotato.HotPotato;
import org.ccg.hotpotato.Tag.Data.TagData;
import org.ccg.hotpotato.Tag.Events.GameStagesEvents.TagGameEnded;
import org.ccg.hotpotato.Tag.Events.GameStagesEvents.TagGameStartedPreparing;
import org.ccg.hotpotato.Tag.Events.GameStagesEvents.TagRoundStarted;
import org.ccg.hotpotato.Tag.Events.GameStagesEvents.TagRoundStartedPreparing;
import org.ccg.hotpotato.Tag.Events.PlayerEvents.PlayerEliminated;
import org.ccg.hotpotato.Tag.Events.PlayerEvents.PlayerTaggedSuccessfully;
import org.ccg.hotpotato.Utils.Text.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TagPlayerColoring implements Listener, IDisposable {
    private final TagData _data;

    private Team huntersTeam;
    private final String huntersTeamName = "hunters";
    
    private Team runnersTeam;
    private final String runnersTeamName = "runners";
    
    private Team spectatorsTeam;
    private final String spectatorsTeamName = "spectators";
    
    private List<Team> teams;
    
    public TagPlayerColoring(TagData data) {
        _data = data;
        teams = new ArrayList<>();

        //choose random player
        Player p = _data.getCurrentPlayers().stream().findFirst().orElse(null);
        if (p == null) throw new NullPointerException("No players in game");
        
        var sb = p.getScoreboard();
        huntersTeam = sb.getTeam(huntersTeamName);
        if (huntersTeam == null) {
            huntersTeam = sb.registerNewTeam(huntersTeamName);
            huntersTeam.color(NamedTextColor.RED);
            huntersTeam.prefix(Component.color("&c&lH "));
            huntersTeam.setAllowFriendlyFire(false);
        }
        
        teams.add(huntersTeam);

        runnersTeam = sb.getTeam(runnersTeamName);
        if (runnersTeam == null) {
            runnersTeam = sb.registerNewTeam(runnersTeamName);
            runnersTeam.color(NamedTextColor.YELLOW);
            runnersTeam.prefix(Component.color("&9&lR "));
        }
        
        teams.add(runnersTeam);

        spectatorsTeam = sb.getTeam(spectatorsTeamName);
        if (spectatorsTeam == null) {
            spectatorsTeam = sb.registerNewTeam(spectatorsTeamName);
            spectatorsTeam.color(NamedTextColor.GRAY);
            spectatorsTeam.prefix(Component.color("&7[SPEC] "));
        }
        
        teams.add(spectatorsTeam);

        HotPotato.register(this);
    }
    
    public void Dispose() {
        Bukkit.getLogger().info("Disposing coloring");
        for (var playerName : _data.getJoinedPlayersNames()) {
            Player player = Bukkit.getPlayer(playerName);
            if (player != null) {
                RemoveFromAllTeams(player);
                continue;
            }
            player = Bukkit.getOfflinePlayer(playerName).getPlayer();
            if (player != null) {
                RemoveFromAllTeams(player);
                continue;
            }
        }
        for (var team : teams) {
            team.unregister();
        }
        HotPotato.unregister(this);
    }
    
    private void RemoveFromAllTeams(Player p) {
        for (var team : teams) {
            team.removeEntry(p.getName());
        }
        p.setGlowing(false);
    }
    
    private void AddToHuntersTeam(Player p) {
        RemoveFromAllTeams(p);
        huntersTeam.addEntry(p.getName());
        p.setGlowing(false);
    }
    
    private void AddToRunnersTeam(Player p) {
        RemoveFromAllTeams(p);
        runnersTeam.addEntry(p.getName());
        p.setGlowing(true);
    }
    
    private void AddToSpectatorsTeam(Player p) {
        RemoveFromAllTeams(p);
        spectatorsTeam.addEntry(p.getName());
        p.setGlowing(false);
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    private void onRoundPreparingStarted(TagRoundStartedPreparing e) {
        if (!Objects.equals(e.getTagData().getGameID(), _data.getGameID())) return;
        for (var player : _data.getCurrentPlayers()) {
            RemoveFromAllTeams(player);
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    private void onGamePreparingStarted(TagGameStartedPreparing e) {
        if (!Objects.equals(e.getTagData().getGameID(), _data.getGameID())) return;
        for (var player : _data.getCurrentPlayers()) {
            RemoveFromAllTeams(player);
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    private void onRoundStart(TagRoundStarted e) {
        if (!Objects.equals(e.getTagData().getGameID(), _data.getGameID())) return;
        for (var player : _data.getHunters()) {
            AddToHuntersTeam(player);
        }
        for (var player : _data.getRunners()) {
            AddToRunnersTeam(player);
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    private void onSuccessfulTag(PlayerTaggedSuccessfully e) {
        if (!_data.getCurrentPlayers().contains(e.get_receiver())) return;
        if (!_data.getCurrentPlayers().contains(e.get_tagger())) return;
        AddToHuntersTeam(e.get_receiver());
        AddToRunnersTeam(e.get_tagger());
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    private void onEliminatePlayer(PlayerEliminated e) {
        if (!_data.getJoinedPlayersNames().contains(e.get_player().getName())) return;
        AddToSpectatorsTeam(e.get_player());
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    private void onGameEnd(TagGameEnded e) {
        if (!Objects.equals(e.getTagData().getGameID(), _data.getGameID())) return;
        Dispose();
    }
}
