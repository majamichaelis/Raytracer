package scene;

import utils.RgbColor;
import utils.algebra.Matrix4x4;
import utils.algebra.Vec3;

import java.util.List;

/**
 * Class Sphere
 * Represents the Sphere
 */
public class Sphere extends Shape{

    private float radius;
    //private Vec3 location;

    /**
     * Constructor Sphere
     * Creates a sphere with a given location and radius.
     * @param _location Location of the sphere object
     * @param _radius radius of the sphere
     */
    public Sphere(Vec3 _location, float _radius, Material _material) {
        this.location = _location;
        this.radius = _radius;
        this.material = _material;
    }

    @Override
    /**
     * Checks if a given Ray intersects with the sphere.
     * Calculates b, c and  the discriminant
     * @param startPosition The Start Position of the Ray
     * @param direction The direction of the Ray.
     * @return The Distance from the start Position of the Ray to the Intersection Point.
     */
    public float intersect(Vec3 _startPosition, Vec3 direction) {
        // Translation of the sphere
        Matrix4x4 transformationMatrix = new Matrix4x4();
        transformationMatrix.translateXYZ(this.location);
        Vec3 startPosition = transformationMatrix.invert().multVec3(_startPosition, true);

        // calculate B und C
        // Math.pow() has to be casted to float because it returns double
        // Intersection
        float b = 2 * (startPosition.x * direction.x + startPosition.y * direction.y + startPosition.z * direction.z);
        float c = (float) (Math.pow(startPosition.x, 2) + Math.pow(startPosition.y, 2) + Math.pow(startPosition.z, 2) - Math.pow(radius, 2));
        float d = calculateDiscriminant(b, c);

        // Check hit
        float distance = 0f;
        //if d>0 there is a intersection
        if (d > 0) {
            distance = calculateDistance(b, d, startPosition, direction);
            if (this.pointOfIntersection != null) {
                this.pointOfIntersection = transformationMatrix.multVec3(this.pointOfIntersection, true);
                this.normal = this.pointOfIntersection.sub(this.location).normalize();
            }
        }
        return distance;
    }

    @Override
    public RgbColor getColor(List<Light> lightList, Vec3 viewVector) {
        return this.material.getColor(this.pointOfIntersection, this.normal, lightList, viewVector);
    }

    /**
     * Calculating the discriminant
     * @param b parameter b of the function: A * t^2 + B * t + C = 0
     * @param c parameter c of the function: A * t^2 + B * t + C = 0
     * @return discriminant
     */
    private float calculateDiscriminant(float b, float c) {
        return (float) Math.pow(b, 2) - 4 * c;
    }

    /**
     * Calculates the distance with B and the discriminant and t0 and t1.
     * It uses the smaller t for the calculation.
     * The Point of intersection is also calculated here.
     * @param b parameter b of the function: A * t^2 + B * t + C = 0
     * @param d discriminant
     * @return distance
     */
    private float calculateDistance(float b, float d, Vec3 startPosition, Vec3 direction) {
        float tZero = (float) ( -1 *  b - Math.sqrt(d)) / 2;
        float tOne = (float) ( -1 *  b + Math.sqrt(d)) / 2;
        // If both of the parameters are negative, set the distance to zero so that no hit is detected
        if(tZero >= 0 || tOne >= 0) {
            if (tZero < tOne) {
                tOne = tZero;
            }
        } else {
            if(tZero < 0.01f && tOne > 0.01f || tZero > 0.01f && tOne < 0.01f){
                if (tZero > tOne) {
                    tOne = tZero;
                }
            } else {
                return 0;
            }
        }
        // Inserts the parameter into the Ray Formula to calculate the distance.
        this.pointOfIntersection = calculatePointOfIntersection(startPosition, direction, tOne);
        return startPosition.sub(pointOfIntersection).length();
    }

    @Override
    public Vec3 getPointOfIntersection() {
        return pointOfIntersection;
    }


}