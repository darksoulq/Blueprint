package com.github.darksoulq.blueprint.core.processor;

import com.github.darksoulq.abyssallib.common.config.Config;
import com.github.darksoulq.abyssallib.common.serialization.Codec;
import com.github.darksoulq.abyssallib.common.serialization.Codecs;
import com.github.darksoulq.abyssallib.server.resource.asset.Font;
import com.github.darksoulq.abyssallib.server.resource.asset.Texture;
import com.github.darksoulq.blueprint.Blueprint;
import com.github.darksoulq.blueprint.core.BlueprintRegistries;
import com.github.darksoulq.blueprint.core.ResourcesHolder;
import com.github.darksoulq.blueprint.core.codec.BlueprintCodecs;
import com.github.darksoulq.blueprint.core.codec.GlyphDef;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class GlyphProcessor implements ContentProcessor {

    @Override
    public void process(String namespace, File parentDir, File dataFile, ResourcesHolder holder) {
        String nameNoExt = dataFile.getName().replace(".yml", "");
        Config config = new Config(parentDir, nameNoExt);
        Config.Value<Map<String, GlyphDef>> glyphs = config.value("glyphs", new HashMap<>(), Codec.map(Codecs.STRING, BlueprintCodecs.GLYPH_DEF));

        if (glyphs.get() == null) {
            Blueprint.getPlugin().getLogger().warning("Failed to parse glyphs in " + dataFile.getName());
            return;
        }

        if (glyphs.get().isEmpty()) return;

        Font font = holder.getPack().namespace(namespace).font(nameNoExt, false);

        for (Map.Entry<String, GlyphDef> entry : glyphs.get().entrySet()) {
            GlyphDef def = entry.getValue();
            if ("bitmap".equals(def.type())) {
                byte[] data = holder.getAsset(namespace, "textures", def.file() + ".png");
                if (data != null) {
                    Texture tex = holder.getPack().namespace(namespace).texture(def.file(), data);
                    font.glyph(entry.getKey(), tex, def.height(), def.ascent());
                    BlueprintRegistries.GLYPH_DEFS.register(namespace + ":" + entry.getKey(), def);
                    BlueprintRegistries.GLYPH_COUNT++;
                }
            }
        }
    }
}