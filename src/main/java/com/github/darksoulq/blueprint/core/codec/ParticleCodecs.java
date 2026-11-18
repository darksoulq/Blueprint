package com.github.darksoulq.blueprint.core.codec;

import com.github.darksoulq.abyssallib.common.serialization.Codec;
import com.github.darksoulq.abyssallib.common.serialization.Codecs;
import com.github.darksoulq.abyssallib.common.serialization.RecordBuilder;
import com.github.darksoulq.abyssallib.world.particle.Generator;
import com.github.darksoulq.abyssallib.world.particle.ParticleRenderer;
import com.github.darksoulq.abyssallib.world.particle.Transformer;
import com.github.darksoulq.abyssallib.world.particle.impl.Generators;
import com.github.darksoulq.abyssallib.world.particle.impl.Renderers;
import com.github.darksoulq.abyssallib.world.particle.timeline.Animations;
import com.github.darksoulq.blueprint.core.BlueprintRegistries;
import com.github.darksoulq.blueprint.core.particle.ParticleTemplate;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import java.util.List;

public class ParticleCodecs {

    public static final Codec<Generator> BASE_GENERATOR = Codec.dispatch("type", Codecs.STRING, g -> "unknown", type -> {
        return switch (type.toLowerCase()) {
            case "point" -> Codec.unit(Generators.point());
            case "circle" -> RecordBuilder.create(i -> i.group(
                BlueprintCodecs.EXPR_DOUBLE.optionalFieldOf("radius", 1.0).forGetter(Generator.class, g -> 1.0),
                BlueprintCodecs.EXPR_INT.optionalFieldOf("points", 10).forGetter(Generator.class, g -> 10)
            ).apply(i, Generators::circle));
            case "sphere" -> RecordBuilder.create(i -> i.group(
                BlueprintCodecs.EXPR_DOUBLE.optionalFieldOf("radius", 1.0).forGetter(Generator.class, g -> 1.0),
                BlueprintCodecs.EXPR_INT.optionalFieldOf("points", 50).forGetter(Generator.class, g -> 50)
            ).apply(i, Generators::sphere));
            case "cube" -> RecordBuilder.create(i -> i.group(
                BlueprintCodecs.EXPR_DOUBLE.optionalFieldOf("size", 1.0).forGetter(Generator.class, g -> 1.0),
                BlueprintCodecs.EXPR_INT.optionalFieldOf("resolution", 5).forGetter(Generator.class, g -> 5)
            ).apply(i, Generators::cube));
            case "helix" -> RecordBuilder.create(i -> i.group(
                BlueprintCodecs.EXPR_DOUBLE.optionalFieldOf("radius", 1.0).forGetter(Generator.class, g -> 1.0),
                BlueprintCodecs.EXPR_DOUBLE.optionalFieldOf("height", 2.0).forGetter(Generator.class, g -> 2.0),
                BlueprintCodecs.EXPR_INT.optionalFieldOf("turns", 2).forGetter(Generator.class, g -> 2),
                BlueprintCodecs.EXPR_INT.optionalFieldOf("points", 50).forGetter(Generator.class, g -> 50),
                BlueprintCodecs.EXPR_DOUBLE.optionalFieldOf("speed", 0.0).forGetter(Generator.class, g -> 0.0)
            ).apply(i, Generators::helix));
            case "pyramid" -> RecordBuilder.create(i -> i.group(
                BlueprintCodecs.EXPR_DOUBLE.optionalFieldOf("size", 1.0).forGetter(Generator.class, g -> 1.0),
                BlueprintCodecs.EXPR_DOUBLE.optionalFieldOf("height", 1.0).forGetter(Generator.class, g -> 1.0),
                BlueprintCodecs.EXPR_INT.optionalFieldOf("resolution", 5).forGetter(Generator.class, g -> 5)
            ).apply(i, Generators::pyramid));
            case "spiral" -> RecordBuilder.create(i -> i.group(
                BlueprintCodecs.EXPR_DOUBLE.optionalFieldOf("max_radius", 1.0).forGetter(Generator.class, g -> 1.0),
                BlueprintCodecs.EXPR_DOUBLE.optionalFieldOf("height", 2.0).forGetter(Generator.class, g -> 2.0),
                BlueprintCodecs.EXPR_INT.optionalFieldOf("points", 50).forGetter(Generator.class, g -> 50),
                BlueprintCodecs.EXPR_DOUBLE.optionalFieldOf("speed", 0.0).forGetter(Generator.class, g -> 0.0)
            ).apply(i, Generators::spiral));
            default -> Codec.error("Unknown generator type: " + type);
        };
    });

    public static final Codec<Generator> GENERATOR = ReferenceCodec.referenceable(BASE_GENERATOR, BlueprintRegistries.GENERATORS::get);

    public static final Codec<ParticleRenderer> BASE_RENDERER = Codec.dispatch("type", Codecs.STRING, r -> "unknown", type -> {
        return switch (type.toLowerCase()) {
            case "standard" -> RecordBuilder.create(i -> i.group(
                Codec.enumCodec(Particle.class).fieldOf("particle").forGetter(ParticleRenderer.class, r -> Particle.HAPPY_VILLAGER),
                BlueprintCodecs.EXPR_INT.optionalFieldOf("count", 1).forGetter(ParticleRenderer.class, r -> 1),
                BlueprintCodecs.EXPR_DOUBLE.optionalFieldOf("speed", 0.0).forGetter(ParticleRenderer.class, r -> 0.0)
            ).apply(i, (p, c, s) -> new Renderers.Standard(p, c, s, null)));
            case "dust" -> RecordBuilder.create(i -> i.group(
                Codecs.FLOAT.optionalFieldOf("size", 1.0f).forGetter(ParticleRenderer.class, r -> 1.0f)
            ).apply(i, Renderers.DustRenderer::new));
            default -> Codec.error("Unknown renderer type: " + type);
        };
    });

