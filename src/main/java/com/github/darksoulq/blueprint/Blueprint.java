package com.github.darksoulq.blueprint;

import com.github.darksoulq.abyssallib.server.command.CommandBus;
import com.github.darksoulq.abyssallib.server.event.EventBus;
import com.github.darksoulq.abyssallib.server.scheduler.Scheduler;
import com.github.darksoulq.blueprint.command.BlueprintCommand;
import com.github.darksoulq.blueprint.core.ContentLoader;
import com.github.darksoulq.blueprint.core.ResourcesHolder;
import com.github.darksoulq.blueprint.core.scripting.ScriptManager;
import com.github.darksoulq.blueprint.event.ChatEvents;
import com.github.darksoulq.blueprint.event.custom.BlueprintLoadEvent;
import com.github.darksoulq.blueprint.event.custom.BlueprintReloadEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class Blueprint extends JavaPlugin {

    public static final String PLUGIN_ID = "blueprint";
    private EventBus eventBus;
    private ResourcesHolder resourcesHolder;
    private ContentLoader contentLoader;
    private Scheduler scheduler;
    private ScriptManager scriptManager;

    @Override
    public void onEnable() {
        this.scheduler = new Scheduler(this);
        this.scriptManager = new ScriptManager(this.scheduler);
        this.eventBus = new EventBus(this);
        this.resourcesHolder = new ResourcesHolder(this);
        this.contentLoader = new ContentLoader(this, this.resourcesHolder, this.scriptManager);

        this.contentLoader.loadAll();
        this.resourcesHolder.registerPack();

        this.eventBus.register(new ChatEvents());

        CommandBus.register(PLUGIN_ID, new BlueprintCommand(this));

        EventBus.post(new BlueprintLoadEvent());
    }

    @Override
    public void onDisable() {
        this.resourcesHolder.unregisterPack();
    }

    public void reload() {
        this.resourcesHolder.unregisterPack();
        this.resourcesHolder.reloadPack();
        this.contentLoader.loadAll();
        this.resourcesHolder.registerPack();

        CommandBus.reloadAll();

        EventBus.post(new BlueprintReloadEvent());
    }

    public static Blueprint getPlugin() {
        return JavaPlugin.getPlugin(Blueprint.class);
    }
}