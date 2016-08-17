import java.io.Serializable;

public class Point4f implements Serializable {

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

	float x;
	float y;
	float z;
	private float z_norm;
	private float y_norm;
	private float x_norm;
	float intensity;

	public Point4f(float x, float y, float z, float normal_x, float normal_y, float normal_z) {
		this.x = x;
		this.y = y;
		this.z = z;
		x_norm = normal_x;
		y_norm = normal_y;
		z_norm = normal_z;
	}

	public Point4f(float x, float y, float z, float intensity) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.intensity = intensity;
	}

	public void scale(int s) {
		this.x = this.x * s;
		this.y = this.y * s;
		this.z = this.z * s;
	}
}
