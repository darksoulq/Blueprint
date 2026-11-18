package com.github.darksoulq.blueprint.core.codec;

import java.util.List;

public record SoundDef(String category, String subtitle, boolean replace, List<SoundVariantDef> sounds) {
}