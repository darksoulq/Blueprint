package com.github.darksoulq.blueprint.core.codec;

public record SoundVariantDef(String name, float volume, float pitch, int weight, boolean stream, int attenuationDistance, boolean preload, String type) {
}