import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.vecmath.Point3d;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.SubScene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jply.Element;
import jply.ElementReader;
import jply.ElementType;
import jply.PlyReader;
import jply.PlyReaderFile;
import jply.Property;

public class PLYVISMenuBar {

	
	private Stage stage;
	private SubScene visGroup;
	private BorderPane bp;
	private static PLYSettings settings;
	PlyVis vis = null;

	public PLYVISMenuBar(Stage stage, SubScene visGroup, BorderPane bp, PLYSettings settings) {
		this.stage = stage;
		this.visGroup = visGroup;
		this.bp = bp;
		this.settings = settings;
	}

	private void createKeyBindsforVis(SubScene visGroup, PLYSettings settings) {
		this.visGroup.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent ke) {
		
				if (ke.getCode() == KeyCode.S) {
					   if (settings.animode.get()) {
						   vis.camAni.stopAnimation();
						   settings.animode.set(false);
					   } else {
						   vis.camAni.startAnimation();
						   settings.animode.set(true);
					   }
				}
				
				if (ke.getCode() == KeyCode.G) {
					if (settings.showAxis.get()) {
						System.out.println("Pressed G - > dis");
						Node n = visGroup.lookup("VISPLY");
						System.out.println(n.getId());
						//get(0).setVisible(false);
						settings.showAxis.set(false);
					} else {
						System.out.println("Pressed G - > en");
						visGroup.getParent().getChildrenUnmodifiable().get(0).setVisible(true);
						settings.showAxis.set(true);
					}
				}
			}
		});
	}
	
	public Node getNode() {
		return createMenu();
	}
	
	private Node createMenu() {
		MenuBar menuBar = new MenuBar();
		 
       // --- Menu File
       Menu menuFile = new Menu("File");
       
       MenuItem add = new MenuItem("Import ..."
    		   //, new ImageView(new Image("menusample/new.png"))
               );
       add.setOnAction(new EventHandler<ActionEvent>() {
           public void handle(ActionEvent t) {
        	   FileChooser fileChooser = new FileChooser();
        	   configureFileChooser(fileChooser);
        	   File file = fileChooser.showOpenDialog(stage);
        	   if (file != null) {
        		   ArrayList<Point4f> pointlist;
        		   try {
        			   pointlist = loadData(file.getAbsolutePath());
        			   vis = new PlyVis(pointlist);
        			   System.out.println("Fin loading.");
        			   visGroup = vis.createSubScene(PLYVisMode.valueOf(settings.vismode));
        			   VBox vbox = new VBox();
        			   vbox.getChildren().add(visGroup);
        			   bp.setBottom(vbox);
        			   stage.setTitle("PLY Vis | " + file.getAbsolutePath());
        			   createKeyBindsforVis(visGroup, settings);
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
       
       MenuItem saveScreenShot = new MenuItem("Save Screenshot");
       saveScreenShot.setOnAction(new EventHandler<ActionEvent>() {
           public void handle(ActionEvent e) {
               FileChooser fileChooser = new FileChooser();
               fileChooser.setTitle("Save Image");

               File file = fileChooser.showSaveDialog(stage);
               if (file != null) {
                   //                       ImageIO.write(SwingFXUtils.fromFXImage(pic.getImage(),
//                           null), "png", file);
				   System.out.println("");
               }
           }
       }
    		   );

       menuEdit.getItems().addAll(saveScreenShot);
       
       // --- Menu View
       Menu menuView = new Menu("View");
       
       MenuItem showcoords = new MenuItem("Show Coords"
//    		   , new ImageView(new Image("menusample/new.png"))
               );
       showcoords.setOnAction(new EventHandler<ActionEvent>() {
		   public void handle(ActionEvent t) {
				if (settings.showAxis.get()) {
					Node n = visGroup.getRoot().lookup("#PLYVIS");
					n.lookup("AXIS").setVisible(false);
					settings.showAxis.set(false);
				} else {
					Node n = visGroup.getRoot().lookup("#PLYVIS");
					n.lookup("AXIS").setVisible(true);
					settings.showAxis.set(true);
				}
		   }
       }); 
       
       MenuItem animate = new MenuItem("Start/Stop animation"
//    		   , new ImageView(new Image("menusample/new.png"))
               );
       animate.setOnAction(new EventHandler<ActionEvent>() {
		   public void handle(ActionEvent t) {
			   Boolean a = settings.animode.getValue();
			   if (a) {
				   vis.camAni.stopAnimation();
				   settings.animode.set(false);
			   } else {
				   vis.camAni.startAnimation();
				   settings.animode.set(true);
			   }
		   }
       }); 

       menuView.getItems().addAll(showcoords, animate);

       menuBar.getMenus().addAll(menuFile, menuEdit, menuView);
       return menuBar;
	}
	
    private static void configureFileChooser(final FileChooser fileChooser){                           
	    fileChooser.setTitle("Open .ply file");
	    fileChooser.setInitialDirectory(
	        new File(settings.defaultFilePath.get())
	    );
	    
        fileChooser.getExtensionFilters().addAll(
        		new FileChooser.ExtensionFilter("PLY", "*.ply"),
        		new FileChooser.ExtensionFilter("All files", "*.*")
        		);
    }
    

	private ArrayList<Point4f> loadData(String filepath) throws IOException {
		PlyReader ply = new PlyReaderFile(filepath);
		
		List<String> header = ply.getRawHeaders();
		
		for (String s : header) {
			System.out.println(s);
		}
		
		List<ElementType> etypes = ply.getElementTypes();
		
		for (ElementType et : etypes) {
			System.out.println(et.getName());
		}
		
		ArrayList<Point4f> points = readElements(ply);
		
		return points;
	}
	
	/**
	 * Method for reading phenospex .ply data.
	 * @param ply
	 * @return
	 * @throws IOException
	 */
	private static ArrayList<Point4f> readElements(PlyReader ply) throws IOException {
		
		ArrayList<Point4f> points = new ArrayList<>();
		
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
				if (p.getName().equals("intensity"))
					intensity =  ee.getDouble(p.getName());
				
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
			
			points.add(new Point4f((float) x, (float) y, (float) z, (float) intensity));

		}
		
		System.out.println("Inp-bounds: " + min_x + ":" + max_x + " | "  + min_y + ":" + max_y + " | "  + min_z + ":" + max_z);
		
		reader.close();

		return points;
	}
}
