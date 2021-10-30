// ************************************************************ //
//                      Hochschule Duesseldorf                  //
//                                                              //
//                     Vertiefung Computergrafik                //
// ************************************************************ //


/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    1. Documentation:    Did you comment your code shortly but clearly?
    2. Structure:        Did you clean up your code and put everything into the right bucket?
    3. Performance:      Are all loops and everything inside really necessary?
    4. Theory:           Are you going the right way?

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

 <<< YOUR TEAM NAME >>>

     Master of Documentation:
     Master of Structure:
     Master of Performance:
     Master of Theory:

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

import raytracer.Raytracer;
import scene.*;
import ui.Window;
import utils.RgbColor;
import utils.algebra.Vec3;



/*
    - THE RAYTRACER -

    TEAM:

    1. Josephine Faasen - Documentation
    2. Maja Michaelis - Performance
    3. Sandra Radzanowski - Theory
    4. Ole Glitza - Structure
 */

// Main application class. This is the routine called by the JVM to run the program.
public class Main {

    /** RESOLUTION **/

    static final int IMAGE_WIDTH = 800;
    static final int IMAGE_HEIGHT = 600;

    /** CORNELL_BOX_DIMENSION **/

    static final float BOX_DIMENSION = 4f;

    /** RAYTRACER **/

    static final int RECURSIONS = 2;
    static final int ANTI_ALIASING = 4;
    static final boolean USE_SOFT_SHADOWS = false;

    /** LIGHT **/
    static final short LIGHT_DENSITY = 20;
    static final short LIGHT_SAMPLES = 40;

    static final RgbColor BACKGROUND_COLOR = RgbColor.BLACK;

    static final Vec3 LIGHT_POSITION = new Vec3( 0, 4, 0);
    static final short AREA_LIGHT_SIZE = 2;

    /** GI **/
    static final boolean USE_GI = false;
    static final int GI_LEVEL = 0;
    static final int GI_SAMPLES = 0;

    static final float AMBIENT_INTENSITY = 0.125f;
    static final RgbColor LIGHT_COLOR = new RgbColor(1f, 1f, 1f);
    static final RgbColor AMBIENT_LIGHT = LIGHT_COLOR.multScalar(AMBIENT_INTENSITY);



    static final boolean USE_AO = false;
    static final int NUMBER_OF_AO_SAMPLES = 0;
    static final float AO_MAX_DISTANCE = 0f;


    /** REFRACTION INDICES **/
    static final float REFRACTION_AIR = 1f;
    static final float REFRACTION_WATER = 1.3f;
    static final float REFRACTION_GLASS = 1.5f;
    static final float REFRACTION_DIAMOND = 2.4f;
    static final float NO_REFRACTION = 0.0f;

    /** CAMERA **/

    static final Vec3 CAM_POS = new Vec3(0, 0, 9);
    static final Vec3 LOOK_AT = new Vec3(0, 0, 0);
    static final Vec3 UP_VECTOR = new Vec3(0, 1, 0);

    static final float VIEW_ANGLE = 70f;
    static final float FOCAL_LENGTH = 1f;

    /** DEBUG **/

    static final boolean SHOW_PARAM_LABEL = true;


    /** Initial method. This is where the show begins. **/
    public static void main(String[] args){
        Window renderWindow = new Window(IMAGE_WIDTH, IMAGE_HEIGHT);

        System.out.printf("Hello World! Again!");
        draw(renderWindow);
    }

    /**  Draw the scene using our Raytracer **/
    private static void draw(Window renderWindow){
        Scene renderScene = new Scene();

        setupScene(renderScene);

        raytraceScene(renderWindow, renderScene);
    }

    /** Setup all components that we want to see in our scene **/
    private static void setupScene(Scene renderScene){
        setupCameras(renderScene);

        setupCornellBox(renderScene);

        setupObjects(renderScene);

        setupLights(renderScene);
    }

    private static void setupLights(Scene renderScene) {
        // Light Setup
        //renderScene.createPointlight(RgbColor.WHITE, LIGHT_POSITION);
        renderScene.createArealight(RgbColor.WHITE, LIGHT_POSITION, LIGHT_SAMPLES);
    }

    private static void setupCameras(Scene renderScene) {
        // Camera Setup
        renderScene.createPerspCamera(CAM_POS, LOOK_AT, UP_VECTOR, VIEW_ANGLE, FOCAL_LENGTH);
    }

    private static void setupObjects(Scene renderScene) {
        // Setup Materials
        Material refractive = new Phong(RgbColor.CYAN, RgbColor.WHITE, 80f, AMBIENT_LIGHT, 0.0f, REFRACTION_DIAMOND);
        Material redPhong = new Phong(RgbColor.RED, RgbColor.WHITE, 80f, AMBIENT_LIGHT, 0.0f, NO_REFRACTION);
        Material reflective = new Phong(RgbColor.WHITE, RgbColor.WHITE, 80f, AMBIENT_LIGHT, 1.0f, NO_REFRACTION);

        // Creates the objects in the scene
        renderScene.createSphere(-1f, -3.5f, -2f, 1f, redPhong);
        renderScene.createSphere(1f, -3.5f, 0f, 1f, reflective);
        renderScene.createSphere(-2f, -3.5f, 0f, 1f, refractive);
    }

    private static void setupCornellBox(Scene renderScene) {
        // Setup Materials
        Material white = new Lambert(RgbColor.WHITE, AMBIENT_LIGHT);
        Material red = new Lambert(RgbColor.RED, AMBIENT_LIGHT);
        Material blue = new Lambert(RgbColor.BLUE, AMBIENT_LIGHT);
        Material whiteUnlit = new Unlit(RgbColor.WHITE);

        // Background plane
        renderScene.createPlane(new Vec3(0f, 0f, -5f), new Vec3(0f, 0f, 1f), white);
        // right plane
        renderScene.createPlane(new Vec3(6f, 0f, 0f), new Vec3(-1f, 0f, 0f), blue);
        // left plane
        renderScene.createPlane(new Vec3(-6f, 0f, 0f), new Vec3(1f, 0f, 0f), red);
        // floor plane
        renderScene.createPlane(new Vec3(0f, -4.5f, 0f), new Vec3(0f,1f,0f), white);
        // ceiling plane
        renderScene.createPlane(new Vec3(0f, 4.5f, 0f), new Vec3(0f, -1f, 0f), white);
        // ceiling Light
        renderScene.createSquare(new Vec3(0f, 4.3f, 0f), new Vec3(0f, -1f, 0f), whiteUnlit, 2f);
    }

    /** Create our personal renderer and give it all of our items and prefs to calculate our scene **/
    private static void raytraceScene(Window renderWindow, Scene renderScene){
        Raytracer raytracer = new Raytracer(
                renderScene,
                renderWindow,
                RECURSIONS,
                BACKGROUND_COLOR,
                AMBIENT_LIGHT,
                ANTI_ALIASING,
                SHOW_PARAM_LABEL,
                IMAGE_WIDTH,
                IMAGE_HEIGHT);

        raytracer.renderScene();
    }
}