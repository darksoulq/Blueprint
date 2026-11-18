# [WIP] Particles
<link-summary>[WIP] Creating particle templates and animations</link-summary>

Create custom particle animations by defining shapes (Generators), visuals (Renderers), and movements (Transformers).

### File Structure
Particle templates are written in YAML.

`plugins/Blueprint/content/<namespace>/data/particles/<file>.yml`

### YAML Configuration

```yaml
generators:
  my_circle:
    type: "circle"
    radius: 2.0
    points: 30

renderers:
  my_flame:
    type: "standard"
    particle: "FLAME"
    count: 1

transformers:
  spin:
    type: "rotate"
    axis: "y"
    speed: 0.1

templates:
  fire_ring:
    generator: "#my_circle"
    renderer: "#my_flame"
    transformers: 
      - "#spin"
    interval: 1
    duration: 100
```

### Available Components
- **Generators:** `point`, `circle`, `sphere`, `cube`, `helix`, `pyramid`, `spiral`.
- **Renderers:** `standard`, `dust`.
- **Transformers:** `scale`, `offset`, `translate`, `rotate`, `breathe`.