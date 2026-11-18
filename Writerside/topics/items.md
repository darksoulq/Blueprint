# Items
<link-summary>Creating custom items with YAML and Kotlin</link-summary>

Define an item's visuals and components using YAML, and attach an optional Kotlin script (`.kts`) to handle interactions and combat logic.

### File Structure
Item files are placed in your namespace's data folder.

`plugins/Blueprint/content/<namespace>/data/items/<item_id>.yml`
`plugins/Blueprint/content/<namespace>/data/items/<item_id>.kts` (Optional)

### YAML Configuration
Below is a base configuration. Vanilla DataComponents and Custom Abyssallib Components are supported (check relevant docs in AbyssalLib for format).

```yaml
material: "DIAMOND_SWORD"
hidden: false
tooltip:
  hide: false
  lines:
    - "<red>Blade of the Abyss</red>"
    - "<gray>A scripted weapon</gray>"
components:
  "minecraft:max_stack_size": 1
  "minecraft:enchantment_glint_override": true
```

<note>
By default an Items name is set to "item.namespace.id". to add a translation for this key, use the <code>item.&lt;namespace&gt;.&lt;item_id&gt;</code> key in a <a href="language.md">Language</a> file.
</note>

### Configuration Fields

<table>
<tr>
<th>Field</th>
<th>Type</th>
<th>Description</th>
</tr>
<tr>
<td><code>material</code></td>
<td>String</td>
<td>The base vanilla material of the item. Defaults to <code>STICK</code>.</td>
</tr>
<tr>
<td><code>hidden</code></td>
<td>Boolean</td>
<td>If true, the item is hidden from command autocompletion and GUI menus. Defaults to <code>false</code>.</td>
</tr>
<tr>
<td><code>tooltip</code></td>
<td>Object</td>
<td>Settings for display name, lore lines, and resource pack tooltip styles.</td>
</tr>
<tr>
<td><code>components</code></td>
<td>Map</td>
<td>DataComponent IDs (e.g., <code>minecraft:max_stack_size</code>) mapped to their values.</td>
</tr>
</table>

### Kotlin Behaviors
To add logic, create a `.kts` file with the exact same name as the YAML file. It must return an object implementing `ItemBehavior`.

Scripts compile on startup and can be reloaded using `/blueprint reload`.

```kotlin
import com.github.darksoulq.blueprint.core.scripting.api.ItemBehavior
import com.github.darksoulq.abyssallib.server.event.ActionResult
import com.github.darksoulq.abyssallib.server.event.context.item.UseContext
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import net.kyori.adventure.text.minimessage.MiniMessage

object : ItemBehavior {
    override fun onUseOn(ctx: UseContext): ActionResult {
        val player = ctx.source() as? Player ?: return ActionResult.PASS
        val block = ctx.targetBlock() ?: return ActionResult.PASS

        player.sendMessage(MiniMessage.miniMessage().deserialize("<green>You struck a ${block.type}!</green>"))
        return ActionResult.SUCCESS
    }

    override fun onHit(source: LivingEntity, target: Entity): ActionResult {
        source.world.strikeLightningEffect(target.location)
        return ActionResult.PASS
    }
}
```