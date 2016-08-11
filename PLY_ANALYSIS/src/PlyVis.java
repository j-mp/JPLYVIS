import java.awt.Dimension;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.vecmath.Point3d;

import iap.blocks.debug.FxCameraAnnimation;
import iap.blocks.debug.FxCameraInteraction;
import javafx.collections.ObservableMap;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Bounds;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Shape3D;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.scene.shape.CullFace;
import javafx.application.Application;
import javafx.scene.SubScene;

public class PlyVis {

	private static final double AXIS_LENGTH = 5000.0;
	private static final double AXIS_WIDTH = 25.0;
	int width = 1024;
	int height = 768;
	
	private ArrayList<Point3d> points;

	public PlyVis(ArrayList<Point3d> points) {
		this.points = points;
	}
	
	double anchorX, anchorY, anchorAngle;
	
	public SubScene createScene() {
		// Container
		Group root = new Group();
		root.setTranslateZ(width / 2);
		root.setRotationAxis(Rotate.Y_AXIS);
		
		// Creating Mesh
		TetrahedronMesh tm = new TetrahedronMesh(25.0, points);
		
		MeshView mv = new MeshView(tm);
		mv.setDrawMode(DrawMode.FILL);
		
		PhongMaterial pmaterial = new PhongMaterial();
		Color col = new Color(0.7, 1.0, 0.7, 1.0);
		pmaterial.setDiffuseColor(col);
		pmaterial.setSpecularColor(col);
		pmaterial.setSpecularPower(39.0);
		mv.setMaterial(pmaterial);
		
		mv.setCullFace(CullFace.NONE);
		
		Bounds bounds = mv.getBoundsInParent();
		
		System.out.println(bounds.toString());

		mv.setScaleX(0.5);
		mv.setScaleY(0.5);
		mv.setScaleZ(0.5);
		
		double a = 6.0 - 2500.0 / 2 + 750;
		double b = -90.0 - Math.abs(bounds.getMaxY()) / 2 -500; // green
		double c = -209.0 + Math.abs(bounds.getMinZ()) - 2000; // blue
		mv.setTranslateX(a);
		mv.setTranslateY(b);
		mv.setTranslateZ(c);

		root.getChildren().add(0, buildAxes());
		root.getChildren().add(1, mv);
		
		// Creating Ambient Light
		AmbientLight ambient = new AmbientLight();
		ambient.setColor(Color.rgb(200, 200, 200, 0.9));
		{
			// Creating Point Light
			PointLight point = new PointLight();
			point.setColor(Color.ANTIQUEWHITE);
			point.setLayoutX(width + 700);
			point.setLayoutY(height / 2);
			point.setTranslateZ(-10000);
			point.getScope().add(mv);
			
			root.getChildren().addAll(point, ambient); // point
		}
		
		// Adding to scene
		SubScene scene = new SubScene(root, 1024, 768, true, SceneAntialiasing.DISABLED);
		ArrayList<Stop> stops = new ArrayList<Stop>();
		stops.add(new Stop(0, Color.DARKBLUE));
		stops.add(new Stop(500, Color.DARKGRAY));
		scene.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops));
		
		// Creating Perspective View Camera
		PerspectiveCamera cam = new PerspectiveCamera(true);
		cam.setFarClip(Integer.MAX_VALUE);
		Rotate rz = new Rotate(45.0, Rotate.X_AXIS);
		int camZdist = -20000;
		Translate tz = new Translate(0.0, 0.0, camZdist );
		cam.getTransforms().add(rz);
		cam.getTransforms().add(tz);
		scene.setCamera(cam);
		
		FxCameraAnnimation camAni = new FxCameraAnnimation(cam);
		camAni.startAnimation();
		
		new FxCameraInteractionPlyVis(scene, root, cam, camAni, camZdist, 100);
		
		return scene;
	}
	
	private Group buildAxes() {
        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.RED);
 
        final PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(Color.DARKGREEN);
        greenMaterial.setSpecularColor(Color.GREEN);
 
        final PhongMaterial blueMaterial = new PhongMaterial();
        blueMaterial.setDiffuseColor(Color.DARKBLUE);
        blueMaterial.setSpecularColor(Color.BLUE);
 
        final Box xAxis = new Box(AXIS_LENGTH, AXIS_WIDTH, AXIS_WIDTH);
        final Box yAxis = new Box(AXIS_WIDTH, AXIS_LENGTH, AXIS_WIDTH);
        final Box zAxis = new Box(AXIS_WIDTH, AXIS_WIDTH, AXIS_LENGTH);
        
        xAxis.setMaterial(redMaterial);
        yAxis.setMaterial(greenMaterial);
        zAxis.setMaterial(blueMaterial);
 
        Group axisGroup = new Group();
		axisGroup.getChildren().addAll(xAxis, yAxis, zAxis);
        axisGroup.setVisible(true);
        
        return axisGroup;
    }
}
