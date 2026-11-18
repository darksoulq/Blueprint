package com.github.darksoulq.blueprint.core.particle;

import com.github.darksoulq.abyssallib.world.particle.Generator;
import com.github.darksoulq.abyssallib.world.particle.ParticleRenderer;
import com.github.darksoulq.abyssallib.world.particle.Particles;
import com.github.darksoulq.abyssallib.world.particle.Transformer;
import org.bukkit.Color;
import org.bukkit.Location;

import java.util.List;

public record ParticleTemplate(
        Generator generator,
        ParticleRenderer renderer,
        List<Transformer> transformers,
        Color color,
        long interval,
        long duration,
        boolean smooth
) {
    public Particles build(Location origin) {
        Particles.Builder builder = Particles.builder()
                .origin(origin)
                .shape(generator)
                .render(renderer)
                .interval(interval)
                .duration(duration)
                .smooth(smooth);

        if (transformers != null) {
            transformers.forEach(builder::transform);
        }
        
        if (color != null) {
            builder.color(color);
        }

        return builder.build();
    }
}