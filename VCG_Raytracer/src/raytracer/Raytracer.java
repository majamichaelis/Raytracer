/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    1. Send primary ray
    2. intersection test with all shapes
    3. if hit:
    3a: send secondary ray to the light source
    3b: 2
        3b.i: if hit:
            - Shape is in the shade
            - Pixel color = ambient value
        3b.ii: in NO hit:
            - calculate local illumination
    4. if NO hit:
        - set background color

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package raytracer;

import scene.AreaLight;
import scene.Light;
import scene.Scene;
import scene.Shape;
import ui.Window;
import utils.RgbColor;
import utils.algebra.Vec2;
import utils.algebra.Vec3;
import utils.io.Log;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Class Raytracer
 * Represents the Raytracer.
 */
public class Raytracer {

    private BufferedImage mBufferedImage;

    private Scene mScene;
    private Window mRenderWindow;

    private int pixelWidth;
    private int pixelHeight;

    private int mMaxRecursions;
    private int currentRecursion;

    private RgbColor mBackgroundColor;
    private RgbColor mAmbientLight;

    private int mAntiAliasingSamples;

    private boolean mDebug;
    private long tStart;

    private float imageWidth;
    private float imageHeight;

    private Camera camera;


    /**
     * Constructor
     **/
    public Raytracer(Scene scene, Window renderWindow, int recursions, RgbColor backColor, RgbColor ambientLight, int antiAliasingSamples, boolean debugOn, int pixelWidth, int pixelHeight) {
        Log.print(this, "Init");

        this.pixelWidth = pixelWidth;
        this.pixelHeight = pixelHeight;

        mMaxRecursions = recursions;

        mBufferedImage = renderWindow.getBufferedImage();

        mAntiAliasingSamples = antiAliasingSamples;

        mBackgroundColor = backColor;
        mAmbientLight = ambientLight;
        mScene = scene;
        mRenderWindow = renderWindow;
        mDebug = debugOn;
        tStart = System.currentTimeMillis();
    }

    /**
     * Send the created window to the frame delivered by JAVA to display our result
     **/
    public void exportRendering() {
        mRenderWindow.exportRendering(String.valueOf(stopTime(tStart)), mMaxRecursions, mAntiAliasingSamples, mDebug);
    }

    /**
     * Stop time of rendering
     **/
    private static double stopTime(long tStart) {
        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        return tDelta / 1000.0;
    }

    /**
     * This is where our scene is actually ray-traced
     **/
    public void renderScene() {
        Log.print(this, "Prepare rendering at " + String.valueOf(stopTime(tStart)));

        camera = mScene.getCamera();

        //  Calculates Viewplane Dimensions
        float ratio = (float) pixelWidth / (float) pixelHeight;
        this.imageHeight = (float) (2 * Math.tan(Math.toRadians(camera.getViewAngle()) / 2)) * camera.getFocalLength();
        this.imageWidth = ratio * this.imageHeight;

        float[][] gaussArray = getGaussArray();
        float[] gaussList = getGaussList(mAntiAliasingSamples);

        // Primary rays ...
        for (int y = pixelHeight - 1; y >= 0; y--) {
            for (int x = pixelWidth - 1; x >= 0; x--) {

                float xAntiAliasing = x;
                float yAntiAliasing = y;
                RgbColor colorFinal = new RgbColor(0f, 0f, 0f);

                for (int ySample = 0; ySample < mAntiAliasingSamples; ySample++) {
                    yAntiAliasing += gaussList[ySample];
                    for (int xSample = 0; xSample < mAntiAliasingSamples; xSample++) {
                        //new ray directions
                        xAntiAliasing += gaussList[xSample];

                        Intersection closestIntersection = sendRayThroughPixel(xAntiAliasing, yAntiAliasing);

                        RgbColor color = mBackgroundColor;
                        // If a shape has been hit send secondary Rays from Intersection Point to the Lights
                        if (closestIntersection != null) {
                            color = secondaryRays(closestIntersection);
                        }

                        if (mAntiAliasingSamples > 1) {
                            //color = color.multScalar(gaussArray[xSample][ySample]);
                            color = color.divide(mAntiAliasingSamples * mAntiAliasingSamples);
                        }
                        //color = color.divide(mAntiAliasingSamples * mAntiAliasingSamples);
                        colorFinal = colorFinal.add(color);

                    }
                }
                // Set Pixel
                mRenderWindow.setPixel(mBufferedImage, colorFinal, new Vec2(x, y));
            }
        }
        // Yeeaahhh, raytracing is so much fun ...
        this.exportRendering();
    }

