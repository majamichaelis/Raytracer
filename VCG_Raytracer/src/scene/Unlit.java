package scene;

import utils.RgbColor;
import utils.algebra.Vec3;

import java.util.List;

/**
 * This Type of Material follows no Lighting Model and always returns a singular color.
 */
public class Unlit extends Material {

    /**
     * Creates a new, unshaded, single color Material.
     * @param color The color of the Material.
     */
    public Unlit(RgbColor color) {
        this.diffusionColor = color;
    }

    @Override
    public RgbColor getColor(Vec3 pointOfIntersection, Vec3 normal, List<Light> lightList, Vec3 viewVector) {
        return this.diffusionColor;
    }
}
