package com.github.darksoulq.blueprint.core.scripting.api;

import com.destroystokyo.paper.entity.ai.GoalType;
import com.github.darksoulq.abyssallib.world.entity.goals.CustomGoal;
import org.bukkit.entity.Mob;

import java.util.EnumSet;

public class ScriptedGoalDelegate extends CustomGoal<Mob> {

    private final ScriptedGoalLogic logic;

    public ScriptedGoalDelegate(Class<Mob> mobClass, String keyName, EnumSet<GoalType> types, ScriptedGoalLogic logic) {
        super(mobClass, keyName, types);
        this.logic = logic;
    }

    @Override
    public boolean shouldActivate() {
        return logic.shouldActivate();
    }

    @Override
    public boolean shouldStayActive() {
        return logic.shouldStayActive();
    }

    @Override
    public void start() {
        logic.start();
    }

    @Override
    public void stop() {
        logic.stop();
    }

    @Override
    public void tick() {
        logic.tick();
    }

    @Override
    public boolean isInterruptable() {
        return logic.isInterruptable();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return logic.requiresUpdateEveryTick();
    }
}