    private float[][] getGaussArray() {
        float[] gaussList = getGaussList(mAntiAliasingSamples);
        float[][] gaussArray = new float[mAntiAliasingSamples][mAntiAliasingSamples];
        for (int index = 0; index < mAntiAliasingSamples / 2; index++) {
            float currentGauss = gaussList[index];
            for (int x = index; x < mAntiAliasingSamples - index; x++) {
                gaussArray[x][index] = currentGauss;
            }
            for (int y = index + 1; y < mAntiAliasingSamples - index; y++) {
                gaussArray[mAntiAliasingSamples - 1 - index][y] = currentGauss;
            }
            for (int x = mAntiAliasingSamples - 1 - index; x >= index; x--) {
                gaussArray[x][mAntiAliasingSamples - 1 - index] = currentGauss;
            }
            for (int y = mAntiAliasingSamples - 1 - index; y > index; y--) {
                gaussArray[index][y] = currentGauss;
            }
        }

        return gaussArray;
    }

    /**
     * Gets a list of normal distributed Gauss values
     * @param size The number of values
     * @return A list of normal distributed Gauss values
     */
    private float[] getGaussList(int size) {
        float[] list = new float[size];
        float x = -3;
        float step = 6.0f / (float) (mAntiAliasingSamples - 1);
        for (int i = 0; i < size; i++) {
            list[i] = gauss(x);
            x += step;
        }
        return list;
    }

    /**
     * Calculates a normal distributed Gauss value
     * @param x The parameter of the Gauss function
     * @return A normal distributed Gauss value
     */
    private float gauss(float x) {
        double factor = 1 / Math.sqrt(2 * Math.PI);
        double power = Math.pow(x, 2) * -0.5;
        double gauss = factor * Math.exp(power);
        return (float) gauss;
    }

    /**
     * Calculates where the Ray should be send in World Space
     *
     * @param x The X Coordinate of the pixel
     * @param y The Y Coordinate of the pixel
     * @return An Intersection Object, with the information what the ray hit.
     *         Null when there is no intersection
     */
    public Intersection sendRayThroughPixel(float x, float y) {
        currentRecursion = 0;
        // Position on the Viewplane
        Vec2 normalizedPixel = normalizePixel(x, y);

        // Calculate destination point
        Vec3 destinationPoint = camera.calculateDestinationPoint(normalizedPixel.x, normalizedPixel.y);

        // Send Ray from the Camera Position in the direction of the Pixel
        return primaryRay(camera.getCameraPosition(), destinationPoint);
    }

