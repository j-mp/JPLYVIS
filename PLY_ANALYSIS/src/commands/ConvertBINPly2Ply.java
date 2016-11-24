package commands;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import datastructures.Point4f;
import datastructures.PointCloudDataSet;
import plyvis.PLYSettings;


public class ConvertBINPly2Ply {

	private static PrintWriter sb;

	public static void main(String[] args) {
		
		if (args.length < 2)
			System.out.println("Missing parameters, exit.");
			System.exit(0);
		
		// get parms
		String inputfile = args[0];
		String outfile = args[1];
		
		PLYSettings settings = new PLYSettings();
		settings.debug.set(true);
		
		try {
			   PointCloudDataSet dataset = new PointCloudDataSet(inputfile);
			   ArrayList<Point4f> pointlist = dataset.getPointlist();
			   
			   sb = new PrintWriter(new BufferedWriter(new FileWriter(outfile)));
			   for (int i = 0; i < pointlist.size(); i++) {  
				   Point4f p = pointlist.get(i);
				   String temp = p.getX() + ", " + p.getY() + ", " + p.getZ() + "\n";
				   sb.write(temp);
			   }
			   sb.close();
			   
		   } catch (IOException e) {
			   // TODO Auto-generated catch block
			   System.out.println("Could not load data.");
			   e.printStackTrace();
		   }
	}

}
