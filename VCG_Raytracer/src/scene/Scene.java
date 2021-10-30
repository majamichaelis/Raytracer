package scene;

import org.w3c.dom.css.RGBColor;
import raytracer.Camera;
import raytracer.PerspCam;
import utils.RgbColor;
import utils.algebra.Vec3;
import utils.io.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Class Scene
 * This class is responsible for creating a scene and adding objects to it.
 */
public class Scene {
    private List<Shape> shapeList;
    private List<Light> lightList;
    private Camera camera;


    /**
     * Constructor Scene
     * Creates an object of the class Scene with added objects.
     */
    public Scene() {
        Log.print(this, "Init");
        this.shapeList = new ArrayList<>();
        this.lightList = new ArrayList<>();
    }

    /**
     * Returns a list of shapes.
     * @return The shape list
     */
    public List<Shape> getShapeList() {
        return shapeList;
    }

    /**
     * Adds a sphere to the scene.
     * @param x The X Location of the Sphere
     * @param y The Y Location of the Sphere
     * @param z The Z Location of the Sphere
     * @param radius The Radius of the Sphere
     * @param material The Material of the Sphere
     */
    public void createSphere(float x, float y, float z, float radius, Material material) {
        this.shapeList.add(new Sphere(new Vec3(x, y, z), radius, material));
    }

    /**
     * Adds a plane to the scene.
     * @param location location of the plane
     * @param normal normal of the plane
     * @param material material of the plane
     */
    public void createPlane(Vec3 location, Vec3 normal, Material material) {
        this.shapeList.add(new Plane(location, normal, material));
    }

    /**
     * Adds a square to the scene
     * @param location location of the square
     * @param normal normal of the square
     * @param material material of the square
     * @param size size of the square
     */
    public void createSquare(Vec3 location, Vec3 normal, Material material, float size) {
        this.shapeList.add(new Square(location, normal,size, material));
    }

    /**
     * Creates a new perspective Camera.
     * @param cameraPosition Camera position
     * @param centerOfInterest Center of interest (focus of the camera)
     * @param userUpVector UserUp vector specifies the tendency of the camera
     * @param viewAngle Opening angle which shows how much the camera sees
     * @param focalLength Factor of the distance between camera and viewplane
     */
    public void createPerspCamera(Vec3 cameraPosition, Vec3 centerOfInterest, Vec3 userUpVector, float viewAngle, float focalLength) {
        this.camera = new PerspCam(cameraPosition, centerOfInterest, userUpVector, viewAngle, focalLength);
    }

    /**
     * Returns the camera of the Scene
     * @return The camera of the Scene
     */
    public Camera getCamera() {
        return camera;
    }

    /***
     * Creates a pointlight and adds it to the lightlist of the scene
     * @param color the color of the light
     * @param location the location of the light
     */
    public void createPointlight(RgbColor color, Vec3 location){
        this.lightList.add(new PointLight(color, location));
    }

    /**
     * Creates an area light and adds it to the lightlist of the scene.
     * @param color the color of the light
     * @param location the location of the light
     * @param samples number of samples
     */
    public void createArealight(RgbColor color, Vec3 location, int samples){ new AreaLight(color, location, samples, this); }
    /**
     * Returns the current lightlist.
     * @return lightlist
     */
    public List<Light> getLightList() { return lightList;
    }
}