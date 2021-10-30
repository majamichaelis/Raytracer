package scene;

import utils.RgbColor;
import utils.algebra.Vec3;

import java.util.List;

/**
 * Represents any Shape that can be intersected with.
 */
public abstract class Shape extends SceneObject{
    protected Vec3 location;
    protected Material material;
    protected Vec3 pointOfIntersection;
    protected Vec3 normal;

    /**
     * Checks if a given Ray intersects with the shape.
     * @param startPosition The Start Position of the Ray
     * @param direction The direction of the Ray.
     * @return The Distance from the start Position of the Ray to the Intersection Point.
     */
    public abstract float intersect(Vec3 startPosition,Vec3 direction);

    /**
     * Calculates the Color of a Shape at a Point using a Lighting Model
     * @param lightList The Lights which hit the Point on the Shape
     * @param viewVector The Vector between the camera and the point of intersection
     * @return The Color of a Shape at a Point
     */
    public abstract RgbColor getColor(List<Light> lightList, Vec3 viewVector);

    /**
     * Returns the point of intersection
     * @return point of intersection
     */
    public Vec3 getPointOfIntersection() {
        return pointOfIntersection;
    }

    /**
     * Returns the point of intersection
     * @return point of intersection
     */
    public Vec3 calculatePointOfIntersection(Vec3 startPosition, Vec3 direction, float parameter) {
        return startPosition.add(direction.multScalar(parameter));
    }

    public Vec3 getNormal() { return normal;}

    public boolean isReflective() {
        return material.isReflective();
    }

    public Material getMaterial() {
        return material;
    }

    public boolean isTransmittive() {
        return material.isTransmittive();
    }

    public float getRefractionIndex(){ return material.getRefractionIndex();}
}
