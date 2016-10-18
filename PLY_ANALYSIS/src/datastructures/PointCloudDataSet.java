package plyvis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jply.Element;
import jply.ElementReader;
import jply.ElementType;
import jply.PlyReader;
import jply.PlyReaderFile;
import jply.Property;

public class DataSet {

	ArrayList<Point4f> pointlist = null;
	static double min_x = Double.MAX_VALUE;
	static double min_y = Double.MAX_VALUE;
	static double min_z = Double.MAX_VALUE;
	static double max_x = Double.MIN_VALUE;
	static double max_y = Double.MIN_VALUE;
	static double max_z = Double.MIN_VALUE;
	
	public double getX_width() {
		return max_x - min_x;
	}

	public double getY_width() {
		return max_y - min_y;
	}

	public double getZ_width() {
		return max_z - min_z;
	}

	public DataSet(String absolutePath) throws IOException {
		pointlist = loadData(absolutePath);
	}
	
	public ArrayList<Point4f> getPointlist() {
		return pointlist;
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
		
		if (PLYSettings.debug.getValue()) {
			System.out.println(reader.getCount());
			System.out.println(type);
		}
		
		List<Property> props = type.getProperties();
		
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
			
			if (x < min_x)
				min_x = x;
			
			if (y < min_y)
				min_y = y;
			
			if (z < min_z)
				min_z = z;
			
			// if (z > 226f)
			points.add(new Point4f((float) x, (float) y, (float) z, (float) intensity));

		}
		
		if (PLYSettings.debug.getValue())
			System.out.println("Inp-bounds: " + min_x + ":" + max_x + " | "  + min_y + ":" + max_y + " | "  + min_z + ":" + max_z);
		
		reader.close();

		return points;
	}
}