    /**
     * Sends a primary Ray and checks intersections.
     *
     * @param startPoint The start point of the ray.
     * @param destinationPoint The direction of the ray.
     * @return An Intersection Object, with the information what the ray hit.
     *         Null when there is no intersection
     */
    private Intersection primaryRay(Vec3 startPoint, Vec3 destinationPoint) {
        // Calculate ray with start point and end point
        Ray ray = new Ray(startPoint, destinationPoint);
        // Set the distance to a location very far away if nothing has been hit yet
        double distanceToIntersection = 99999d;
        Intersection intersection = null;
        boolean reflective = false;
        boolean transmittive = false;

        List<Shape> shapeList = mScene.getShapeList();
        // Checks Intersections with each Object
        for (Shape shape : shapeList) {
            boolean hit = false;

            // Calculates the intersection of the ray and shape
            Intersection currentIntersection = new Intersection(ray, shape);
            hit = currentIntersection.isHit();
            if (hit) {
                // Checking with the distance which the closest shape is
                if (currentIntersection.getDistance() < distanceToIntersection) {
                    distanceToIntersection = currentIntersection.getDistance();
                    intersection = currentIntersection;
                    reflective = shape.isReflective();
                    transmittive = shape.isTransmittive();
                }
            }
        }

        // Recursion if shape is Reflective
        if (reflective && currentRecursion < mMaxRecursions) {
            currentRecursion++;
            Vec3 l = ray.getDirection().normalize().multScalar(-1.0f);
            Vec3 n = intersection.getNormal().normalize();
            Vec3 direction = n.multScalar(n.scalar(l) * 2).sub(l);
            // Add direction to avoid self collision
            Vec3 pointOfIntersection = intersection.getPointOfIntersection().add(direction);
            Vec3 pointInDirection = pointOfIntersection.add(direction);
            return primaryRay(pointOfIntersection, pointInDirection);
        } if(transmittive) {
            // iorOfMedium1 is the index of refraction of the medium they ray is in before entering the other medium
            float n1;
            // iorOfMedium2 is the given index of refraction, the ior of the medium the ray is entering from the first medium
            float n2;
            // incident ray
            Vec3 I = ray.getDirection().negate().normalize();
            // normal
            Vec3 refractNormal = intersection.getNormal().normalize();
            // cos alpha is the result of the scalar between normal and incident ray
            float cosAlpha = I.scalar(refractNormal);

            n1 = 1.0f;
            n2 = intersection.getShape().getRefractionIndex();
            // if the scalar between normal and incident ray is less than 0, we are inside the surface
            if(cosAlpha < 0){
                // else we are outside the surface, so cos is positive but the normal has the reverse direction so it needs to be negated
                refractNormal = refractNormal.negate().normalize();
                // swap refraction indices
                n1 = intersection.getShape().getRefractionIndex();
                n2 = 1.0f;
                // if we are outside, we want cos to be positive so we have to negate the scalar
                cosAlpha = cosAlpha * (-1);
            }
            // Snellius (sin alpha/ sin beta = n2/n1) here n1/n2
            float snellius = n1 / n2;
            // Check if there is a total internal reflection
            float sinT = 1 - ((snellius * snellius) * (1- (cosAlpha * cosAlpha)));
            float cosBeta = (float) Math.sqrt(sinT);

            // calculate transmission Ray
            Vec3 firstHalf = (refractNormal.multScalar(cosAlpha).sub(I)).multScalar(snellius);
            Vec3 secondHalf = refractNormal.multScalar(cosBeta);
            Vec3 transmissionRay = firstHalf.sub(secondHalf).normalize();
            Vec3 pointOfIntersection = intersection.getPointOfIntersection().add(refractNormal.multScalar(100f));
            return primaryRay(pointOfIntersection,transmissionRay);
        } else {
            return intersection;
        }
    }

    /**
     * Sends the secondary rays for an intersection
     * @param closestIntersection The intersection for which the secondary rays will be calculated
     * @return The Color calculated after the seconadry rays have been send.
     */
    public RgbColor secondaryRays(Intersection closestIntersection) {
        Vec3 pointOfIntersection = closestIntersection.getPointOfIntersection();
        Shape closestShape = closestIntersection.getShape();

        // Get all Lights in the scene
        List<Light> sceneLights = mScene.getLightList();
        // Light list for the Lights which hit the Intersection Point
        List<Light> objectLights = new ArrayList<>();

        // Rays for every light in our Lightlist
        for (Light light : sceneLights) {
            //System.out.println(light.getLocation().x + ", " + light.getLocation().z);
            Ray secondaryRay = new Ray(pointOfIntersection, light.getLocation());
            boolean hit = false;
            List<Shape> shapeList = mScene.getShapeList();
            for (Shape shape : shapeList) {
                // Ignore self Collision
                if (shape != closestShape) {
                    Intersection intersectionLight = new Intersection(secondaryRay, shape);

                    // Checks if the lightRay intersects a shape
                    if (intersectionLight.isHit()) {
                        // Check if the ray hits the Light before it hits the Object
                        if (pointOfIntersection.sub(light.getLocation()).length() > (float) intersectionLight.getDistance()) {
                            hit = true;
                        }
                    }
                }
            }
            // Add light to a List if it isn't obscured
            if (!hit) {
                objectLights.add(light);
            }
        }
        RgbColor color = mBackgroundColor;
        // If there is an intersection with the primary ray we calculate the color
        if (closestShape != null) {
            Vec3 viewVector = camera.getCameraPosition().sub(pointOfIntersection).normalize();
            color = closestShape.getColor(objectLights, viewVector);
        }
        return color;
    }

    /**
     * Converts a Window-Pixel Coordinate to a Viewplane Coordinate.
     *
     * @param x The x coordinate of the window
     * @param y The y coordinate of the window
     * @return The corresponding Viewplane Coordinate.
     */
    public Vec2 normalizePixel(float x, float y) {
        Vec2 viewplaneCoordinate = new Vec2(2 * (( x + 0.5f) / pixelWidth) - 1, 2 * (( y + 0.5f) / pixelHeight) - 1);
        viewplaneCoordinate.x = viewplaneCoordinate.x * (imageWidth / 2);
        viewplaneCoordinate.y = viewplaneCoordinate.y * (imageHeight / 2 * -1);
        return viewplaneCoordinate;
    }
}
