import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3d;

import org.apache.poi.hssf.util.PaneInformation;
import org.w3c.tools.widgets.BorderPanel;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jply.Element;
import jply.ElementReader;
import jply.ElementType;
import jply.PlyReader;
import jply.PlyReaderFile;
import jply.Property;
import javafx.scene.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.scene.control.TextField;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.scene.SubScene;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.geometry.Orientation;

public class main extends Application{
	
	public static void main(String[] args) throws Exception {
		Application.launch(args);
	}

	private SubScene visGroup;
	Group g;
	Stage primaryStage;
	BorderPane bp;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// Load root layout from fxml file.
//		FXMLLoader loader = new FXMLLoader();
//		loader.setLocation(main.class.getResource("PlyVisGui.fxml"));
//		rootlayout = loader.load();
		
//		ArrayList<Point3d> pointlist = loadData("/home/pape/Schreibtisch/test.ply");
//		PlyVis vis = new PlyVis(pointlist);
//		
//		visGroup = vis.createSubScene();
//		
//		primaryStage.setScene(vis.createScene());
		
//		primaryStage.setScene(createGuiScene(primaryStage));
		
//		ArrayList<Point3d> pointlist = loadData("/home/pape/Schreibtisch/test.ply");
//		PlyVis vis = new PlyVis(pointlist);
//		System.out.println("Fin loading.");
//		   visGroup = vis.createSubScene();
//		   VBox vbox = new VBox();
//		   vbox.getChildren().add(visGroup);

//		   bp.setBottom(visGroup);
		primaryStage.setScene(createGuiScene(primaryStage));
		   
//		primaryStage.setScene(scene);
		primaryStage.setTitle("PLY VIS");
		primaryStage.show();
	}

	private Scene createGuiScene(Stage stage) {
		
		HBox hbox = new HBox(4.0);
		bp = new BorderPane();
		bp.setTop(hbox);
		bp.setMinWidth(1024);
		

		Node m = new PLYVISMenuBar(stage, visGroup, bp).getNode();
		hbox.getChildren().addAll(m);
		HBox.setHgrow(m, Priority.ALWAYS);
		
		g = new Group();
		g.getChildren().addAll(bp);
		Scene s = new Scene(g, 1024, 768);

		return s;
	}
}
