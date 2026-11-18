# Glyphs
<link-summary>Defining chat glyphs and fonts</link-summary>

Glyphs allow players to type placeholders (like `:smile:`) in chat (optional), which are replaced by custom images.

### File Structure
Glyphs require a YAML configuration and a `.png` texture file.

**Configuration:** `plugins/Blueprint/content/<namespace>/data/glyphs/<group>.yml`
**Texture:** `plugins/Blueprint/content/<namespace>/assets/textures/<texture_name>.png`

### YAML Configuration

```yaml
glyphs:
  smile:
    type: "bitmap"
    file: "smile_icon"
    height: 9
    ascent: 8
    allow_in_chat: true
    chat_use_permission: "blueprint.glyph.smile"
    show_in_dictionary: true
```

### Configuration Fields

<table>
<tr>
<th>Field</th>
<th>Type</th>
<th>Description</th>
</tr>
<tr>
<td><code>type</code></td>
<td>String</td>
<td>Font provider type. Defaults to <code>bitmap</code>.</td>
</tr>
<tr>
<td><code>file</code></td>
<td>String</td>
<td>PNG file name in the assets folder (excluding the <code>.png</code> extension).</td>
</tr>
<tr>
<td><code>height</code></td>
<td>Integer</td>
<td>Rendering height of the glyph.</td>
</tr>
<tr>
<td><code>ascent</code></td>
<td>Integer</td>
<td>Vertical shift of the glyph relative to the text baseline.</td>
</tr>
<tr>
<td><code>allow_in_chat</code></td>
<td>Boolean</td>
<td>Allows players to type this glyph's ID to render it in chat.</td>
</tr>
<tr>
<td><code>chat_use_permission</code></td>
<td>String</td>
<td>Permission required to use this glyph. Leave blank for no permission requirement.</td>
</tr>
<tr>
<td><code>show_in_dictionary</code></td>
<td>Boolean</td>
<td>Includes this glyph in the <code>/bp dictionary</code> book.</td>
</tr>
</table>