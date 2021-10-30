package scene;

import utils.RgbColor;
import utils.algebra.Vec3;

import java.util.List;

public class Phong extends Material {

    private RgbColor specularColor;
    private float specularReflectivity;
    private float reflectivity;
    private float refractionIndex;

    /**
     * Constructor of the Phong Material with reflectivity
     * Creates an object with ambient light and its color and sets the color of the shape as diffuse reflection coefficient
     */
    public Phong(RgbColor _diffusionColor, RgbColor _specularColor, float _specularReflectivity, RgbColor _ambientLight, float _reflectivity, float _refractionIndex) {
        this.ambientLight = _ambientLight;
        // Color of the surface
        this.diffusionColor = _diffusionColor;
        this.specularColor = _specularColor;
        // The factor n, which sets the Specular Reflectivity
        this.specularReflectivity = _specularReflectivity;
        this.reflectivity = _reflectivity;
        this.isReflective = this.reflectivity != 0;
        this.refractionIndex = _refractionIndex;
        this.isTransmittive = this.refractionIndex > 0;
    }


    @Override
    public RgbColor getColor(Vec3 pointOfIntersection, Vec3 normalVector, List<Light> lightList, Vec3 viewVector) {
        // Initialize the return value
        RgbColor outColor = new RgbColor(0f, 0f, 0f);

        // Initialize the light portions
        RgbColor ambient = this.ambientLight.multRGB(this.diffusionColor);
        RgbColor directLight = new RgbColor(0f, 0f, 0f);

        // Normalize the normal Vector
        normalVector = normalVector.normalize();

        for (Light light : lightList) {
            // Calculate light vector
            Vec3 lightVector = light.location.sub(pointOfIntersection);
            lightVector = lightVector.normalize();

            // Calculate diffuse scalar (Angle between normal and light vector)
            float diffusionScalar = normalVector.scalar(lightVector);

            // Calculate the Reflection Vector
            // R = 2 * (N * L) * N - L
            Vec3 reflectionVector = normalVector.multScalar(diffusionScalar * 2).sub(lightVector);
            reflectionVector = reflectionVector.normalize();

            // Calculate specular scalar (Angle between view and reflection vector)
            float specularScalar = viewVector.scalar(reflectionVector);

            // Angles bigger than 90 degree should not be taken into account
            if (diffusionScalar < 0) {
                diffusionScalar = 0;
            }
            if (specularScalar < 0) {
                specularScalar = 0;
            }

            // Apply reflectivity of specular part to specular scalar
            specularScalar = (float) Math.pow(specularScalar, specularReflectivity);

            // Calculates how much Light hits and lights the surface
            RgbColor diffuseAmount = diffusionColor.multScalar(diffusionScalar);

            // Calculates how much Light is reflected
            RgbColor specularAmount = specularColor.multScalar(specularScalar);

            // Calculate the color of the light hitting the surface and combine it witn the diffuse lighting of other the lights
            directLight = directLight.add(diffuseAmount.add(specularAmount).multRGB(light.getColor()));
        }
        // Combine the direct and ambient Light
        outColor = ambient.add(directLight);

        // return calculated RGB color value
        return outColor;
    }

    public float getRefractionIndex() {
        return this.refractionIndex;
    }

}

