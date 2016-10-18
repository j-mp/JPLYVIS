package tools;

import java.awt.Color;
import java.awt.image.BufferedImage;

import datastructures.Point4f;
import datastructures.PointCloudDataSet;

public class Projection {

	/**
	 * Method to export an gray-image of the z-projection. Double sampled points will be averaged.
	 * TODO Read sample-rate from ply file?? Or try to evaluate.
	 * @param dataSet
	 * @param size - x size (width) of the exported image, related to the scanner sampling
	 * @return buffered image
	 */
	public static BufferedImage createProjection(PointCloudDataSet dataSet, int size) {
		
		double factor_size_x = size / dataSet.getX_width();
		int size_y = (int) (dataSet.getY_width() * factor_size_x);
		BufferedImage bf = new BufferedImage(size, size_y, BufferedImage.TYPE_INT_RGB);
		
		for(Point4f point : dataSet.getPointlist()) {
			float x = point.getX();
			
			if (dataSet.getMin_x() < 0)
				x -= dataSet.getMin_x();
			else
				x -= dataSet.getMin_x();
			
			float y = point.getY();
			
			if (dataSet.getMin_y() < 0)
				y -= dataSet.getMin_y();
			else
				y -= dataSet.getMin_y();
			
			float z = point.getZ();
			
			if (dataSet.getMin_z() < 0)
				z -= dataSet.getMin_z();
			else
				z -= dataSet.getMin_z();
			
			//System.out.println(x + " | " + y);
			
			int xi = (int) (x * size / dataSet.getX_width());
			int yi = (int) (y * size_y / dataSet.getY_width());
			
			int zi = (int) (z * 255 / dataSet.getZ_width());
			
			if (xi < bf. getWidth() && yi < bf.getHeight() && zi < 255) {
				int ap = bf.getRGB(xi, yi);
				int r = (ap & 0xff0000) >> 16;
			
				// merge pixel values in case of double sampling
				if(r != 0) {
					bf.setRGB(xi, yi, new Color((int) ((zi + r) / 2d), (int) ((zi + r) / 2d), (int) ((zi + r) / 2d)).getRGB());
				} else
					bf.setRGB(xi, yi, new Color(zi, zi, zi).getRGB());
			
			}
		}
		
		return bf;
	}
}
