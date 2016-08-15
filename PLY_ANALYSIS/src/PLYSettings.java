
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

public class PLYSettings {

	BooleanProperty animode;
	StringProperty defaultFilePath;
	IntegerProperty vismode;
	BooleanProperty showAxis;
	
	public PLYSettings() {
		animode = new SimpleBooleanProperty(this, "RunAnnimation", true);
		defaultFilePath = new SimpleStringProperty(this, "Default file path", System.getProperty("user.home") + "/Desktop/");
		vismode = new SimpleIntegerProperty(this, "Vis mode", PLYVisMode.MESH_FULL.toInt());
		showAxis = new SimpleBooleanProperty(this, "Show axes", false);
	}
}
