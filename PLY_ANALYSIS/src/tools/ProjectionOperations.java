package tools;

import java.awt.Color;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferFloat;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;

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
	public static BufferedImage createZProjection(PointCloudDataSet dataSet, int size) {
		
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
	
	/**
	 * Float export
	 * @param dataSet
	 * @param size_x
	 * @return
	 */
	public static BufferedImage createZProjectionFloat(PointCloudDataSet dataSet, int size_x) {
		
		double factor_size_x = size_x / dataSet.getX_width();
		int size_y = (int) (dataSet.getY_width() * factor_size_x);
		
		// Define dimensions and layout of the image
        int bands = 1; // 4 bands for ARGB, 3 for RGB etc
        int[] bandOffsets = {0}; // length == bands, 0 == R, 1 == G, 2 == B and 3 == A

        // Create a TYPE_FLOAT sample model (specifying how the pixels are stored)
        SampleModel sampleModel = new PixelInterleavedSampleModel(DataBuffer.TYPE_FLOAT, size_x, size_y, bands, size_x  * bands, bandOffsets);
        // ...and data buffer (where the pixels are stored)
        DataBuffer buffer = new DataBufferFloat(size_x * size_y * bands);

        // Wrap it in a writable raster
        WritableRaster raster = Raster.createWritableRaster(sampleModel, buffer, null);

        // Create a color model compatible with this sample model/raster (TYPE_FLOAT)
        // Note that the number of bands must equal the number of color components in the 
        // color space (3 for RGB) + 1 extra band if the color model contains alpha 
        ColorSpace colorSpace = ColorSpace.getInstance(ColorSpace.CS_GRAY);
        ColorModel colorModel = new ComponentColorModel(colorSpace, false, false, Transparency.TRANSLUCENT, DataBuffer.TYPE_FLOAT);
		
        float[] farray = null;
        
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
			
			int xi = (int) (x * size_x / dataSet.getX_width());
			int yi = (int) (y * size_y / dataSet.getY_width());
			
			int zi = (int) (z * 255 / dataSet.getZ_width());
			
			if (xi < size_x && yi < size_y) {
				float[] ap = raster.getPixel(xi, yi, farray);
			
				// merge pixel values in case of double sampling
				if(ap[0] != 0.0) {
					raster.setPixel(xi, yi, ap);
				} else
					raster.setPixel(xi, yi, new float[]{z});
			
			}
		}
		
        // And finally create an image with this raster
        BufferedImage bf = new BufferedImage(colorModel, raster, colorModel.isAlphaPremultiplied(), null);
		
		return bf;
	}
}
