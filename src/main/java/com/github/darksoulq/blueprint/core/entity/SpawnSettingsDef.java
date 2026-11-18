package com.github.darksoulq.blueprint.core.entity;

public record SpawnSettingsDef(
        int weight,
        int minPack,
        int maxPack,
        int minY,
        int maxY,
        int minLight,
        int maxLight,
        boolean requireSkyDarkness,
        boolean requireSkyAccess,
        String placement,
        String heightMap
) {}