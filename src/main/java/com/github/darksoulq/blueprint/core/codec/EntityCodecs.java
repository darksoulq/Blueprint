package com.github.darksoulq.blueprint.core.codec;

import com.github.darksoulq.abyssallib.common.serialization.Codec;
import com.github.darksoulq.abyssallib.common.serialization.Codecs;
import com.github.darksoulq.abyssallib.common.serialization.RecordBuilder;
import com.github.darksoulq.blueprint.core.entity.SpawnSettingsDef;

import java.util.HashMap;

public class EntityCodecs {

    public static final Codec<SpawnSettingsDef> SPAWN_SETTINGS = RecordBuilder.create(instance -> instance.group(
        BlueprintCodecs.EXPR_INT.optionalFieldOf("weight", 1).forGetter(SpawnSettingsDef.class, SpawnSettingsDef::weight),
        BlueprintCodecs.EXPR_INT.optionalFieldOf("min_pack", 1).forGetter(SpawnSettingsDef.class, SpawnSettingsDef::minPack),
        BlueprintCodecs.EXPR_INT.optionalFieldOf("max_pack", 1).forGetter(SpawnSettingsDef.class, SpawnSettingsDef::maxPack),
        BlueprintCodecs.EXPR_INT.optionalFieldOf("min_y", -64).forGetter(SpawnSettingsDef.class, SpawnSettingsDef::minY),
        BlueprintCodecs.EXPR_INT.optionalFieldOf("max_y", 320).forGetter(SpawnSettingsDef.class, SpawnSettingsDef::maxY),
        BlueprintCodecs.EXPR_INT.optionalFieldOf("min_light", 0).forGetter(SpawnSettingsDef.class, SpawnSettingsDef::minLight),
        BlueprintCodecs.EXPR_INT.optionalFieldOf("max_light", 15).forGetter(SpawnSettingsDef.class, SpawnSettingsDef::maxLight),
        Codecs.BOOLEAN.optionalFieldOf("require_sky_darkness", false).forGetter(SpawnSettingsDef.class, SpawnSettingsDef::requireSkyDarkness),
        Codecs.BOOLEAN.optionalFieldOf("require_sky_access", false).forGetter(SpawnSettingsDef.class, SpawnSettingsDef::requireSkyAccess),
        Codecs.STRING.optionalFieldOf("placement", "ON_GROUND").forGetter(SpawnSettingsDef.class, SpawnSettingsDef::placement),
        Codecs.STRING.optionalFieldOf("height_map", "MOTION_BLOCKING_NO_LEAVES").forGetter(SpawnSettingsDef.class, SpawnSettingsDef::heightMap)
    ).apply(instance, SpawnSettingsDef::new)).describe("SpawnSettingsDef");

    public static final Codec<EntityDef> ENTITY_DEF = RecordBuilder.create(instance -> instance.group(
        Codecs.STRING.optionalFieldOf("model_id", "").forGetter(EntityDef.class, EntityDef::modelId),
        Codecs.STRING.optionalFieldOf("base_type", "ZOMBIE").forGetter(EntityDef.class, EntityDef::baseType),
        Codecs.STRING.optionalFieldOf("category", "MONSTER").forGetter(EntityDef.class, EntityDef::category),
        SPAWN_SETTINGS.optionalFieldOf("spawn_settings", null).forGetter(EntityDef.class, EntityDef::spawnSettings),
        Codec.map(Codecs.STRING, BlueprintCodecs.EXPR_DOUBLE).optionalFieldOf("attributes", new HashMap<>()).forGetter(EntityDef.class, EntityDef::attributes),
        Codec.map(Codecs.STRING, BlueprintCodecs.EXPR_INT).optionalFieldOf("goals", new HashMap<>()).forGetter(EntityDef.class, EntityDef::goals)
    ).apply(instance, EntityDef::new)).describe("EntityDef");
}