package com.github.darksoulq.blueprint.command;

import com.github.darksoulq.abyssallib.server.command.BaseCommand;
import com.github.darksoulq.abyssallib.server.command.CommandResult;
import com.github.darksoulq.abyssallib.server.resource.asset.Font;
import com.github.darksoulq.blueprint.core.BlueprintRegistries;
import com.github.darksoulq.blueprint.core.codec.GlyphDef;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.Map;

public class DictionaryCommand extends BaseCommand {

    public DictionaryCommand() {
        super("dictionary", "dict");
        setRequirement(ctx -> ctx.getSender().hasPermission("blueprint.dictionary") || ctx.getSender().isOp());

        addSyntax(ctx -> {
            CommandSourceStack source = ctx.getSource();
            if (source.getExecutor() instanceof Player player) {
                ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
                BookMeta meta = (BookMeta) book.getItemMeta();
                meta.setTitle("Glyphs Dictionary");
                meta.setAuthor("Blueprint");

                Component page = Component.empty();
                int count = 0;

                for (Map.Entry<String, GlyphDef> entry : BlueprintRegistries.GLYPH_DEFS.getAll().entrySet()) {
                    if (!entry.getValue().showInDictionary()) continue;

                    String[] split = entry.getKey().split(":");
                    Component glyph = Font.getGlyph(split[0], split[1]);

                    if (glyph != null) {
                        page = page.append(glyph.color(NamedTextColor.WHITE)).append(Component.text(" - " + entry.getKey() + "\n"));
                        count++;

                        if (count >= 10) {
                            meta.addPages(page);
                            page = Component.empty();
                            count = 0;
                        }
                    }
                }

                if (count > 0) {
                    meta.addPages(page);
                }

                book.setItemMeta(meta);
                player.openBook(book);
            }
            return CommandResult.success();
        });
    }
}