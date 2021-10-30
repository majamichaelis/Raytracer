package scene;

import utils.algebra.Vec3;
import utils.RgbColor;

import java.util.List;

/**
 * Class Material
 * Provides properties for the material.
 */
public abstract class Material {

    // The ambient Light of the scene
    protected RgbColor ambientLight;
    // The color for the diffuse light
    protected RgbColor diffusionColor;
    // viewVector for calculating the Phong material
    protected Vec3 viewVector;
    protected boolean isReflective = false;
    protected boolean isTransmittive = false;
    protected float refractionIndex;


    public abstract RgbColor getColor(Vec3 pointOfIntersection, Vec3 normal, List<Light> lightList, Vec3 viewVector);

    public boolean isReflective() {
        return isReflective;
    }

    public boolean isTransmittive(){
        return isTransmittive;
    }

    public float getRefractionIndex() {
        return refractionIndex;
    }
}
