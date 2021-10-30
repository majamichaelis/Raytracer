package raytracer;

import scene.Shape;
import utils.algebra.Vec3;

/**
 * Class Responsible for checking Intersections between a Ray and a Shape.
 */
public class Intersection {

    private Vec3 pointOfIntersection;
    private Vec3 normal;
    private Ray inRay;
    private Shape shape;

    private double distance;
    private boolean hit;

    /**
     * Calculates if a Ray hits a given Shape.
     * If it hits it also calculates the distance between the Start Point of the Ray and the Intersection point.
     * @param _inRay ray The Ray that for which the Intersection is tested.
     * @param _shape shape The shape for which the Intersection is tested.
     */
    public Intersection(Ray _inRay, Shape _shape) {
        this.inRay = _inRay;
        this.shape = _shape;
        this.distance = this.shape.intersect(_inRay.getStartPoint(), _inRay.getDirection());
        this.pointOfIntersection = this.shape.getPointOfIntersection();
        this.normal = this.shape.getNormal();
        this.hit = false;
        if (distance > 0) {
            this.hit = true;
        }
    }

    /**
     * Returns if a shape is hit
     * @return hit
     */
    public boolean isHit() {
        return hit;
    }

    /**
     * Returns the Distance to the Intersection
     * @return The Distance to the Intersection
     */
    public double getDistance() {
        return distance;
    }

    public Vec3 getPointOfIntersection() {
        return pointOfIntersection;
    }

    public Vec3 getNormal() {
        return normal;
    }

    public Shape getShape() {
        return shape;
    }
}
