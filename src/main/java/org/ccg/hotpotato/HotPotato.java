package org.ccg.hotpotato;
import cloud.commandframework.CommandTree;
import cloud.commandframework.annotations.AnnotationParser;
import cloud.commandframework.arguments.parser.ParserParameters;
import cloud.commandframework.arguments.parser.StandardParameters;
import cloud.commandframework.bukkit.CloudBukkitCapabilities;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.minecraft.extras.MinecraftExceptionHandler;
import cloud.commandframework.paper.PaperCommandManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.ccg.hotpotato.Disposable.IDisposable;
import org.ccg.hotpotato.Global.GlobalHandler;
import org.ccg.hotpotato.Tag.TagGameManager;

import java.util.ArrayList;
import java.util.function.Function;

import static net.kyori.adventure.text.Component.text;

public final class HotPotato extends JavaPlugin {
    
    @Getter
    private static HotPotato instance;

    @Getter
    private static AnnotationParser<Player> annotationParser;

    @Getter
    private static PaperCommandManager<Player> commandManager;

    private ArrayList<IDisposable> disposables;

    @Override
    public void onEnable() {
        instance = this;
        
        disposables = new ArrayList<>();
        
        InitCommandManager();
        InitAnnotationParser();
        
        register(new GlobalHandler());
        
        TagGameManager tagGameManager = new TagGameManager();
        tagGameManager.Init();
    }

    @Override
    public void onDisable() {
        ArrayList<IDisposable> disposables = new ArrayList<>(this.disposables);
        for (int i =0; i < disposables.size(); i++) {
            disposables.get(i).Dispose();
        }
        disposables.clear();
        this.disposables.clear();
    }

    public static void register(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, instance);
        if (listener instanceof IDisposable disposable) {
            instance.disposables.add(disposable);
        }
    }

    public static void unregister(Listener listener) {
        HandlerList.unregisterAll(listener);
        if (listener instanceof IDisposable disposable) {
            instance.disposables.remove(disposable);
        }
    }
    
    private void InitCommandManager() {
        
        final Function<CommandTree<Player>, CommandExecutionCoordinator<Player>> executionCoordinatorFunction =
                CommandExecutionCoordinator.simpleCoordinator();

        final Function<CommandSender, Player> mapperFunction = (a) -> {
            if (a instanceof Player p) return p;
            return null;
        };
        
        final Function<Player, CommandSender> mapperFunctionBackwards = (a) -> a;
        
        try {
            commandManager = new PaperCommandManager<>(
                    /* Owning plugin */ this,
                    /* Coordinator function */ executionCoordinatorFunction,
                    /* Command Sender -> C */ mapperFunction,
                    /* C -> Command Sender */ mapperFunctionBackwards
            );
        } catch (final Exception e) {
            this.getLogger().severe("Failed to initialize the command manager");
            /* Disable the plugin */
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (commandManager.hasCapability(CloudBukkitCapabilities.BRIGADIER)) {
            commandManager.registerBrigadier();
        }

        if (commandManager.hasCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
            commandManager.registerAsynchronousCompletions();
        }
    }
    
    private void InitAnnotationParser() {
        final Function<ParserParameters, CommandMeta> commandMetaFunction = p ->
                CommandMeta.simple()
                        // This will allow you to decorate commands with descriptions
                        .with(CommandMeta.DESCRIPTION, p.get(StandardParameters.DESCRIPTION, "No description"))
                        .build();
        annotationParser = new AnnotationParser<>(
                /* Manager */ commandManager,
                /* Command sender type */ Player.class,
                /* Mapper for command meta instances */ commandMetaFunction
        );

        new MinecraftExceptionHandler<Player>()
                .withInvalidSyntaxHandler()
                .withInvalidSenderHandler()
                .withNoPermissionHandler()
                .withArgumentParsingHandler()
                .withCommandExecutionHandler()
                .withDecorator(
                        component -> text().append(component).build()
                ).apply(commandManager, c -> c);
    }
    
    
}
