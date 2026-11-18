package com.github.darksoulq.blueprint.core.codec;

import com.github.darksoulq.abyssallib.common.serialization.Codec;
import com.github.darksoulq.abyssallib.common.serialization.Codecs;
import com.github.darksoulq.abyssallib.common.serialization.RecordBuilder;
import com.github.darksoulq.abyssallib.world.item.component.DataComponent;
import net.kyori.adventure.text.Component;

import java.util.List;

public class ItemCodecs {

    public static final Codec<ItemTooltipDef> TOOLTIP_DEF = RecordBuilder.create(instance -> instance.group(
        Codecs.BOOLEAN.optionalFieldOf("hide", false).forGetter(ItemTooltipDef.class, ItemTooltipDef::hide),
        Codecs.TEXT_COMPONENT.list().optionalFieldOf("lines", List.of()).forGetter(ItemTooltipDef.class, ItemTooltipDef::lines),
        Codecs.KEY.optionalFieldOf("style", null).forGetter(ItemTooltipDef.class, ItemTooltipDef::style)
    ).apply(instance, ItemTooltipDef::new)).describe("ItemTooltipDef");

    public static final Codec<ItemDef> ITEM_DEF = RecordBuilder.create(instance -> instance.group(
        Codecs.STRING.optionalFieldOf("material", "STICK").forGetter(ItemDef.class, ItemDef::material),
        Codecs.BOOLEAN.optionalFieldOf("hidden", false).forGetter(ItemDef.class, ItemDef::hidden),
        TOOLTIP_DEF.optionalFieldOf("tooltip", null).forGetter(ItemDef.class, ItemDef::tooltip),
        Codecs.DATA_COMPONENT_MAP.optionalFieldOf("components", List.of()).forGetter(ItemDef.class, ItemDef::components)
    ).apply(instance, ItemDef::new)).describe("ItemDef");

}