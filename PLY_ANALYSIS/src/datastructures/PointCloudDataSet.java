package datastructures;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jply_io.Element;
import jply_io.ElementReader;
import jply_io.ElementType;
import jply_io.PlyReader;
import jply_io.PlyReaderFile;
import jply_io.Property;
import plyvis.PLYSettings;

/**
 * 
 * @author jmp
 *
 */
public class PointCloudDataSet {

	ArrayList<Point4f> pointlist = null;
	private static double min_x = Double.MAX_VALUE;
	private static double min_y = Double.MAX_VALUE;
	private static double min_z = Double.MAX_VALUE;
	static double max_x = Double.MIN_VALUE;
	static double max_y = Double.MIN_VALUE;
	static double max_z = Double.MIN_VALUE;

	public PointCloudDataSet(String absolutePath) throws IOException {
		pointlist = loadData(absolutePath);
	}

	private ArrayList<Point4f> loadData(String filepath) throws IOException {
		PlyReader ply = new PlyReaderFile(filepath);
		
		List<String> header = ply.getRawHeaders();
		
		if (PLYSettings.debug.getValue()) {
			for (String s : header) {
				System.out.println(s);
			}
			
			List<ElementType> etypes = ply.getElementTypes();
			
			for (ElementType et : etypes) {
				System.out.println(et.getName());
			}
		}
		ArrayList<Point4f> points = readElements(ply);
		
		return points;
	}
	
	/**
	 * Method for reading phenospex .ply data.
	 * @param ply
	 * @return
	 * @throws IOException
	 */
	private static ArrayList<Point4f> readElements(PlyReader ply) throws IOException {
		
		ArrayList<Point4f> points = new ArrayList<>();
		
		ElementReader reader = ply.nextElementReader();
		
		ElementType type = reader.getElementType();
		
		List<Property> props = type.getProperties();
		
		if (PLYSettings.debug.getValue()) {
			System.out.println(reader.getCount());
			System.out.println(type);
			System.out.println(props.toString());
		}
		
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
				if (p.getName().equals("intensity"))
					intensity =  ee.getDouble(p.getName());
				
			}
			
			if (x > max_x)
				max_x = x;
			
			if (y > max_y)
				max_y = y;
			
			if (z > max_z)
				max_z = z;
			
			if (x < getMin_x())
				setMin_x(x);
			
			if (y < getMin_y())
				setMin_y(y);
			
			if (z < getMin_z())
				setMin_z(z);
			
			// if (z > 226f)
			points.add(new Point4f((float) x, (float) y, (float) z, (float) intensity));

		}
		
		if (PLYSettings.debug.getValue())
			System.out.println("Inp-bounds: " + getMin_x() + ":" + max_x + " | "  + getMin_y() + ":" + max_y + " | "  + getMin_z() + ":" + max_z);
		
		reader.close();

		return points;
	}

	public static double getMin_x() {
		return min_x;
	}

	public static void setMin_x(double min_x) {
		PointCloudDataSet.min_x = min_x;
	}

	public static double getMin_y() {
		return min_y;
	}

	public static void setMin_y(double min_y) {
		PointCloudDataSet.min_y = min_y;
	}

	public static double getMin_z() {
		return min_z;
	}

	public static void setMin_z(double min_z) {
		PointCloudDataSet.min_z = min_z;
	}
	
	public double getX_width() {
		return max_x - getMin_x();
	}

	public double getY_width() {
		return max_y - getMin_y();
	}

	public double getZ_width() {
		return max_z - getMin_z();
	}
	
	public ArrayList<Point4f> getPointlist() {
		return pointlist;
	}
}
