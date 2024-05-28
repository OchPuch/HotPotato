package org.ccg.hotpotato.Tag.View;

import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.ccg.hotpotato.Disposable.IDisposable;
import org.ccg.hotpotato.HotPotato;
import org.ccg.hotpotato.Tag.Data.TagData;
import org.ccg.hotpotato.Tag.Events.GameGeneralEvents.BlocksCleared;
import org.ccg.hotpotato.Tag.Events.GameStagesEvents.GameSecondPassed;
import org.ccg.hotpotato.Tag.Events.GameStagesEvents.TagGameEnded;
import org.ccg.hotpotato.Tag.Events.GameStagesEvents.TagGameStartedPreparing;
import org.ccg.hotpotato.Tag.Events.GameStagesEvents.TagRoundStarted;
import org.ccg.hotpotato.Tag.Events.PlayerEvents.PlayerEliminated;
import org.ccg.hotpotato.Tag.Events.PlayerEvents.PlayerTaggedSuccessfully;
import org.ccg.hotpotato.Utils.Text.Component;

import java.util.Objects;

public class TagText implements Listener, IDisposable {

    private final TagData _data;

    private final String newHunterActionBarMessage = "&c&lТы водишь!";
    private final String newRunnerActionBarMessage = "&aТы убегаешь!";

    public TagText(TagData data) {
        _data = data;
        HotPotato.register(this);
    }

    public void Dispose() {
        Bukkit.getLogger().info("Disposing text");
        HotPotato.unregister(this);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onGameStartPreparing(TagGameStartedPreparing e) {
        if (!Objects.equals(e.getTagData().getGameID(), _data.getGameID())) return;
        Bukkit.broadcast(Component.color("&aИгра начинается!"));
        for (Player player : _data.getCurrentPlayers()) {
            player.sendActionBar(Component.color(""));
        }

    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onRoundStart(TagRoundStarted e) {
        if (!Objects.equals(e.getTagData().getGameID(), _data.getGameID())) return;
        for (Player player : _data.getCurrentPlayers()) {
            boolean hunter = _data.getHunters().contains(player);
            player.sendActionBar(Component.color(hunter ? newHunterActionBarMessage : newRunnerActionBarMessage));
            player.showTitle(Title.title(Component.color("&" + (hunter ? "c" : "a") + "&lИГРА НАЧАЛАСЬ!"),
                    Component.color("&" + (hunter ? "4Ты вода!" : "bТы убегаешь!"))));
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    private void onSecond(GameSecondPassed e)
    {
        if (!Objects.equals(e.getTagData().getGameID(), _data.getGameID())) return;
        if (e.getTagData().getGameState() == TagData.GameState.PreparingGame ||
                (e.getTagData().getGameState() == TagData.GameState.PreparingRound)) {
            Bukkit.broadcast(Component.color("&a"+_data.getTimeLeftInSeconds()+"..."));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onTagSuccess(PlayerTaggedSuccessfully e) {
        if (!_data.getCurrentPlayers().contains(e.get_tagger()) || !_data.getCurrentPlayers().contains(e.get_receiver()))
            return;
        Bukkit.broadcast(Component.color("&c" + e.get_tagger().getName() + "&f запятнал &4&l" + e.get_receiver().getName() + "&f!"));
        e.get_tagger().sendActionBar(Component.color(newRunnerActionBarMessage));
        e.get_receiver().sendActionBar(Component.color(newHunterActionBarMessage));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onBlocksCleared(BlocksCleared e) {
        if (!Objects.equals(e.getTagData().getGameID(), _data.getGameID())) return;
        Bukkit.broadcast(Component.color("&fОчистка блоков..."));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onPlayerEliminated(PlayerEliminated e) {
        if (!_data.getJoinedPlayersNames().contains(e.get_player().getName())) return;
        switch (e.get_cause()) {
            case Tagged -> {
                Bukkit.broadcast(Component.color("&c" + e.get_player().getName() + "&f выбывает из игры!"));
                e.get_player().showTitle(Title.title(Component.color("&c&lТы выбыл!"), Component.color("&fGG!")));
            }
            case Disconnected -> {
                Bukkit.broadcast(Component.color("&c" + e.get_player().getName() + "&f выбыл, так как отключился от игры!"));
                e.get_player().showTitle(Title.title(Component.color("&c&lТы выбыл!"), Component.color("&fGG!")));
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onGameEnd(TagGameEnded e) {
        if (!Objects.equals(e.getTagData().getGameID(), _data.getGameID())) return;
        Dispose();
    }
}
