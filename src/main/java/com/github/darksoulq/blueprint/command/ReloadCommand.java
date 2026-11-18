package com.github.darksoulq.blueprint.command;

import com.github.darksoulq.abyssallib.server.command.BaseCommand;
import com.github.darksoulq.abyssallib.server.command.CommandResult;
import com.github.darksoulq.blueprint.Blueprint;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class ReloadCommand extends BaseCommand {

    public ReloadCommand(Blueprint plugin) {
        super("reload");
        setRequirement(ctx -> ctx.getSender().hasPermission("blueprint.reload") || ctx.getSender().isOp());
        
        addSyntax(ctx -> {
            plugin.reload();
            ctx.getSource().getSender().sendMessage(MiniMessage.miniMessage().deserialize("<green>Blueprint reloaded successfully!</green>"));
            return CommandResult.success();
        });
    }
}