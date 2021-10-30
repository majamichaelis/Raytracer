package scene;

import utils.RgbColor;
import utils.algebra.Vec3;

/**
 * class PointLight
 * This class is responsible for creating a pointlight for the scene.
 */
public class PointLight extends Light{

    public PointLight(RgbColor _color, Vec3 _location){

        super(_color, _location);
    }

}