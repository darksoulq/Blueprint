package com.github.darksoulq.blueprint.core.scripting.api;

public interface ScriptedGoalLogic {
    boolean shouldActivate();
    
    default boolean shouldStayActive() {
        return shouldActivate();
    }
    
    default void start() {}
    
    default void stop() {}
    
    default void tick() {}
    
    default boolean isInterruptable() {
        return true;
    }
    
    default boolean requiresUpdateEveryTick() {
        return false;
    }
}