
public class Point3f {

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

	public float getIntensity() {
		return intensity;
	}

	private float x;
	private float y;
	private float z;
	private float z_norm;
	private float y_norm;
	private float x_norm;
	private float intensity;

	public Point3f(float x, float y, float z, float normal_x, float normal_y, float normal_z) {
		this.x = x;
		this.y = y;
		this.z = z;
		x_norm = normal_x;
		y_norm = normal_y;
		z_norm = normal_z;
	}

	public Point3f(float x, float y, float z, float intensity) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.intensity = intensity;
	}

}
