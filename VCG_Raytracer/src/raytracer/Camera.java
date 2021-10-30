package raytracer;

import scene.SceneObject;
import utils.algebra.Vec3;

/**
 * Class Camera
 * The class Camera is responsible for creating the camera coordinate system.
 */
public abstract class Camera extends SceneObject {
    private Vec3 cameraPosition;
    private Vec3 centerOfInterest;
    private Vec3 userUpVector;
    private Vec3 viewVector;
    private Vec3 cameraUpVector;
    private Vec3 sideVector;
    private float viewAngle;
    private float focalLength;
    private Vec3 focalPoint;

    /**
     * Constructor of the class Camera
     * It creates an object of the class Camera, sets its instance variables and calculates the view-, side- and cameraUp-
     * vector.
     * @param _cameraPosition Camera position
     * @param _centerOfInterest Center of interest (focus of the camera)
     * @param _userUpVector UserUp vector specifies the tendency of the camera
     * @param _viewAngle Opening angle which shows how much the camera sees
     * @param _focalLength Factor of the distance between camera and viewplane
     */
    public Camera(Vec3 _cameraPosition, Vec3 _centerOfInterest, Vec3 _userUpVector, float _viewAngle, float _focalLength) {
        this.cameraPosition = _cameraPosition;
        this.centerOfInterest = _centerOfInterest;
        this.userUpVector = _userUpVector.normalize();
        this.viewAngle = _viewAngle;
        this.focalLength = _focalLength;

        this.viewVector = this.centerOfInterest.sub(this.cameraPosition).normalize();
 
        this.sideVector = viewVector.cross(this.userUpVector).normalize();
    
        this.cameraUpVector = sideVector.cross(viewVector).normalize();

        // Calculates the location of the focal point.
        this.focalPoint = getCameraPosition().add(getViewVector().multScalar(getFocalLength()));
    }

    /**
     * Calculates the point on the viewplane the ray has to travel through.
     * @param x The x Coordinate of the Viewplane
     * @param y The y Coordinate of the Viewplane
     * @return The point on the viewplane the ray has to travel through.
     */
    public Vec3 calculateDestinationPoint(float x, float y) {
        // Translates the Focal Point in the x direction
        Vec3 destinationPoint = focalPoint.add(getSideVector().multScalar(x ));
        // Translates the Focal Point in the y direction
        // Must be inverted, since the Coordinates of the library start in the top-left
        destinationPoint = destinationPoint.add(getCameraUpVector().multScalar(y));
        return destinationPoint;
    }

    public Vec3 getViewVector(){
        return this.viewVector;
    }

    public Vec3 getSideVector(){
        return this.sideVector;
    }

    public Vec3 getCameraPosition(){
        return this.cameraPosition;
    }

    public Vec3 getCameraUpVector(){
        return this.cameraUpVector;
    }

    public float getViewAngle(){
        return this.viewAngle;
    }

    public float getFocalLength(){
        return this.focalLength;
    }
}
