package plyvis;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.SubScene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
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
	PlyVis vis = null;

	public PLYVISMenuBar(Stage stage, SubScene visGroup, BorderPane bp) {
		this.stage = stage;
		this.visGroup = visGroup;
		this.bp = bp;
	}
	
	public Node getNode() {
		return createMenu();
	}
	
	private Node createMenu() {
		MenuBar menuBar = new MenuBar();
		 
       // --- Menu File
       Menu menuFile = new Menu("File");
       
       MenuItem imp = new MenuItem("Import ..."
    		   //, new ImageView(new Image("menusample/new.png"))
               );
       imp.setOnAction(new EventHandler<ActionEvent>() {
           public void handle(ActionEvent t) {
        	   FileChooser fileChooser = new FileChooser();
        	   configurePLYFileChooser(fileChooser);
        	   File file = fileChooser.showOpenDialog(stage);
        	   if (file != null) {
        		   ArrayList<Point4f> pointlist;
        		   try {
        			   pointlist = loadData(file.getAbsolutePath());
        			   // System.out.println(PLYSettings.vismode.toString());
        			   vis = new PlyVis(pointlist, (int) 1024, (int) 768);
        			   // System.out.println("Fin loading.");
        			   visGroup = vis.createSubScene(PLYVisMode.valueOf(PLYSettings.vismode));
        			   visGroup.setId("visGroup");
        			   VBox vbox = new VBox();
        			   vbox.setId("visGroupVbox");
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
       
       imp.setAccelerator(new KeyCodeCombination(KeyCode.I));
           
       MenuItem exit = new MenuItem("Exit"
//        		   , new ImageView(new Image("menusample/new.png"))
                   );
       exit.setOnAction(new EventHandler<ActionEvent>() {
    	   public void handle(ActionEvent t) {
    		   // System.out.println("exit");
    		   System.exit(0);
           }
       });
       
       exit.setAccelerator(new KeyCodeCombination(KeyCode.E));
    
       menuFile.getItems().addAll(imp, exit);

       // --- Menu Edit
       Menu menuEdit = new Menu("Edit");
       
       MenuItem saveScreenShot = new MenuItem("Save Screenshot");
       saveScreenShot.setOnAction(new EventHandler<ActionEvent>() {
           public void handle(ActionEvent e) {
               FileChooser fileChooser = new FileChooser();
               configureSaveDialog(fileChooser);
               File file = fileChooser.showSaveDialog(stage);
               ExtensionFilter format = fileChooser.getSelectedExtensionFilter();
               
               if (file != null) {
            	   WritableImage img = new WritableImage((int) visGroup.getWidth(), (int) visGroup.getHeight());
            	   visGroup.snapshot(new SnapshotParameters(), img);
            	   try {
            		   String fs = format.getDescription().toLowerCase();
            		   ImageIO.write(SwingFXUtils.fromFXImage(img, null), fs, new File(file.getAbsoluteFile() + "." + fs));
            	   } catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
            	   }
               }
           }

		private void configureSaveDialog(FileChooser fileChooser) {
            fileChooser.setTitle("Save Image");
		    fileChooser.setInitialDirectory(
		        new File(PLYSettings.defaultFilePath.get())
		    );
		    
		    String[] l = ImageIO.getReaderFormatNames();
		    for (int i = 0; i < l.length; i+=2)
		    	fileChooser.getExtensionFilters().add(
	        		new FileChooser.ExtensionFilter(l[i].toLowerCase(), "*." + l[i].toLowerCase()));
		    
		    fileChooser.getExtensionFilters().add(
	        		new FileChooser.ExtensionFilter("All files", "*.*")
	        		);
			
		}
       });
       
       saveScreenShot.setAccelerator(new KeyCodeCombination(KeyCode.P));

       menuEdit.getItems().addAll(saveScreenShot);
       
       // --- Menu View
       Menu menuView = new Menu("View");
       
       MenuItem showcoords = new MenuItem("Show Coords"
//    		   , new ImageView(new Image("menusample/new.png"))
               );
       showcoords.setOnAction(new EventHandler<ActionEvent>() {
		   public void handle(ActionEvent t) {
				checkAxes();
		   }
       }); 
       
       showcoords.setAccelerator(new KeyCodeCombination(KeyCode.G));
       
       MenuItem animate = new MenuItem("Start/Pause animation"
//    		   , new ImageView(new Image("menusample/new.png"))
               );
       animate.setOnAction(new EventHandler<ActionEvent>() {
		   public void handle(ActionEvent t) {
			   Boolean a = PLYSettings.animode.getValue();
			   if (a) {
				   vis.camAni.pauseAnimation();
				   PLYSettings.animode.set(false);
			   } else {
				   vis.camAni.startAnimation();
				   PLYSettings.animode.set(true);
			   }
		   }
       });
       
       animate.setAccelerator(new KeyCodeCombination(KeyCode.A));
       
       Menu subMenu_VisMode = new Menu("Vismode");
       final ToggleGroup groupVismode = new ToggleGroup();
       for (PLYVisMode mode : PLYVisMode.values()) {
    	   RadioMenuItem itemVismode = new RadioMenuItem(mode.name());
    	   itemVismode.setId(mode.name());
    	   
    	   if (PLYSettings.vismode.getValue().compareTo(mode.toInt()) == 0)
    		   itemVismode.setSelected(true);
    	   
    	   itemVismode.setToggleGroup(groupVismode);
           itemVismode.setOnAction(new EventHandler<ActionEvent>() {
		   
        	   public void handle(ActionEvent t) {
        		   // rm old
        		   Group root = (Group) visGroup.getRoot().lookup("#PLYVIS");
        		   root.lookup("#vis").setVisible(true);
        		   root.getChildren().remove(root.lookup("#vis"));
        		   
        		   Node n = new Group();
        		   switch (PLYVisMode.valueOf(itemVismode.getId())) {
        		   		case MESH_LINES:
        		   			PLYSettings.vismode.set(PLYVisMode.MESH_LINES.toInt());
	        				n = vis.createMeshView(true);
	        				break;
	        			case MESH_FULL:
	        				PLYSettings.vismode.set(PLYVisMode.MESH_FULL.toInt());
	        				n = vis.createMeshView(false);
	        				break;
	        			case PRIMITIVES:
	        				PLYSettings.vismode.set(PLYVisMode.PRIMITIVES.toInt());
	        				n = vis.createPrimitives();
	        				break;
	        		}

        		   Node nn = vis.movetoOrgin(n);
        		   nn.setId("vis");
        		   root.getChildren().add(n);
        	   }
           });
           subMenu_VisMode.getItems().add(itemVismode);
       }

       menuView.getItems().addAll(showcoords, animate, subMenu_VisMode);

       menuBar.getMenus().addAll(menuFile, menuEdit, menuView);
       return menuBar;
	}
	
    private void configurePLYFileChooser(final FileChooser fileChooser){                           
	    fileChooser.setTitle("Open .ply file");
	    fileChooser.setInitialDirectory(
	        new File(PLYSettings.defaultFilePath.get())
	    );
	    
        fileChooser.getExtensionFilters().addAll(
        		new FileChooser.ExtensionFilter("PLY", "*.ply"),
        		new FileChooser.ExtensionFilter("All files", "*.*")
        		);
    }
    

	private ArrayList<Point4f> loadData(String filepath) throws IOException {
		PlyReader ply = new PlyReaderFile(filepath);
		
		List<String> header = ply.getRawHeaders();
		
		if (PLYSettings.debug.getValue()) {
			for (String s : header) {
				System.out.println(s);
			}
			
			List<ElementType> etypes = ply.getElementTypes();
			
			for (ElementType et : etypes) {
				System.out.println(et.getName());
			}
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
			
//			if (z > 226f)
			points.add(new Point4f((float) x, (float) y, (float) z, (float) intensity));

		}
		
		if (PLYSettings.debug.getValue())
			System.out.println("Inp-bounds: " + min_x + ":" + max_x + " | "  + min_y + ":" + max_y + " | "  + min_z + ":" + max_z);
		
		reader.close();

		return points;
	}

	private void checkAxes() {
		if (PLYSettings.showAxis.get()) {
			Node n = visGroup.getRoot().lookup("#PLYVIS");
			n.lookup("#AXIS").setVisible(false);
			PLYSettings.showAxis.set(false);
		} else {
			Node n = visGroup.getRoot().lookup("#PLYVIS");
			n.lookup("#AXIS").setVisible(true);
			PLYSettings.showAxis.set(true);
		}
	}
}
