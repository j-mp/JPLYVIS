import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3d;

import org.apache.commons.math3.random.StableRandomGenerator;
import org.jfree.ui.StandardGradientPaintTransformer;

import jply.Element;
import jply.ElementReader;
import jply.ElementType;
import jply.PlyReader;
import jply.PlyReaderFile;
import jply.Property;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.SubScene;

public class main extends Application{

	static ArrayList<Point3d> points;
	
	public static void main(String[] args) throws Exception {
		
		String filepath = "/home/pape/Schreibtisch/test.ply";
		
		PlyReader ply = new PlyReaderFile(filepath);
		
		List<String> header = ply.getRawHeaders();
		
		for (String s : header) {
			System.out.println(s);
		}
		
		List<ElementType> etypes = ply.getElementTypes();
		
		for (ElementType et : etypes) {
			System.out.println(et.getName());
		}
		
		points = readElements(ply);
		
		
		Application.launch(args);

	}

	/**
	 * Method for reading phenospex .ply data.
	 * @param ply
	 * @return
	 * @throws IOException
	 */
	private static ArrayList<Point3d> readElements(PlyReader ply) throws IOException {
		
		ArrayList<Point3d> points = new ArrayList<>();
		
		ElementReader reader = ply.nextElementReader();
		
		// System.out.println(reader.getCount());
		
		ElementType type = reader.getElementType();
		// System.out.println(type);
		
		List<Property> props = type.getProperties();
		
		double min_x = Double.MAX_VALUE, min_y = Double.MAX_VALUE, min_z = Double.MAX_VALUE, max_x = Double.MIN_VALUE, max_y = Double.MIN_VALUE, max_z = Double.MIN_VALUE;
		
		for (int i = 0; i < reader.getCount(); i++) {

			Element ee = reader.readElement();

			double x = 0, y = 0, z = 0, intensity = 0;
			for (Property p : props) {
				
				if (p.getName().equals("x"))
					x = ee.getDouble(p.getName());
				if (p.getName().equals("y"))
					y =  ee.getDouble(p.getName());
				if (p.getName().equals("z"))
					z =  ee.getDouble(p.getName());
//				if (p.getName().equals("intensity"))
//					intensity =  ee.getDouble(p.getName());
				
			}
			
			if (x > max_x)
				max_x = x;
			
			if (y > max_y)
				max_y = y;
			
			if (z > max_z)
				max_z = z;
			
			if (x < min_x)
				min_x = x;
			
			if (y < min_y)
				min_y = y;
			
			if (z < min_z)
				min_z = z;
			
			points.add(new Point3d(x, y, z));

		}
		
		System.out.println("Inp-bounds: " + min_x + ":" + max_x + " | "  + min_y + ":" + max_y + " | "  + min_z + ":" + max_z);
		
		reader.close();

		return points;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		PlyVis vis = new PlyVis(points);
		
		SubScene s = vis.createScene();
		
		HBox hbox = new HBox();
	    hbox.setLayoutX(75);
	    hbox.setLayoutY(200);
		
	    hbox.getChildren().add(s);
	    
		PlyJFrame frame = new PlyJFrame();
		frame.create(primaryStage, hbox);
		frame.setVisible(true);
	}

}
