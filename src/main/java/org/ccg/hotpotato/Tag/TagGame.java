package org.ccg.hotpotato.Tag;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.ccg.hotpotato.Tag.Data.TagData;
import org.ccg.hotpotato.Tag.Data.TagGameResult;
import org.ccg.hotpotato.Tag.Events.GameStagesEvents.*;
import org.ccg.hotpotato.Tag.Events.PlayerEvents.PlayerEliminated;
import org.ccg.hotpotato.Tag.GameHandlers.*;
import org.ccg.hotpotato.Utils.Cooldown.ServerCooldown;

import java.util.ArrayList;

public class TagGame {

    private final TagData _data;
    private final TagGameResult _gameResult;

    public TagGame(TagData data) {
        _data = data;
        TagGameModelHandler tagGameplayHandler = new TagGameModelHandler(_data);
        TagBlockHandler tagBlockHandler = new TagBlockHandler(_data);
        TagTotemHandler tagTotemHandler = new TagTotemHandler(_data);
        TagPlayersHandler tagPlayersHandler = new TagPlayersHandler(_data);
        TagWorldHandler tagWorldHandler = new TagWorldHandler(_data);
        TagReturnHeightItemsHandler tagReturnHeightItemsHandler = new TagReturnHeightItemsHandler(_data);
        TagTntHandler tntHandler = new TagTntHandler(_data);    
        TagPickaxeHandler pickaxeHandler = new TagPickaxeHandler(_data);
        TagEffectsHandler tagEffectsHandler = new TagEffectsHandler(_data);
        _gameResult = new TagGameResult();
    }

    public void StartGame() {
        _data.setGameState(TagData.GameState.PreparingGame);
        Bukkit.getPluginManager().callEvent(new TagGameStartedPreparing(_data));
        new ServerCooldown(_data.getConfig().get_gameStartDelay()) {
            @Override
            public void onTick() {
                _data.getTimeLeftInTicks().set(ticks);
            }

            @Override
            public void onSecond() {
                Bukkit.getPluginManager().callEvent(new GameSecondPassed(_data));
            }

            @Override
            public void onEnd() {
                Bukkit.getPluginManager().callEvent(new TagGameStarted(_data));
                StartNewRound();
            }

            @Override
            public void onCancel() {

            }
        };
    }

    private void PrepareAndStartNewRound() {
        _data.getWorldData().UpdateBorderSizeByPlayers(_data.getCurrentPlayers().size());
        _data.setGameState(TagData.GameState.PreparingRound);
        Bukkit.getPluginManager().callEvent(new TagRoundStartedPreparing(_data));
        new ServerCooldown(_data.getConfig().get_roundStartDelay()) {
            @Override
            public void onTick() {
                _data.getTimeLeftInTicks().set(ticks);
            }

            @Override
            public void onSecond() {
                Bukkit.getPluginManager().callEvent(new GameSecondPassed(_data));
            }

            @Override
            public void onEnd() {
                StartNewRound();
            }

            @Override
            public void onCancel() {

            }
        };

    }

    private void StartNewRound() {
        _data.setGameState(TagData.GameState.Running);
        TagHunterSelector.RandomSelection(_data);
        Bukkit.getPluginManager().callEvent(new TagRoundStarted(_data));
        new ServerCooldown(_data.getConfig().get_roundDuration()) {
            @Override
            public void onTick() {
                _data.getTimeLeftInTicks().set(ticks);
                
            }

            @Override
            public void onSecond() {
                Bukkit.getPluginManager().callEvent(new GameSecondPassed(_data));
                if (_data.getCurrentPlayers().size() <= 1 && _data.getGameState() != TagData.GameState.Ending) {
                    EndGame();
                    cancel();
                }
            }

            @Override
            public void onEnd() {
                Bukkit.getPluginManager().callEvent(new TagRoundEnded(_data));
                EliminateHunters();
                if (_data.getCurrentPlayers().size() <= 1) {
                    EndGame();
                } else {
                    PrepareAndStartNewRound();
                }
            }

            @Override
            public void onCancel() {
            }
        };
    }

    private void EliminateHunters() {
        ArrayList<Player> playersToEliminate = new ArrayList<>(_data.getHunters());
        for (int i = 0; i < playersToEliminate.size(); i++) {
            Player player = playersToEliminate.get(i);
            Bukkit.getPluginManager().callEvent(new PlayerEliminated(player, _data, PlayerEliminated.EliminationCause.Tagged));
        }
    }

    private void EndGame() {
        _data.setGameState(TagData.GameState.Ending);
        Bukkit.getPluginManager().callEvent(new TagGameEnded(_data, _gameResult));
        TagGameManager.currentGames.remove(_data);
    }

}
