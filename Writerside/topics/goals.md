# Goals
<link-summary>Creating custom AI behaviors using Kotlin</link-summary>

Goals dictate the AI behaviors of your custom entities (e.g., finding a target, wandering, attacking). Blueprint registers goals globally, allowing multiple entity types to reference and share the same goal.

### File Structure
Goals are strictly defined using Kotlin scripts.

`plugins/Blueprint/content/<namespace>/data/goals/<goal_id>.kts`

### Kotlin Scripting
The script uses an injected `goal` function that takes a name, a list of `GoalType` flags (which prevent conflicting goals from running simultaneously), and the logic block. The script must return the result of this `goal` function.

```kotlin
import com.destroystokyo.paper.entity.ai.GoalType
import com.github.darksoulq.blueprint.core.scripting.api.ScriptedGoalLogic
import org.bukkit.entity.Mob

goal("custom_wander", GoalType.RANDOM_WANDER) { mob ->
    object : ScriptedGoalLogic {
        
        override fun shouldActivate(): Boolean {
            // Logic to determine if the goal should start
            return mob.random.nextDouble() < 0.05
        }

        override fun shouldStayActive(): Boolean {
            // Logic to determine if a running goal should continue
            return !mob.navigation.isDone
        }

        override fun start() {
            // Called when the goal activates
            val loc = mob.location.add(
                (mob.random.nextDouble() - 0.5) * 10,
                0.0,
                (mob.random.nextDouble() - 0.5) * 10
            )
            mob.navigation.moveTo(loc, 1.0)
        }

        override fun tick() {
            // Called every tick while the goal is active
        }

        override fun stop() {
            // Called when the goal is interrupted or finishes
            mob.navigation.stopPathfinding()
        }
    }
}
```

### Applying Goals to Entities
Once defined, you reference the goal in your entity's YAML configuration using its ID (prepended with a `#`). If your script is named `goal.kts`, its reference is `#namespace:goal`.

```yaml
goals:
  "#namespace:goal": 1
```

### ScriptedGoalLogic Methods

- **`shouldActivate()`**: (Required) Returns true if the goal should begin executing.
- **`shouldStayActive()`**: Returns true if an active goal should keep running. Defaults to `shouldActivate()`.
- **`start()`**: Executes once when the goal begins.
- **`tick()`**: Executes every tick while the goal remains active.
- **`stop()`**: Executes once when the goal finishes or is cancelled.
- **`isInterruptable()`**: Returns true if another higher-priority goal can cancel this one. Defaults to `true`.
- **`requiresUpdateEveryTick()`**: Returns true if this goal strictly requires ticking every single server tick. Defaults to `false`.