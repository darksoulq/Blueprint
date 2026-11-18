# Sounds
<link-summary>Registering sound events and audio</link-summary>

Register custom audio files as playable sound events in-game.

### File Structure
Place the YAML configuration in `data`, and the raw `.ogg` files in `assets`.

**Configuration:** `plugins/Blueprint/content/<namespace>/data/sounds/<file>.yml`
**Audio:** `plugins/Blueprint/content/<namespace>/assets/sounds/<sound_name>.ogg`

### YAML Configuration
Keys in the YAML represent the Sound Event ID (e.g., `/playsound <namespace>:<event_key>`).

```yaml
custom.sword.swing:
  category: "player"
  subtitle: "Sword swings"
  replace: false
  sounds:
    - name: "sword_swing_1"
      volume: 1.0
      pitch: 1.0
      weight: 1
    - name: "sword_swing_2"
      volume: 1.0
      pitch: 0.8
```

### Configuration Fields

<table>
<tr>
<th>Field</th>
<th>Type</th>
<th>Description</th>
</tr>
<tr>
<td><code>category</code></td>
<td>String</td>
<td>The mixer category (e.g., <code>master</code>, <code>player</code>, <code>hostile</code>). Defaults to <code>master</code>.</td>
</tr>
<tr>
<td><code>subtitle</code></td>
<td>String</td>
<td>The translation key shown in subtitles when this sound plays.</td>
</tr>
<tr>
<td><code>replace</code></td>
<td>Boolean</td>
<td>If true, replaces existing sounds registered under this event ID instead of appending to them.</td>
</tr>
<tr>
<td><code>sounds</code></td>
<td>List</td>
<td>List of sound variants. The <code>name</code> field must match an <code>.ogg</code> file in your assets folder without the extension.</td>
</tr>
</table>