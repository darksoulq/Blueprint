package com.github.darksoulq.blueprint.core.item;

import com.github.darksoulq.abyssallib.server.event.ActionResult;
import com.github.darksoulq.abyssallib.server.event.ClickType;
import com.github.darksoulq.abyssallib.server.event.InventoryClickType;
import com.github.darksoulq.abyssallib.server.event.context.item.AnvilContext;
import com.github.darksoulq.abyssallib.server.event.context.item.UseContext;
import com.github.darksoulq.abyssallib.world.item.Item;
import com.github.darksoulq.abyssallib.world.item.component.DataComponent;
import com.github.darksoulq.blueprint.core.codec.ItemDef;
import com.github.darksoulq.blueprint.core.scripting.api.ItemBehavior;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.Nullable;

public class ScriptedCustomItem extends Item {

    private final ItemBehavior behavior;

    public ScriptedCustomItem(Key id, ItemDef def, ItemBehavior behavior) {
        super(id, resolveMaterial(def.material()));
        this.behavior = behavior;
        this.setHidden(def.hidden());

        if (def.components() != null && !def.components().isEmpty()) {
            for (DataComponent<?> component : def.components()) {
                this.setData(component);
            }
        }
        
        if (def.tooltip() != null) {
            this.tooltip.setVisible(!def.tooltip().hide());
            if (def.tooltip().style() != null) {
                this.tooltip.withStyle(def.tooltip().style());
            }
            if (def.tooltip().lines() != null) {
                for (Component line : def.tooltip().lines()) {
                    this.tooltip.addLine(line);
                }
            }
            this.updateTooltip();
        }
    }

    private static Material resolveMaterial(String name) {
        Material mat = Material.matchMaterial(name);
        return mat != null ? mat : Material.STICK;
    }

    @Override
    public ActionResult onMine(LivingEntity source, Block target) {
        if (behavior != null) {
            ActionResult res = behavior.onMine(source, target);
            if (res != ActionResult.PASS) return res;
        }
        return super.onMine(source, target);
    }

    @Override
    public ActionResult onHit(LivingEntity source, Entity target) {
        if (behavior != null) {
            ActionResult res = behavior.onHit(source, target);
            if (res != ActionResult.PASS) return res;
        }
        return super.onHit(source, target);
    }

    @Override
    public ActionResult onUseOn(UseContext ctx) {
        if (behavior != null) {
            ActionResult res = behavior.onUseOn(ctx);
            if (res != ActionResult.PASS) return res;
        }
        return super.onUseOn(ctx);
    }

    @Override
    public ActionResult onUse(LivingEntity source, EquipmentSlot hand, ClickType type) {
        if (behavior != null) {
            ActionResult res = behavior.onUse(source, hand, type);
            if (res != ActionResult.PASS) return res;
        }
        return super.onUse(source, hand, type);
    }

    @Override
    public void onInventoryTick(Player player) {
        if (behavior != null) behavior.onInventoryTick(player);
        super.onInventoryTick(player);
    }

    @Override
    public void onSlotChange(Player player, @Nullable Integer newSlot) {
        if (behavior != null) behavior.onSlotChange(player, newSlot);
        super.onSlotChange(player, newSlot);
    }

    @Override
    public ActionResult onClick(Player player, int slot, PlayerInventory inventory, InventoryClickType type) {
        if (behavior != null) {
            ActionResult res = behavior.onClick(player, slot, inventory, type);
            if (res != ActionResult.PASS) return res;
        }
        return super.onClick(player, slot, inventory, type);
    }

    @Override
    public ActionResult onDrop(Player player) {
        if (behavior != null) {
            ActionResult res = behavior.onDrop(player);
            if (res != ActionResult.PASS) return res;
        }
        return super.onDrop(player);
    }

    @Override
    public ActionResult onPickup(Player player) {
        if (behavior != null) {
            ActionResult res = behavior.onPickup(player);
            if (res != ActionResult.PASS) return res;
        }
        return super.onPickup(player);
    }

    @Override
    public ActionResult onSwapHand(Player player, EquipmentSlot current) {
        if (behavior != null) {
            ActionResult res = behavior.onSwapHand(player, current);
            if (res != ActionResult.PASS) return res;
        }
        return super.onSwapHand(player, current);
    }

    @Override
    public ActionResult onAnvil(AnvilContext ctx) {
        if (behavior != null) {
            ActionResult res = behavior.onAnvil(ctx);
            if (res != ActionResult.PASS) return res;
        }
        return super.onAnvil(ctx);
    }

    @Override
    public void onCraft(Player player) {
        if (behavior != null) behavior.onCraft(player);
        super.onCraft(player);
    }
}