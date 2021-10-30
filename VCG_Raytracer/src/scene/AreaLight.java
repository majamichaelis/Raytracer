package scene;

import utils.RgbColor;
import utils.algebra.Vec3;

import java.util.LinkedList;
import java.util.List;

/**
 * class AreaLight
 * This class is responsible for creating an arealight for the scene.
 */
public class AreaLight extends Light{


    private int samples;
    private Scene scene;

    /**
     * Constructor
     * Creates a light object with a color and position.
     *
     * @param _color    color of light
     * @param _location location of light
     */
    public AreaLight(RgbColor _color, Vec3 _location, int samples, Scene scene) {
        super(_color, _location);
        this.samples = samples;
        this.scene = scene;
        createLights(samples);
    }
    /**
     * creates each light for the areaLight
     * @param samples number of lights
     */
    public void createLights(int samples) {

        RgbColor sampleColor = color.multScalar(1f/samples);

        int i_sample = 1;
        int direction = 1;
        int radius = 1;
        float scale = 0.3f;

        scene.createPointlight(sampleColor, new Vec3(location.x, location.y, location.z));

        while (i_sample < samples)
        {
            for (int i_2 = 0; i_2 < 2; i_2++)
            {
                //
                for (int i_1 = 0; i_1 < radius; i_1++)
                {
                    i_sample++;
                    if (i_sample > samples) break;

                    if (direction % 4 == 1) location.x+=scale;
                    else if (direction % 4 == 2) location.z+=scale;
                    else if (direction % 4 == 3) location.x-=scale;
                    else if (direction % 4 == 0) location.z-=scale;

                    scene.createPointlight(sampleColor, new Vec3(location.x, location.y, location.z));
                    System.out.println(location.x + ", " + location.z);
                }
                direction++;
            }
            radius++;
        }
    }
}
