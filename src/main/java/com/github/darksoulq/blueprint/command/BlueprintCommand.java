package com.github.darksoulq.blueprint.command;

import com.github.darksoulq.abyssallib.server.command.BaseCommand;
import com.github.darksoulq.blueprint.Blueprint;

public class BlueprintCommand extends BaseCommand {

    public BlueprintCommand(Blueprint plugin) {
        super("blueprint", "bp");
        addSubcommand(new DictionaryCommand());
        addSubcommand(new ReloadCommand(plugin));
        addSubcommand(new PlayParticleCommand());
    }
}