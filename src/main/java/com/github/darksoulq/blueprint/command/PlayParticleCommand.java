package com.github.darksoulq.blueprint.command;

import com.github.darksoulq.abyssallib.server.command.BaseCommand;
import com.github.darksoulq.abyssallib.server.command.CommandResult;
import com.github.darksoulq.abyssallib.server.command.argument.RegistryEntryArgument;
import com.github.darksoulq.abyssallib.server.registry.Registry;
import com.github.darksoulq.blueprint.core.BlueprintRegistries;
import com.github.darksoulq.blueprint.core.particle.ParticleTemplate;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

public class PlayParticleCommand extends BaseCommand {

    public PlayParticleCommand() {
        super("playparticle");
        setRequirement(ctx -> ctx.getSender().hasPermission("blueprint.particle") || ctx.getSender().isOp());

        addSyntax(ctx -> {
            ParticleTemplate template = ctx.getArgument("particle", ParticleTemplate.class);
            String id = BlueprintRegistries.PARTICLE_TEMPLATES.getId(template);
            
            if (template == null) {
                ctx.getSource().getSender().sendMessage(MiniMessage.miniMessage().deserialize("<red>Particle template '" + id + "' not found.</red>"));
                return CommandResult.failure();
            }

            if (ctx.getSource().getExecutor() instanceof Player player) {
                template.build(player.getLocation()).start();
                player.sendMessage(MiniMessage.miniMessage().deserialize("<green>Playing particle effect: " + id + "</green>"));
            } else {
                ctx.getSource().getSender().sendMessage(MiniMessage.miniMessage().deserialize("<red>Only players can execute this command.</red>"));
            }

            return CommandResult.success();
        }, Commands.argument("particle", RegistryEntryArgument.registryEntry(BlueprintRegistries.PARTICLE_TEMPLATES)));
    }
}