package scene;

import utils.RgbColor;
import utils.algebra.Vec3;

/**
 * Class Light
 * This class provides the properties for every light source. Such as color of light or location.
 */
public abstract class Light {

    // color of light source
    protected RgbColor color;

    // location of light source
    protected Vec3 location;

    /**
     * Constructor
     * Creates a light object with a color and position.
     * @param _color color of light
     * @param _location location of light
     */
    public Light(RgbColor _color, Vec3 _location){
        this.location = _location;
        this.color=_color;
    }

    /**
     * Returns the color of the light source.
     * @return color of light
     */
    public RgbColor getColor() {
        return color;
    }

    /**
     * Returns the location of the light source.
     * @return location of light source
     */
    public Vec3 getLocation() {
        return location;
    }
}
