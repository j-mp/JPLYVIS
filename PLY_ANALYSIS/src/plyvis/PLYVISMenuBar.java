package plyvis;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import datastructures.Point4f;
import datastructures.PointCloudDataSet;
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
import javafx.scene.control.CheckMenuItem;
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
import tools.ProjectionOperations;
import vtk.vtkNativeLibrary;

public class PLYVISMenuBar {

	
	private Stage stage;
	private SubScene visGroup;
	private BorderPane bp;
	PlyVis vis = null;
	private HashMap<String, PointCloudDataSet> dataSets;
	private String actualKeyDataset = "";
	
	public PLYVISMenuBar(Stage stage, SubScene visGroup, BorderPane bp) {
		this.stage = stage;
		this.visGroup = visGroup;
		this.bp = bp;
		dataSets = new HashMap<String, PointCloudDataSet>();
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

        		   try {
        			   PointCloudDataSet dataset = new PointCloudDataSet(file.getAbsolutePath());
        			   ArrayList<Point4f> pointlist = dataset.getPointlist();
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
        			   dataSets.put(file.getName(), dataset);
        			   actualKeyDataset = file.getName();
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
       Menu menuEdit = new Menu("Tools");
       
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

       MenuItem createProjection = new MenuItem("Save Z-projection");
       createProjection.setOnAction(new EventHandler<ActionEvent>() {
           public void handle(ActionEvent e) {
               FileChooser fileChooser = new FileChooser(); 
               
               boolean floatExport = true;
               boolean intensityExport = true;
               
               configureSaveDialog(fileChooser);
               File file = fileChooser.showSaveDialog(stage);
               ExtensionFilter format = fileChooser.getSelectedExtensionFilter();
               
               if (file != null) {
            	   BufferedImage img = null;
            	   
            	   if (floatExport)
            		   if (intensityExport)
            			   img = ProjectionOperations.createZProjectionFloat(dataSets.get(actualKeyDataset), 256, true);
            		   else
            			   img = ProjectionOperations.createZProjectionFloat(dataSets.get(actualKeyDataset), 256, false);
            	   else
            		   img = ProjectionOperations.createZProjection(dataSets.get(actualKeyDataset), 256);

            	   if (!floatExport) {
	            	   try {
	            		   String fs = format.getDescription().toLowerCase();
	            		   ImageIO.write(img, fs, new File(file.getAbsoluteFile() + "." + fs));
	            	   } catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
	            	   }
            	   } else {
            		    String fs = format.getDescription().toLowerCase();
            		    // TODO include new lib?
            		    // JAI.create("filestore", img, file.getAbsoluteFile() + "." + fs, "TIFF");
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
       
//       createProjection.setAccelerator(new KeyCodeCombination(KeyCode.P));
       
       menuEdit.getItems().addAll(saveScreenShot, createProjection);
       
       // --- Menu View
       Menu menuView = new Menu("View");
       
       MenuItem showcoords = new MenuItem("Show Coords"
//    		   , new ImageView(new Image("menusample/new.png"))
               );
       showcoords.setOnAction(new EventHandler<ActionEvent>() {
		   public void handle(ActionEvent t) {
			   if(visGroup == null)
					return;
				
				checkAxes();
		   }
       }); 
       
       showcoords.setAccelerator(new KeyCodeCombination(KeyCode.G));
       
       MenuItem animate = new MenuItem("Stop animation"
//    		   , new ImageView(new Image("menusample/new.png"))
               );
       animate.setOnAction(new EventHandler<ActionEvent>() {
		   public void handle(ActionEvent t) {
			   Boolean a = PLYSettings.animode.getValue();
			   
			   if(vis == null)
				   return;
			   
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
       
       
       // --- Settings
       Menu menuSettings = new Menu("Settings");
       
       CheckMenuItem debug = new CheckMenuItem("Debug mode");
       debug.setOnAction(new EventHandler<ActionEvent>() {
		   public void handle(ActionEvent t) {
			   
			   if (PLYSettings.debug.get()) {
					PLYSettings.debug.set(false);
				} else {
					PLYSettings.debug.set(true);
				}
		   }
       }); 
       
       //demo.setAccelerator(new KeyCodeCombination(KeyCode.D));
       
       menuSettings.getItems().addAll(debug);
       
       
       // --- About
       Menu menuAbout = new Menu("About");
       
       MenuItem demo = new MenuItem("Run VTK Demo"
//    		   , new ImageView(new Image("menusample/new.png"))
               );
       demo.setOnAction(new EventHandler<ActionEvent>() {
		   public void handle(ActionEvent t) {
			   
			   /* Load VTK shared librarires (.dll) on startup, print message if not found */
			    {				
			        if (!vtkNativeLibrary.LoadAllNativeLibraries()) 
				{
				       for (vtkNativeLibrary lib : vtkNativeLibrary.values()) 
					{
			                	if (!lib.IsLoaded()) 
							System.out.println(lib.GetLibraryName() + " not loaded");    
					}
						
					System.out.println("Make sure the search path is correct: ");
					System.out.println(System.getProperty("java.library.path"));
			        }
			        vtkNativeLibrary.DisableOutputWindow(null);
			    }
			    
//				DemoJavaVTK vtkdemo = new DemoJavaVTK();
//				vtkdemo.main(new String[]{});
			   MeshingTools ttt = new MeshingTools(dataSets.get(actualKeyDataset).getPointlist());
			   ttt.main(new String[]{});
		   }
       }); 
       
       //demo.setAccelerator(new KeyCodeCombination(KeyCode.D));
       
       menuAbout.getItems().addAll(demo);
       
       // add to bar
       menuBar.getMenus().addAll(menuFile, menuEdit, menuView, menuSettings, menuAbout);
       
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
