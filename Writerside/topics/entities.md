# Entities
<link-summary>Defining custom entities, AI, and spawning</link-summary>

Create custom entities by assigning attributes, spawn rules, and AI goals to a base vanilla entity type.

### File Structure
Entity configurations are stored in YAML. An optional Kotlin script handles lifecycle logic.

`plugins/Blueprint/content/<namespace>/data/entities/<entity_id>.yml`
`plugins/Blueprint/content/<namespace>/data/entities/<entity_id>.kts` (Optional)

### YAML Configuration

```yaml
model_id: "my_namespace:custom_zombie" #The FreeMinecraftModels model ID
base_type: "ZOMBIE"
category: "MONSTER"
spawn_settings:
  weight: 10
  min_pack: 1
  max_pack: 3
  min_y: -64
  max_y: 320
  min_light: 0
  max_light: 7
  require_sky_darkness: true
  require_sky_access: false
  placement: "ON_GROUND"
  height_map: "MOTION_BLOCKING_NO_LEAVES"
attributes:
  "minecraft:max_health": 40.0
  "minecraft:movement_speed": 0.25
goals:
  "#namespace:goal": 1
  "#namespace:other_goal": 2
```

### Configuration Fields

<table>
<tr>
<th>Field</th>
<th>Type</th>
<th>Description</th>
</tr>
<tr>
<td><code>model_id</code></td>
<td>String</td>
<td>The namespaced ID of the custom model to apply to this entity.</td>
</tr>
<tr>
<td><code>base_type</code></td>
<td>String</td>
<td>The vanilla EntityType this custom entity extends (e.g., <code>ZOMBIE</code>).</td>
</tr>
<tr>
<td><code>category</code></td>
<td>String</td>
<td>The spawn category (e.g., <code>MONSTER</code>, <code>CREATURE</code>, <code>AMBIENT</code>).</td>
</tr>
<tr>
<td><code>spawn_settings</code></td>
<td>Object</td>
<td>Rules dictating where, when, and how often this entity naturally spawns.</td>
</tr>
<tr>
<td><code>attributes</code></td>
<td>Map</td>
<td>A map of vanilla or custom attribute IDs to their default values.</td>
</tr>
<tr>
<td><code>goals</code></td>
<td>Map</td>
<td>A map of AI goal references (prepended with <code>#</code>) to their priority. Lower numbers execute first. See <a href="goals.md">Goals</a> for defining these.</td>
</tr>
</table>

### Spawn Settings Breakdown

- **`weight`** (Int): Spawn probability weight relative to other entities.
- **`min_pack` / `max_pack`** (Int): Minimum and maximum amount of entities that spawn in a single group.
- **`min_y` / `max_y`** (Int): The Y-level height bounds where this entity can spawn.
- **`min_light` / `max_light`** (Int): The light level bounds required for spawning.
- **`require_sky_darkness`** (Boolean): If true, the entity only spawns at night.
- **`require_sky_access`** (Boolean): If true, the entity requires a clear path to the sky above its spawn location.
- **`placement`** (String): Determines where it can spawn (e.g., `ON_GROUND`, `IN_WATER`).
- **`height_map`** (String): Determines the heightmap used for surface spawning (e.g., `MOTION_BLOCKING_NO_LEAVES`).

### Lifecycle Scripting
The optional `.kts` file intercepts lifecycle events. It must return an `EntityBehavior` object.

```kotlin
import com.github.darksoulq.blueprint.core.scripting.api.EntityBehavior
import com.github.darksoulq.abyssallib.server.event.ActionResult
import org.bukkit.damage.DamageSource
import org.bukkit.entity.LivingEntity

object : EntityBehavior {
    override fun onDeath(entity: LivingEntity, damageSource: DamageSource): ActionResult {
        entity.world.createExplosion(entity.location, 2.0f)
        return ActionResult.PASS
    }
}
```