package raytracer;

import utils.algebra.Vec3;

/**
 * A single Ray, which can be sent into a Scene and perform Intersections.
 */
public class Ray {
    private Vec3 startPoint;
    private Vec3 direction;

    /**
     * A single Ray, which can be sent into a Scene and perform Intersections.
     * The direction is calculated using the start and an end position of the ray.
     * @param _startPoint The start position of a ray.
     * @param _endPoint A point laying on the direction of the Path.
     */
    public Ray(Vec3 _startPoint, Vec3 _endPoint) {
        this.startPoint = _startPoint;
        this.direction = _endPoint.sub(_startPoint).normalize();
    }

    /**
     * Returns the direction in which the ray is send.
     * @return The direction in which the ray is send.
     */
    public Vec3 getDirection() {
        return direction;
    }

    public Vec3 getStartPoint() {
        return startPoint;
    }


    /**
     * Checks whether or not the ray has hit something for the given parameter.
     * @param parameter How far along the ray has travelled.
     * @return The color of what the ray has hit
     */
    public Vec3 calculateRayAt(float parameter) {
        return null;
    }
}
