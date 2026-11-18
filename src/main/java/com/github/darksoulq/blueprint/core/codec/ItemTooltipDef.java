package com.github.darksoulq.blueprint.core.codec;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;

import java.util.List;

public record ItemTooltipDef(
    boolean hide,
    List<Component> lines,
    Key style
) {}