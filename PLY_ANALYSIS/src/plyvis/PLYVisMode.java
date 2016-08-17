package plyvis;
import javafx.beans.property.IntegerProperty;

public enum PLYVisMode {
	MESH_LINES, MESH_FULL, PRIMITIVES,;

	public int toInt() {
		switch (this) {
			case MESH_LINES:
				return 0;
			case MESH_FULL:
				return 1;
			case PRIMITIVES:
				return 2;
		}
		return -1;
	}
	
	public PLYVisMode intToPLYVismode(int i) {
		return null;
	}

	public static PLYVisMode valueOf(IntegerProperty i) {
		switch (i.getValue()) {
			case 0:
				return MESH_LINES;
			case 1:
				return MESH_FULL;
			case 2:
				return PRIMITIVES;
		}
		return null;
	}
}
