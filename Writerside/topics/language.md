# Language
<link-summary>Managing language files and translations</link-summary>

Define translations and text replacements for the server resource pack.

### File Structure
Place language files in the `lang` folder. The filename must be a valid locale code (e.g., `en_us`).

`plugins/Blueprint/content/<namespace>/data/lang/<locale>.yml`

### YAML Format
You can use flat keys, nested maps, or a combination of both. MiniMessage formatting tags are supported.

```yaml
# Nested format
menu:
  quests:
    title: "Active Quests"
    empty: "<gray>No active quests."

# Flat format
entity.my_namespace.custom_zombie: "Abyssal Walker"
custom_string_id: "<red>Warning!</red>"
```