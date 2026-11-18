package com.github.darksoulq.blueprint.core.scripting.api;

import com.github.darksoulq.abyssallib.server.event.ActionResult;
import com.github.darksoulq.abyssallib.server.event.custom.entity.CustomEntitySpawnEvent;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.LivingEntity;

public interface EntityBehavior {
    default void onSpawn(LivingEntity entity, CustomEntitySpawnEvent.SpawnReason reason) {}
    default ActionResult onDeath(LivingEntity entity, DamageSource damageSource) { return ActionResult.PASS; }
    default void onUnload(LivingEntity entity) {}
    default void onLoad(LivingEntity entity) {}
}