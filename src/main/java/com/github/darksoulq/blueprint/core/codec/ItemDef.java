package com.github.darksoulq.blueprint.core.codec;

import com.github.darksoulq.abyssallib.world.item.component.DataComponent;

import java.util.List;

public record ItemDef(
    String material,
    boolean hidden,
    ItemTooltipDef tooltip,
    List<DataComponent<?>> components
) {}