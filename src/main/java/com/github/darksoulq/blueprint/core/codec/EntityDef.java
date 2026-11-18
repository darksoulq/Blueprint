package com.github.darksoulq.blueprint.core.codec;

import com.github.darksoulq.blueprint.core.entity.SpawnSettingsDef;

import java.util.Map;

public record EntityDef(
    String modelId,
    String baseType,
    String category,
    SpawnSettingsDef spawnSettings,
    Map<String, Double> attributes,
    Map<String, Integer> goals
) {}