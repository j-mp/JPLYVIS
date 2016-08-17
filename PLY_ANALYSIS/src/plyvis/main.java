package plyvis;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class main extends Application{
	
	public static void main(String[] args) throws Exception {
		Application.launch(args);
	}

	private SubScene visGroup;
	Group g;
	Stage primaryStage;
	BorderPane bp;
	PLYSettings settings;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setScene(createGuiScene(primaryStage));

		primaryStage.setTitle("PLY VIS");
		primaryStage.show();
	}

	public Scene createGuiScene(Stage stage) {
		
		new PLYSettings();
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
