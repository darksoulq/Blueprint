package com.github.darksoulq.blueprint.core;

import com.github.darksoulq.abyssallib.server.registry.DeferredRegistry;
import com.github.darksoulq.abyssallib.server.registry.Registry;
import com.github.darksoulq.abyssallib.world.entity.CustomEntity;
import com.github.darksoulq.abyssallib.world.item.Item;
import com.github.darksoulq.abyssallib.world.particle.Generator;
import com.github.darksoulq.abyssallib.world.particle.ParticleRenderer;
import com.github.darksoulq.abyssallib.world.particle.Transformer;
import com.github.darksoulq.blueprint.core.codec.GlyphDef;
import com.github.darksoulq.blueprint.core.particle.ParticleTemplate;
import com.destroystokyo.paper.entity.ai.Goal;
import org.bukkit.entity.Mob;

import java.util.function.Function;

public class BlueprintRegistries {
    public static final Registry<GlyphDef> GLYPH_DEFS = new Registry<>();

    public static final Registry<Generator> GENERATORS = new Registry<>();
    public static final Registry<ParticleRenderer> RENDERERS = new Registry<>();
    public static final Registry<Transformer> TRANSFORMERS = new Registry<>();
    public static final Registry<ParticleTemplate> PARTICLE_TEMPLATES = new Registry<>();

    public static final Registry<Function<Mob, Goal<Mob>>> GOALS = new Registry<>();
    public static final Registry<DeferredRegistry<CustomEntity>> ENTITY_REGISTRIES = new Registry<>();
    public static final Registry<DeferredRegistry<Item>> ITEM_REGISTRIES = new Registry<>();

    public static int SOUND_COUNT = 0;
    public static int GLYPH_COUNT = 0;
    public static int LANG_COUNT = 0;
    public static int ATLAS_COUNT = 0;
    public static int PARTICLE_COUNT = 0;
    public static int ENTITY_COUNT = 0;
    public static int ITEM_COUNT = 0;
    public static int GOAL_COUNT = 0;
    public static int POST_EFFECT_COUNT = 0;
    public static int CORE_SHADER_COUNT = 0;

    public static void clearAll() {
        GLYPH_DEFS.clear();
        GENERATORS.clear();
        RENDERERS.clear();
        TRANSFORMERS.clear();
        PARTICLE_TEMPLATES.clear();
        GOALS.clear();

        ENTITY_REGISTRIES.getAll().values().forEach(DeferredRegistry::unload);
        ENTITY_REGISTRIES.clear();

        ITEM_REGISTRIES.getAll().values().forEach(DeferredRegistry::unload);
        ITEM_REGISTRIES.clear();

        SOUND_COUNT = 0;
        GLYPH_COUNT = 0;
        LANG_COUNT = 0;
        ATLAS_COUNT = 0;
        PARTICLE_COUNT = 0;
        ENTITY_COUNT = 0;
        ITEM_COUNT = 0;
        GOAL_COUNT = 0;
        POST_EFFECT_COUNT = 0;
        CORE_SHADER_COUNT = 0;
    }
}