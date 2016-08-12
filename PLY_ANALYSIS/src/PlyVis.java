import java.util.ArrayList;

import javax.vecmath.Point3d;

import iap.blocks.debug.FxCameraAnnimation;
import javafx.geometry.Bounds;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.scene.shape.Shape;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.Sphere;
import javafx.scene.Node;
import javafx.scene.shape.Line;
import javafx.scene.shape.*;

public class PlyVis {

	private static final double AXIS_LENGTH = 5000.0;
	private static final double AXIS_WIDTH = 25.0;
	int width = 1024;
	int height = 768;
	
	private ArrayList<Point4f> points;

	public PlyVis(ArrayList<Point4f> points) {
		this.points = points;
	}
	
	double anchorX, anchorY, anchorAngle;
	
	public Object create(boolean suborly, PLYVisMode mode) {
		// Container
		Group root = new Group();
		root.setTranslateZ(width / 2);
		root.setRotationAxis(Rotate.Y_AXIS);
		
		// Creating Mesh
		Node n;
		
//		switch (mode) {
//			case MESH_LINES:
//				n = createMeshView(true);
//				break;
//			case MESH_FULL:
//				n = createMeshView(false);
//				break;
//			case PRIMITIVES:
//				n = cratePrimitives();
//				break;
//		}
		
		n = new Group();
		
		root.getChildren().add(0, buildAxes());
		//root.getChildren().add(1, n);
		
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
			point.getScope().add(n);
			
			root.getChildren().addAll(point, ambient); // point
		}
		
		// Adding to scene
		
//		ArrayList<Stop> stops = new ArrayList<Stop>();
//		stops.add(new Stop(0, Color.DARKBLUE));
//		stops.add(new Stop(500, Color.DARKGRAY));
		
		
		// Creating Perspective View Camera
		PerspectiveCamera cam = new PerspectiveCamera(false);
		cam.setFarClip(Integer.MAX_VALUE);
		Rotate rz = new Rotate(45.0, Rotate.X_AXIS);
		int camZdist = -20000;
		Translate tz = new Translate(0.0, 0.0, camZdist);
		cam.getTransforms().add(rz);
		cam.getTransforms().add(tz);
		
		
		FxCameraAnnimation camAni = new FxCameraAnnimation(cam);
//		camAni.startAnimation();
		
		Object returnscene;
		if (suborly) {
			SubScene scene = new SubScene(root, 1024, 768, true, SceneAntialiasing.BALANCED);
//			scene.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops));
		    scene.setFill(Color.WHITESMOKE);
			scene.setCamera(cam);
			returnscene = scene;
		} else {
			Scene scene = new Scene(root, 1024, 768, true, SceneAntialiasing.BALANCED);
//			scene.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops));
		    scene.setFill(Color.WHITESMOKE);
			scene.setCamera(cam);
			returnscene = scene;
		}
		
		new FxCameraInteractionPlyVis((SubScene)returnscene, root, cam, camAni, camZdist, 100);
		
		return returnscene;
	}

	private Group cratePrimitives() {
		ArrayList<Shape3D> shapes = new ArrayList<>();
		for (int i = 0; i < points.size(); i+=20) {
			Point4f p = points.get(i);
			// new Box(1.0, 1.0, 1.0);



			Polygon prim = new Polygon();
			prim.getPoints().addAll(new Double[]{
			    0.0, 0.0,
			    20.0, 10.0,
			    10.0, 20.0 });
			
			Color col = new Color(p.intensity / 255d, p.intensity / 255d, p.intensity / 255d, 1.0);
			
			PhongMaterial material = new PhongMaterial();
			// Diffuse Color
			material.setDiffuseColor(col);
			// Specular Color
			material.setSpecularColor(col);
			material.setSpecularPower(39.0);
			prim.setMaterial(material);
			//		c.setTranslateX(rand + xc + (x - n / 2d) * radius);
			//		c.setTranslateY(rand + yc + (y - n / 2d) * radius);
			//		c.setTranslateZ(rand + (z - n / 2d) * radius);
			
			prim.setScaleX(100.0);
			prim.setScaleY(100.0);
			prim.setScaleZ(100.0);
			
			double a = 6.0 - 2500.0 / 2 + 750;
			double b = -90.0 - Math.abs(16503) / 2 -500; // green
			double c = -209.0 + Math.abs(-23257) - 2000; // blue
			prim.setTranslateX(a + p.x*100);
			prim.setTranslateY(b + p.y*100);
			prim.setTranslateZ(c + p.z*100);
			
			
			shapes.add(prim);
		}
		Group res = new Group();
		
		for (Shape3D sh : shapes)
			res.getChildren().addAll(sh);
		
		return res;
	}
	
	private Group createMeshView(boolean showLines) {
		TetrahedronMesh tm = new TetrahedronMesh(25.0, points);
		
		MeshView mv;
		mv = new MeshView(tm);
		
		if(showLines)
			mv.setDrawMode(DrawMode.LINE);
		
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
		
		Group g = new Group();
		g.getChildren().add(mv);
		
		return g;
	}
	
	public Scene createScene(PLYVisMode vismode) {
		return (Scene) create(false, vismode);
	}
	
	public SubScene createSubScene(PLYVisMode vismode) {
		return (SubScene) create(true, vismode);
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
 
        // xAxis.setOpacity(0.5);
        
        Group axisGroup = new Group();
		axisGroup.getChildren().addAll(xAxis, yAxis, zAxis);
        axisGroup.setVisible(true);
        
        return axisGroup;
    }
}
