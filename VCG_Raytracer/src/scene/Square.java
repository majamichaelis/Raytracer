package scene;

import utils.algebra.Vec3;

/**
 * A plane which has a limited, square size.
 */
public class Square extends Plane {

    private float size;
    //descripes if the intersection point is in the square
    private boolean isInSquare;

    /**
     * Creates a new Plane for a given size.
     * @param _location The Center Point of the Square
     * @param _normal The direction in which the Square is facing
     * @param _size The horizontal and vertical size of the square
     * @param _material The Material of the Square
     */
    public Square(Vec3 _location, Vec3 _normal, float _size, Material _material) {
        super(_location, _normal, _material);
        this.size = _size;
    }

    @Override
    public float intersect(Vec3 _startPosition, Vec3 direction) {
        float distance = super.intersect(_startPosition, direction);
        if (pointOfIntersection != null) {
            if (isInSquare()) {
                return distance;
            }
        }
        return 0f;
    }

    /**
     * Checks if intersection point is in square
     * @return true if intersection point is inside square
     * @return false if intersection point is outside square
     */
    private boolean isInSquare() {
        Vec3 normalizedPOI = pointOfIntersection.sub(location);
        if (normalizedPOI.x > -size / 2 &&
                normalizedPOI.x < size / 2 &&
                normalizedPOI.y > -size / 2 &&
                normalizedPOI.y < size / 2 &&
                normalizedPOI.z > -size / 2 &&
                normalizedPOI.z < size / 2) {
            return true;
        } else {
            return false;
        }
    }

}
