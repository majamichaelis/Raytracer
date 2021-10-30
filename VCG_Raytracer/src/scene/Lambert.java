package scene;

import utils.RgbColor;
import utils.algebra.Vec3;

import java.util.List;

/**
 * Class Lambert
 * This class is responsible for providing the material for the Lambert lighting model.
 */
public class Lambert extends Material {

    /**
     * Constructor of the Lambert Material
     * Creates an object with ambient light and its color and sets the color of the shape as diffuse reflection coefficient
     */
    public Lambert(RgbColor _diffusionColor, RgbColor _ambientLight) {
        this.ambientLight = _ambientLight;
        // Color of the surface
        this.diffusionColor = _diffusionColor;
    }

    /**
     * returns color of the given point of intersection
     * @param pointOfIntersection Point where the ray hits an object
     * @param normalVector normal vector
     * @param lightList pointlights of the scene
     * @return outcolor
     */
    public RgbColor getColor(Vec3 pointOfIntersection, Vec3 normalVector, List<Light> lightList, Vec3 viewVector) {
        // Initialize the return value
        RgbColor outColor = new RgbColor(0f, 0f, 0f);

        // Initialize the light portions
        RgbColor ambient = this.ambientLight.multRGB(this.diffusionColor);
        RgbColor diffuse = new RgbColor(0f, 0f,0f);

        // Normalize the normal Vector
        normalVector = normalVector.normalize();

        for (Light light: lightList) {
            // Calculate light vector
            Vec3 lightVector = light.location.sub(pointOfIntersection);
            lightVector = lightVector.normalize();

            // Calculate diffuse scalar (Angle between normal and light vector)
            float diffusionScalar = normalVector.scalar(lightVector);

            // Angles bigger than 90 degree should not be taken into account
            if (diffusionScalar < 0) {
                diffusionScalar = 0;
            }

            // Calculates how much Light hits and lights the surface
            RgbColor diffuseAmount = diffusionColor.multScalar(diffusionScalar);

            // Calculate the color of the light hitting the surface and combine it witn the diffuse lighting of other the lights
            diffuse = diffuse.add(diffuseAmount.multRGB(light.getColor()));
        }
        // Combine the direct and ambient Light
        outColor = ambient.add(diffuse);

        // return calculated RGB color value
        return outColor;
    }

}