    public static final Codec<ParticleRenderer> RENDERER = ReferenceCodec.referenceable(BASE_RENDERER, BlueprintRegistries.RENDERERS::get);

    public static final Codec<Transformer> BASE_TRANSFORMER = Codec.dispatch("type", Codecs.STRING, t -> "unknown", type -> {
        return switch (type.toLowerCase()) {
            case "scale" -> RecordBuilder.create(i -> i.group(
                BlueprintCodecs.EXPR_DOUBLE.fieldOf("start").forGetter(Transformer.class, t -> 1.0),
                BlueprintCodecs.EXPR_DOUBLE.fieldOf("end").forGetter(Transformer.class, t -> 1.0),
                BlueprintCodecs.EXPR_LONG.fieldOf("duration").forGetter(Transformer.class, t -> 20L)
            ).apply(i, (s, e, d) -> (Vector v, long tick) -> {
                double progress = Math.min(1.0, (double) tick / Math.max(1, d));
                return v.multiply((double) s + ((double) e - (double) s) * progress);
            }));
            case "offset" -> RecordBuilder.create(i -> i.group(
                BlueprintCodecs.EXPR_DOUBLE.optionalFieldOf("x", 0.0).forGetter(Transformer.class, t -> 0.0),
                BlueprintCodecs.EXPR_DOUBLE.optionalFieldOf("y", 0.0).forGetter(Transformer.class, t -> 0.0),
                BlueprintCodecs.EXPR_DOUBLE.optionalFieldOf("z", 0.0).forGetter(Transformer.class, t -> 0.0)
            ).apply(i, (x, y, z) -> (Vector v, long tick) -> v.add(new Vector((double) x, (double) y, (double) z))));
            case "breathe" -> RecordBuilder.create(i -> i.group(
                BlueprintCodecs.EXPR_DOUBLE.fieldOf("min").forGetter(Transformer.class, t -> 1.0),
                BlueprintCodecs.EXPR_DOUBLE.fieldOf("max").forGetter(Transformer.class, t -> 2.0),
                BlueprintCodecs.EXPR_LONG.fieldOf("duration").forGetter(Transformer.class, t -> 40L)
            ).apply(i, Animations::breathe));
            case "rotate" -> RecordBuilder.create(i -> i.group(
                Codecs.STRING.fieldOf("axis").forGetter(Transformer.class, t -> "y"),
                BlueprintCodecs.EXPR_DOUBLE.fieldOf("speed").forGetter(Transformer.class, t -> 0.1)
            ).apply(i, (axis, speed) -> (Vector v, long tick) -> {
                double angle = tick * (double) speed;
                return switch (axis.toLowerCase()) {
                    case "x" -> v.rotateAroundX(angle);
                    case "z" -> v.rotateAroundZ(angle);
                    default -> v.rotateAroundY(angle);
                };
            }));
            case "translate" -> RecordBuilder.create(i -> i.group(
                BlueprintCodecs.EXPR_DOUBLE.optionalFieldOf("x", 0.0).forGetter(Transformer.class, t -> 0.0),
                BlueprintCodecs.EXPR_DOUBLE.optionalFieldOf("y", 0.0).forGetter(Transformer.class, t -> 0.0),
                BlueprintCodecs.EXPR_DOUBLE.optionalFieldOf("z", 0.0).forGetter(Transformer.class, t -> 0.0)
            ).apply(i, (x, y, z) -> (Vector v, long tick) -> v.add(new Vector((double) x * tick, (double) y * tick, (double) z * tick))));
            default -> Codec.error("Unknown transformer type: " + type);
        };
    });

    public static final Codec<Transformer> TRANSFORMER = ReferenceCodec.referenceable(BASE_TRANSFORMER, BlueprintRegistries.TRANSFORMERS::get);

    public static final Codec<ParticleTemplate> TEMPLATE = RecordBuilder.create(i -> i.group(
        GENERATOR.fieldOf("generator").forGetter(ParticleTemplate.class, ParticleTemplate::generator),
        RENDERER.fieldOf("renderer").forGetter(ParticleTemplate.class, ParticleTemplate::renderer),
        TRANSFORMER.list().optionalFieldOf("transformers", List.of()).forGetter(ParticleTemplate.class, ParticleTemplate::transformers),
        BlueprintCodecs.COLOR.optionalFieldOf("color", null).forGetter(ParticleTemplate.class, ParticleTemplate::color),
        BlueprintCodecs.EXPR_LONG.optionalFieldOf("interval", 1L).forGetter(ParticleTemplate.class, ParticleTemplate::interval),
        BlueprintCodecs.EXPR_LONG.optionalFieldOf("duration", -1L).forGetter(ParticleTemplate.class, ParticleTemplate::duration),
        Codecs.BOOLEAN.optionalFieldOf("smooth", false).forGetter(ParticleTemplate.class, ParticleTemplate::smooth)
    ).apply(i, ParticleTemplate::new)).describe("ParticleTemplate");
}