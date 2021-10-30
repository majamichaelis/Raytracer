package raytracer;

import utils.algebra.Vec3;

/**
 * Class PerspCam
 * The class PerspCam is responsible for the perspective Camera. It inherits everything from the class camera and thus
 * all its properties.
 */
public class PerspCam extends Camera{

    /**
     * Constructor of the perspective Camera
     * It creates an object of the class PerspCam and sets its instance variables
     * @param cameraPosition Camera position
     * @param centerOfInterest Center of interest (focus of the camera)
     * @param userUpVector UserUp vector specifies the tendency of the camera
     * @param viewAngle Opening angle which shows how much the camera sees
     * @param focalLength Factor of the distance between camera and viewplane
     */
    public PerspCam(Vec3 cameraPosition, Vec3 centerOfInterest, Vec3 userUpVector, float viewAngle, float focalLength) {
        super(cameraPosition, centerOfInterest, userUpVector, viewAngle, focalLength);
    }
}
