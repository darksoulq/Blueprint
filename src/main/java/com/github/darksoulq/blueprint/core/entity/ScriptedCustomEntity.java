package com.github.darksoulq.blueprint.core.entity;

import com.github.darksoulq.abyssallib.server.event.ActionResult;
import com.github.darksoulq.abyssallib.server.event.custom.entity.CustomEntitySpawnEvent;
import com.github.darksoulq.abyssallib.server.registry.Registries;
import com.github.darksoulq.abyssallib.world.data.attribute.CustomAttribute;
import com.github.darksoulq.abyssallib.world.entity.CustomEntity;
import com.github.darksoulq.abyssallib.world.entity.DefaultAttributes;
import com.github.darksoulq.abyssallib.world.entity.DefaultGoals;
import com.github.darksoulq.abyssallib.world.entity.SpawnCategory;
import com.github.darksoulq.blueprint.Blueprint;
import com.github.darksoulq.blueprint.core.BlueprintRegistries;
import com.github.darksoulq.blueprint.core.codec.EntityDef;
import com.github.darksoulq.blueprint.core.scripting.api.EntityBehavior;
import com.destroystokyo.paper.entity.ai.Goal;
import net.kyori.adventure.key.Key;
import org.bukkit.HeightMap;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;

import java.util.function.Function;

public class ScriptedCustomEntity extends CustomEntity {

    private final EntityDef def;
    private final EntityBehavior behavior;
    private final DefaultAttributes defaultAttributes;
    private final DefaultGoals defaultGoals;

    public ScriptedCustomEntity(Key id, EntityDef def, EntityBehavior behavior) {
        super(id, EntityType.valueOf(def.baseType().toUpperCase()), SpawnCategory.valueOf(def.category().toUpperCase()), buildSpawnSettings(def.spawnSettings()));
        this.def = def;
        this.behavior = behavior;
        this.defaultAttributes = new DefaultAttributes();
        this.defaultGoals = new DefaultGoals();

        if (def.attributes() != null) {
            def.attributes().forEach((k, v) -> {
                if (k.startsWith("minecraft:")) {
                    NamespacedKey nsk = NamespacedKey.fromString(k);
                    if (nsk != null) {
                        Attribute attr = Registry.ATTRIBUTE.get(nsk);
                        if (attr != null) {
                            this.defaultAttributes.set(attr, v);
                        } else {
                            Blueprint.getPlugin().getLogger().warning("Unknown vanilla attribute mapped in entity " + id.asString() + ": " + k);
                        }
                    }
                } else {
                    CustomAttribute customAttr = Registries.ATTRIBUTES.get(k);
                    if (customAttr != null) {
                        this.defaultAttributes.set(customAttr, v);
                    } else {
                        Blueprint.getPlugin().getLogger().warning("Unknown custom attribute mapped in entity " + id.asString() + ": " + k);
                    }
                }
            });
        }

        if (def.goals() != null) {
            def.goals().forEach((ref, priority) -> {
                if (ref.startsWith("#")) {
                    String key = ref.substring(1);
                    Function<Mob, Goal<Mob>> goalFunc = BlueprintRegistries.GOALS.get(key);
                    if (goalFunc != null) {
                        this.defaultGoals.addGoal(priority, goalFunc);
                    } else {
                        Blueprint.getPlugin().getLogger().warning("Unknown goal reference mapped in entity " + id.asString() + ": " + ref);
                    }
                }
            });
        }
    }

    private static SpawnSettings buildSpawnSettings(SpawnSettingsDef def) {
        if (def == null) return null;
        SpawnSettings.Builder builder = SpawnSettings.builder();
        builder.weight(def.weight());
        builder.pack(def.minPack(), def.maxPack());
        builder.heightRange(def.minY(), def.maxY());
        builder.light(def.minLight(), def.maxLight());
        if (def.requireSkyDarkness()) builder.nightOnly();
        if (def.requireSkyAccess()) builder.requireSkyAccess();
        builder.placement(SpawnPlacement.valueOf(def.placement().toUpperCase()));
        builder.heightMap(HeightMap.valueOf(def.heightMap().toUpperCase()));
        return builder.build();
    }

    @Override
    public String getModelId() {
        return def.modelId().isEmpty() ? null : def.modelId();
    }

    @Override
    public DefaultAttributes getAttributes() {
        return defaultAttributes;
    }

    @Override
    public DefaultGoals getGoals() {
        return defaultGoals;
    }

    @Override
    public void onSpawn(LivingEntity entity, CustomEntitySpawnEvent.SpawnReason reason) {
        if (behavior != null) behavior.onSpawn(entity, reason);
    }

    @Override
    public ActionResult onDeath(LivingEntity entity, DamageSource damageSource) {
        if (behavior != null) {
            ActionResult result = behavior.onDeath(entity, damageSource);
            if (result != ActionResult.PASS) return result;
        }
        return super.onDeath(entity, damageSource);
    }

    @Override
    public void onUnload(LivingEntity entity) {
        if (behavior != null) behavior.onUnload(entity);
    }

    @Override
    public void onLoad(LivingEntity entity) {
        if (behavior != null) behavior.onLoad(entity);
    }
}