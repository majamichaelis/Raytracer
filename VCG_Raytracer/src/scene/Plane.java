package scene;

import utils.RgbColor;
import utils.algebra.Matrix4x4;
import utils.algebra.Vec3;

import java.util.List;

/**
 * Class Plane
 * This class provides the properties for a Plane Object.
 */
public class Plane extends Shape{

    // Creates an infinite plane
    public Plane (Vec3 _location, Vec3 _normal, Material _material){
        this.location = _location;
        this.normal = _normal.normalize();
        this.material = _material;
    }

    @Override
    /**
     * Checks if a given Ray intersects with the plane.
     * @param startPosition The Start Position of the Ray
     * @param direction The direction of the Ray.
     * @return The Distance from the start Position of the Ray to the Intersection Point.
     */
    public float intersect(Vec3 _startPosition, Vec3 direction) {
        // Translation of the plane
        Matrix4x4 transformationMatrix = new Matrix4x4();
        transformationMatrix.translateXYZ(this.location);
        Vec3 startPosition = transformationMatrix.invert().multVec3(_startPosition, true);

        float intersectionScalar = normal.scalar(direction.normalize());
        float distance = 0f;
        // if scalar between Pn and D is not 0(90°) calculate the point of intersection, else there is no intersection
        Vec3 size = null;
        if(intersectionScalar < 0){
            // Calculate parameter t
            float t = (normal.scalar(startPosition) / intersectionScalar) * -1;
            // Calculate point of intersection
            this.pointOfIntersection = calculatePointOfIntersection(startPosition, direction, t);
            distance = startPosition.sub(pointOfIntersection).length();
            // Backtranslation of the plane
            this.pointOfIntersection = transformationMatrix.multVec3(this.pointOfIntersection, true);
        }
        return distance;
    }

    @Override
    public RgbColor getColor(List<Light> lightList, Vec3 viewVector) {
        return this.material.getColor(this.pointOfIntersection, this.normal, lightList, viewVector);
    }
}
