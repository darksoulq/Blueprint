package com.github.darksoulq.blueprint.core.scripting.api;

import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalType;
import com.github.darksoulq.abyssallib.server.scheduler.Scheduler;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.inventory.EntityEquipment;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Function;

public class ScriptUtils {

    private final Scheduler scheduler;

    public ScriptUtils(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public EntityEquipment getEquipment(LivingEntity entity) {
        return entity.getEquipment();
    }

    public Function<Mob, Goal<Mob>> createGoal(String keyName, List<GoalType> types, Function<Mob, ScriptedGoalLogic> logic) {
        EnumSet<GoalType> typeSet = types.isEmpty() ? EnumSet.noneOf(GoalType.class) : EnumSet.copyOf(types);
        return mob -> new ScriptedGoalDelegate(Mob.class, keyName, typeSet, logic.apply(mob));
    }
}