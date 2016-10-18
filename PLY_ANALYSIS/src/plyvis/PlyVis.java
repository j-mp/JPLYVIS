package plyvis;
import java.util.ArrayList;

import com.sun.scenario.Settings;

import datastructures.Point4f;
import datastructures.TetrahedronMesh;
import javafx.geometry.Bounds;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Shape3D;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class PlyVis {

	private static final double AXIS_LENGTH = 5000.0;
	private static final double AXIS_WIDTH = 25.0;
	int width = 1024;
	int height = 768;
	public CameraAnimation camAni;
	
	private ArrayList<Point4f> points;

	public PlyVis(ArrayList<Point4f> points, int w, int h) {
		this.points = points;
		this.width = w;
		this.height = h;
	}
	
	double anchorX, anchorY, anchorAngle;
	
	public Object createIntialThreeDScene(boolean suborly, PLYVisMode mode) {
		// Container
		Group root = new Group();
		root.setRotationAxis(Rotate.Y_AXIS);
		
		// Creating 3D
		Node n = new Group();
		
		switch (mode) {
			case MESH_LINES:
				n = createMeshView(true);
				break;
			case MESH_FULL:
				n = createMeshView(false);
				break;
			case PRIMITIVES:
				n = createPrimitives();
				break;
		}
		
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
			

		}
			
		n.setId("vis");
		root.getChildren().add(movetoOrgin(buildAxes()));
		root.getChildren().add(movetoOrgin(n));
		root.getChildren().add(ambient); // point
		root.setId("PLYVIS");
		
//		ArrayList<Stop> stops = new ArrayList<Stop>();
//		stops.add(new Stop(0, Color.DARKBLUE));
//		stops.add(new Stop(500, Color.DARKGRAY));
		
		
		// Creating Perspective View Camera
		PerspectiveCamera cam = new PerspectiveCamera(true);
		cam.setFarClip(Integer.MAX_VALUE);
		Rotate rx = new Rotate(45.0, Rotate.X_AXIS);
		Rotate ry = new Rotate(45.0, Rotate.Y_AXIS);
		Rotate rz = new Rotate(45.0, Rotate.Z_AXIS);
		int camZdist = -15000;
		Translate tz = new Translate(0.0, 0.0, camZdist);
		cam.getTransforms().addAll(rx, ry, rz, tz);
		
		camAni = new CameraAnimation(cam);
		camAni.startAnimation();
		
		Object returnscene;

		SubScene scene = new SubScene(root, width, height, true, SceneAntialiasing.BALANCED);
//		scene.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops));
	    scene.setFill(Color.WHITESMOKE);
		scene.setCamera(cam);
		returnscene = scene;

		
		new FxCameraInteractionPlyVis((SubScene)returnscene, root, cam, camAni, camZdist, 100);
		
		return returnscene;
	}

	public Node movetoOrgin(Node n) {
		Bounds b = n.localToScene(n.getBoundsInLocal());
		
		// System.out.println("bou:" + b);
		double tx, ty, tz;
		
		if (b.getMinX() > 0.0)
			tx = - (b.getWidth() / 2 + b.getMinX());
		else
			tx = Math.abs(b.getMinX()) - b.getWidth() / 2;
		
		if (b.getMinY() > 0.0)
			ty = - (b.getHeight() / 2 + b.getMinY());
		else
			ty = Math.abs(b.getMinY()) - b.getHeight() / 2;
		
		if (b.getMinZ() > 0.0)
			tz = - (b.getDepth() / 2 + b.getMinZ());
		else
			tz = Math.abs(b.getMinZ()) - b.getDepth() / 2;
		
//		System.out.println("trans_x:" + tx + " y: " + ty + " z: " + tz);
		
		n.getTransforms().add(new Translate(tx, ty, tz));
		
		return n;
	}

	public Group createPrimitives() {
		ArrayList<Shape3D> shapes = new ArrayList<>();
		for (int i = 0; i < points.size(); i+=1) {
			Point4f p = points.get(i);

			Box prim = new Box(10.0, 10.0, 10.0);
			
			double green_intensity = 0.0;
			
			if ((0.5 + p.getIntensity() / 255d) > 0.99)
				green_intensity =  1.0;
				else
					green_intensity = (0.5 + p.getIntensity() / 255d);
			
			Color col = new Color(p.getIntensity() / 255d, green_intensity, p.getIntensity() / 255d, 1.0);
			
			PhongMaterial material = new PhongMaterial();
			// Diffuse Color
			material.setDiffuseColor(col);
			// Specular Color
			material.setSpecularColor(col);
			material.setSpecularPower(39.0);
			prim.setMaterial(material);

			prim.setTranslateX(p.getX() * 50);
			prim.setTranslateY(p.getY() * 50);
			prim.setTranslateZ(p.getZ() * -50);
			
			shapes.add(prim);
		}
		
		Group res = new Group();
		
		for (Shape3D sh : shapes)
			res.getChildren().addAll(sh);
		
		return res;
	}
	
	public Group createMeshView(boolean showLines) {
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
		
//		System.out.println(bounds.toString());

		mv.setScaleX(0.5);
		mv.setScaleY(0.5);
		mv.setScaleZ(0.5);
		
		Group g = new Group();
		g.getChildren().add(mv);
		
		return g;
	}
	
	
	public SubScene createSubScene(PLYVisMode vismode) {
		return (SubScene) createIntialThreeDScene(true, vismode);
	}
	
	private Node buildAxes() {
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
		axisGroup.setId("AXIS");
		
		if (Settings.getBoolean("Show axes"))
			axisGroup.setVisible(true);
		else
			axisGroup.setVisible(false);
        
        return axisGroup;
    }
}
