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

	/**
	 * Method for reading phenospex .ply data.
	 * @param ply
	 * @return
	 * @throws IOException
	 */
	private static ArrayList<Point3d> readElements(PlyReader ply) throws IOException {
		
		ArrayList<Point3d> points = new ArrayList<>();
		
		ElementReader reader = ply.nextElementReader();
		
		// System.out.println(reader.getCount());
		
		ElementType type = reader.getElementType();
		// System.out.println(type);
		
		List<Property> props = type.getProperties();
		
		double min_x = Double.MAX_VALUE, min_y = Double.MAX_VALUE, min_z = Double.MAX_VALUE, max_x = Double.MIN_VALUE, max_y = Double.MIN_VALUE, max_z = Double.MIN_VALUE;
		
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
//				if (p.getName().equals("intensity"))
//					intensity =  ee.getDouble(p.getName());
				
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
			
			points.add(new Point3d(x, y, z));

		}
		
		System.out.println("Inp-bounds: " + min_x + ":" + max_x + " | "  + min_y + ":" + max_y + " | "  + min_z + ":" + max_z);
		
		reader.close();

		return points;
	}

	private SubScene visGroup;
	Group g;
	Stage primaryStage;
	BorderPane bp;
	
	private double mousePosX, mousePosY;
	private double mouseOldX, mouseOldY;
	final static Rotate rotateX = new Rotate(-20, Rotate.X_AXIS);
	final static Rotate rotateY = new Rotate(-20, Rotate.Y_AXIS);
	
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
		
		ArrayList<Point3d> pointlist = loadData("/home/pape/Schreibtisch/test.ply");
		   PlyVis vis = new PlyVis(pointlist);
		   System.out.println("Fin loading.");
		   visGroup = vis.createSubScene();
//		   VBox vbox = new VBox();
//		   vbox.getChildren().add(visGroup);

//		   bp.setBottom(visGroup);
//		   primaryStage.setScene(vis.createScene());
		   


//		   SubScene subScene = vis.createSubScene();
		   
		    // 2D
		    BorderPane pane = new BorderPane();
		    pane.setCenter(visGroup);
		    Button button = new Button("Reset");
		    button.setOnAction(e->{
		        rotateX.setAngle(-20);
		        rotateY.setAngle(-20);
		    });
//		    CheckBox checkBox = new CheckBox("Line");
//		    checkBox.setOnAction(e->{
//		        box.setDrawMode(checkBox.isSelected()?DrawMode.LINE:DrawMode.FILL);
//		    });
		    ToolBar toolBar = new ToolBar(button);
		    toolBar.setOrientation(Orientation.VERTICAL);
		    pane.setRight(toolBar);
		    pane.setPrefSize(300,300);

		    Scene scene = new Scene(pane);

		    scene.setOnMousePressed((MouseEvent me) -> {
		        mouseOldX = me.getSceneX();
		        mouseOldY = me.getSceneY();
		    });
		    scene.setOnMouseDragged((MouseEvent me) -> {
		        mousePosX = me.getSceneX();
		        mousePosY = me.getSceneY();
		        rotateX.setAngle(rotateX.getAngle()-(mousePosY - mouseOldY));
		        rotateY.setAngle(rotateY.getAngle()+(mousePosX - mouseOldX));
		        mouseOldX = mousePosX;
		        mouseOldY = mousePosY;
		    });
		   
		    primaryStage.setScene(scene);
		primaryStage.setTitle("PLY VIS");
		primaryStage.show();
	}

	private Scene createGuiScene(Stage stage) {
		HBox hbox = new HBox(4.0);
		Node m = createMenu(stage);
//		hbox.getChildren().addAll(m);
//		HBox.setHgrow(m, Priority.ALWAYS);
		
		bp = new BorderPane();
		bp.setTop(hbox);
		bp.setMinWidth(1024);
		
		g = new Group();
		g.getChildren().addAll(bp);
		Scene s = new Scene(g, 1024, 768);

		return s;
	}

	private Node createMenu(Stage stage) {
		MenuBar menuBar = new MenuBar();
		 
       // --- Menu File
       Menu menuFile = new Menu("File");
       
       MenuItem add = new MenuItem("Import ..."
    		   //, new ImageView(new Image("menusample/new.png"))
               );
       add.setOnAction(new EventHandler<ActionEvent>() {
           public void handle(ActionEvent t) {
        	   FileChooser fileChooser = new FileChooser();
        	   File file = fileChooser.showOpenDialog(stage);
        	   if (file != null) {
        		   ArrayList<Point3d> pointlist;
        		   PlyVis vis = null;
        		   try {
        			   pointlist = loadData(file.getAbsolutePath());
        			   vis = new PlyVis(pointlist);
        			   System.out.println("Fin loading.");
        			   visGroup = vis.createSubScene();
        			   VBox vbox = new VBox();
        			   vbox.getChildren().add(visGroup);
        			   bp.setBottom(vbox);
        			   stage.setTitle("PLY Vis | " + file.getAbsolutePath());
        		   } catch (IOException e) {
        			   // TODO Auto-generated catch block
        			   System.out.println("Could not load data.");
        			   e.printStackTrace();
        		   }
        	   }
           }
       });
           
       MenuItem exit = new MenuItem("Exit"
//        		   , new ImageView(new Image("menusample/new.png"))
                   );
       exit.setOnAction(new EventHandler<ActionEvent>() {
    	   public void handle(ActionEvent t) {
    		   System.out.println("exit");
    		   System.exit(0);
           }
       }); 
    
       menuFile.getItems().addAll(add, exit);

       // --- Menu Edit
       Menu menuEdit = new Menu("Edit");

       // --- Menu View
       Menu menuView = new Menu("View");

       menuBar.getMenus().addAll(menuFile, menuEdit, menuView);
       return menuBar;
	}

	private ArrayList<Point3d> loadData(String filepath) throws IOException {
		PlyReader ply = new PlyReaderFile(filepath);
		
		List<String> header = ply.getRawHeaders();
		
		for (String s : header) {
			System.out.println(s);
		}
		
		List<ElementType> etypes = ply.getElementTypes();
		
		for (ElementType et : etypes) {
			System.out.println(et.getName());
		}
		
		ArrayList<Point3d> points = readElements(ply);
		
		return points;
	}
	
	

//	public class PlyJFrame extends JFrame {
//
//		public PlyJFrame() {
////			this.add(co);
//		}
//		
//		public void create(Stage stage, HBox hbox) {
//			this.pack();
//			this.setTitle("Ply Visualisation");
//			this.setResizable(true);
//			this.setDefaultCloseOperation( this.EXIT_ON_CLOSE );
//			this.setLocationRelativeTo( null );
//			
//			MenuBar menuBar = new MenuBar();
//			 
//	        // --- Menu File
//	        Menu menuFile = new Menu("File");
//	 
//	        // --- Menu Edit
//	        Menu menuEdit = new Menu("Edit");
//	 
//	        // --- Menu View
//	        Menu menuView = new Menu("View");
//	 
////	        menuBar.getMenus().addAll(menuFile, menuEdit, menuView);
//	// 
//	        Scene s = new Scene(hbox, 400, 350);
//	        //((HBox) s.getRoot()).getChildren().addAll(menuBar);
//	 
//	        stage.setScene(s);
//	        stage.show();
//		}
//	}
}
