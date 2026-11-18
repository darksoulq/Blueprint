package com.github.darksoulq.blueprint.core.codec;

import com.github.darksoulq.abyssallib.common.serialization.Codec;
import com.github.darksoulq.abyssallib.common.serialization.Codecs;
import com.github.darksoulq.abyssallib.common.serialization.DataResult;
import com.github.darksoulq.abyssallib.common.serialization.RecordBuilder;
import com.github.darksoulq.blueprint.core.util.MathUtils;
import org.bukkit.Color;

import java.util.List;

public class BlueprintCodecs {

    public static final Codec<Double> EXPR_DOUBLE = Codec.fallback(
        Codecs.DOUBLE,
        Codecs.STRING.flatXmap(
            s -> {
                try {
                    return DataResult.success(MathUtils.ExpressionParser.parse(s).evaluate(MathUtils.ExpressionContext.empty()));
                } catch (Exception e) {
                    return DataResult.error("Failed to parse expression: " + e.getMessage());
                }
            },
            d -> DataResult.success(String.valueOf(d))
        )
    ).describe("ExpressionDouble");

    public static final Codec<Integer> EXPR_INT = Codec.fallback(
        Codecs.INT,
        Codecs.STRING.flatXmap(
            s -> {
                try {
                    return DataResult.success((int) Math.round(MathUtils.ExpressionParser.parse(s).evaluate(MathUtils.ExpressionContext.empty())));
                } catch (Exception e) {
                    return DataResult.error("Failed to parse expression: " + e.getMessage());
                }
            },
            i -> DataResult.success(String.valueOf(i))
        )
    ).describe("ExpressionInt");

    public static final Codec<Long> EXPR_LONG = Codec.fallback(
        Codecs.LONG,
        Codecs.STRING.flatXmap(
            s -> {
                try {
                    return DataResult.success(Math.round(MathUtils.ExpressionParser.parse(s).evaluate(MathUtils.ExpressionContext.empty())));
                } catch (Exception e) {
                    return DataResult.error("Failed to parse expression: " + e.getMessage());
                }
            },
            l -> DataResult.success(String.valueOf(l))
        )
    ).describe("ExpressionLong");

    public static final Codec<Color> COLOR = Codec.fallback(
        Codecs.INT.list().flatXmap(
            list -> {
                if (list.size() == 3) return DataResult.success(Color.fromRGB(list.get(0), list.get(1), list.get(2)));
                if (list.size() == 4) return DataResult.success(Color.fromARGB(list.get(3), list.get(0), list.get(1), list.get(2)));
                return DataResult.error("Color list must have 3 or 4 integers: [r, g, b] or [r, g, b, a].");
            },
            c -> DataResult.success(List.of(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()))
        ),
        Codecs.INT.xmap(Color::fromARGB, Color::asARGB)
    ).describe("Color");

    public static final Codec<GlyphDef> GLYPH_DEF = RecordBuilder.create(instance -> instance.group(
        Codecs.STRING.optionalFieldOf("type", "bitmap").forGetter(GlyphDef.class, GlyphDef::type),
        Codecs.STRING.fieldOf("file").forGetter(GlyphDef.class, GlyphDef::file),
        EXPR_INT.optionalFieldOf("height", 8).forGetter(GlyphDef.class, GlyphDef::height),
        EXPR_INT.optionalFieldOf("ascent", 8).forGetter(GlyphDef.class, GlyphDef::ascent),
        Codecs.BOOLEAN.optionalFieldOf("allow_in_chat", true).forGetter(GlyphDef.class, GlyphDef::allowInChat),
        Codecs.STRING.optionalFieldOf("chat_use_permission", "").forGetter(GlyphDef.class, GlyphDef::chatPermission),
        Codecs.BOOLEAN.optionalFieldOf("show_in_dictionary", true).forGetter(GlyphDef.class, GlyphDef::showInDictionary)
    ).apply(instance, GlyphDef::new)).describe("GlyphDef");

    public static final Codec<SoundVariantDef> SOUND_VARIANT_DEF = RecordBuilder.create(instance -> instance.group(
        Codecs.STRING.fieldOf("name").forGetter(SoundVariantDef.class, SoundVariantDef::name),
        Codecs.FLOAT.optionalFieldOf("volume", 1.0f).forGetter(SoundVariantDef.class, SoundVariantDef::volume),
        Codecs.FLOAT.optionalFieldOf("pitch", 1.0f).forGetter(SoundVariantDef.class, SoundVariantDef::pitch),
        EXPR_INT.optionalFieldOf("weight", 1).forGetter(SoundVariantDef.class, SoundVariantDef::weight),
        Codecs.BOOLEAN.optionalFieldOf("stream", false).forGetter(SoundVariantDef.class, SoundVariantDef::stream),
        EXPR_INT.optionalFieldOf("attenuation_distance", 16).forGetter(SoundVariantDef.class, SoundVariantDef::attenuationDistance),
        Codecs.BOOLEAN.optionalFieldOf("preload", false).forGetter(SoundVariantDef.class, SoundVariantDef::preload),
        Codecs.STRING.optionalFieldOf("type", "file").forGetter(SoundVariantDef.class, SoundVariantDef::type)
    ).apply(instance, SoundVariantDef::new)).describe("SoundVariantDef");

    public static final Codec<SoundDef> SOUND_DEF = RecordBuilder.create(instance -> instance.group(
        Codecs.STRING.optionalFieldOf("category", "master").forGetter(SoundDef.class, SoundDef::category),
        Codecs.STRING.optionalFieldOf("subtitle", "").forGetter(SoundDef.class, SoundDef::subtitle),
        Codecs.BOOLEAN.optionalFieldOf("replace", false).forGetter(SoundDef.class, SoundDef::replace),
        SOUND_VARIANT_DEF.list().optionalFieldOf("sounds", List.of()).forGetter(SoundDef.class, SoundDef::sounds)
    ).apply(instance, SoundDef::new)).describe("SoundDef");
}