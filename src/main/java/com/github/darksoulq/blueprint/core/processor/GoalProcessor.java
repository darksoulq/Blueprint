package com.github.darksoulq.blueprint.core.processor;

import com.github.darksoulq.blueprint.Blueprint;
import com.github.darksoulq.blueprint.core.BlueprintRegistries;
import com.github.darksoulq.blueprint.core.ResourcesHolder;
import com.github.darksoulq.blueprint.core.scripting.ScriptManager;
import com.destroystokyo.paper.entity.ai.Goal;
import org.bukkit.entity.Mob;

import java.io.File;
import java.util.function.Function;

public class GoalProcessor implements ContentProcessor {

    private final ScriptManager scriptManager;

    public GoalProcessor(ScriptManager scriptManager) {
        this.scriptManager = scriptManager;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void process(String namespace, File parentDir, File dataFile, ResourcesHolder holder) {
        if (!dataFile.getName().endsWith(".kts")) return;

        String name = dataFile.getName().replace(".kts", "");
        Blueprint.getPlugin().getLogger().info("Parsing goal script: " + dataFile.getName());

        Object result = scriptManager.eval(dataFile);

        if (result == null) {
            Blueprint.getPlugin().getLogger().severe("Failed to load goal script " + dataFile.getName() + " (eval returned null). Check console for script errors.");
            return;
        }

        if (result instanceof Function<?, ?>) {
            BlueprintRegistries.GOALS.register(namespace + ":" + name, (Function<Mob, Goal<Mob>>) result);
            BlueprintRegistries.GOAL_COUNT++;
            Blueprint.getPlugin().getLogger().info("Successfully loaded goal: " + namespace + ":" + name);
        } else {
            Blueprint.getPlugin().getLogger().severe("Script " + dataFile.getName() + " did not return a Function! It returned: " + result.getClass().getName());
        }
    }
}