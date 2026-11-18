package com.github.darksoulq.blueprint.core.scripting.api;

import com.github.darksoulq.abyssallib.server.event.ActionResult;
import com.github.darksoulq.abyssallib.server.event.ClickType;
import com.github.darksoulq.abyssallib.server.event.InventoryClickType;
import com.github.darksoulq.abyssallib.server.event.context.item.AnvilContext;
import com.github.darksoulq.abyssallib.server.event.context.item.UseContext;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.PlayerInventory;

public interface ItemBehavior {
    default ActionResult onMine(LivingEntity source, Block target) { return ActionResult.PASS; }
    default ActionResult onHit(LivingEntity source, Entity target) { return ActionResult.PASS; }
    default ActionResult onUseOn(UseContext ctx) { return ActionResult.PASS; }
    default ActionResult onUse(LivingEntity source, EquipmentSlot hand, ClickType type) { return ActionResult.PASS; }
    default void onInventoryTick(Player player) {}
    default void onSlotChange(Player player, Integer newSlot) {}
    default ActionResult onClick(Player player, int slot, PlayerInventory inventory, InventoryClickType type) { return ActionResult.PASS; }
    default ActionResult onDrop(Player player) { return ActionResult.PASS; }
    default ActionResult onPickup(Player player) { return ActionResult.PASS; }
    default ActionResult onSwapHand(Player player, EquipmentSlot current) { return ActionResult.PASS; }
    default ActionResult onAnvil(AnvilContext ctx) { return ActionResult.PASS; }
    default void onCraft(Player player) {}
}