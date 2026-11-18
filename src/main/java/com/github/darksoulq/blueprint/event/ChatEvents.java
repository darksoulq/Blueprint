package com.github.darksoulq.blueprint.event;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent;
import com.github.darksoulq.abyssallib.server.event.SubscribeEvent;
import com.github.darksoulq.abyssallib.server.resource.asset.Font;
import com.github.darksoulq.blueprint.core.BlueprintRegistries;
import com.github.darksoulq.blueprint.core.codec.GlyphDef;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;

public class ChatEvents {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onChat(AsyncChatEvent event) {
        Player player = event.getPlayer();

        TextReplacementConfig replacementConfig = TextReplacementConfig.builder()
            .match(":[a-zA-Z0-9_:]+:")
            .replacement((MatchResult match, TextComponent.Builder textBuilder) -> {
                String full = match.group();
                String name = full.substring(1, full.length() - 1);

                String targetId = null;
                GlyphDef targetDef = null;

                if (BlueprintRegistries.GLYPH_DEFS.getAll().containsKey(name)) {
                    targetId = name;
                    targetDef = BlueprintRegistries.GLYPH_DEFS.get(name);
                } else {
                    for (Map.Entry<String, GlyphDef> entry : BlueprintRegistries.GLYPH_DEFS.getAll().entrySet()) {
                        String[] split = entry.getKey().split(":");
                        if (split[1].equals(name)) {
                            targetId = entry.getKey();
                            targetDef = entry.getValue();
                            break;
                        }
                    }
                }

                if (targetDef != null && targetDef.allowInChat()) {
                    if (targetDef.chatPermission().isEmpty() || player.hasPermission(targetDef.chatPermission())) {
                        String[] split = targetId.split(":");
                        Component glyph = Font.getGlyph(split[0], split[1]);
                        if (glyph != null) {
                            return glyph.color(NamedTextColor.WHITE);
                        }
                    }
                }

                return Component.text(full);
            })
            .build();

        event.message(event.message().replaceText(replacementConfig));
    }
}