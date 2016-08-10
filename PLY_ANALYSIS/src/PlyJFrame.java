import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

import javax.swing.JComponent;
import javax.swing.JFrame;

import org.apache.xmlbeans.impl.schema.SchemaStringEnumEntryImpl;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javafx.scene.Scene;

public class PlyJFrame extends JFrame {

	public PlyJFrame() {
//		this.add(co);
	}
	
	public void create(Stage stage, HBox hbox) {
		this.pack();
		this.setTitle("Ply Visualisation");
		this.setResizable(true);
		this.setDefaultCloseOperation( this.EXIT_ON_CLOSE );
		this.setLocationRelativeTo( null );
		
		MenuBar menuBar = new MenuBar();
		 
        // --- Menu File
        Menu menuFile = new Menu("File");
 
        // --- Menu Edit
        Menu menuEdit = new Menu("Edit");
 
        // --- Menu View
        Menu menuView = new Menu("View");
 
//        menuBar.getMenus().addAll(menuFile, menuEdit, menuView);
// 
        Scene s = new Scene(hbox, 400, 350);
        //((HBox) s.getRoot()).getChildren().addAll(menuBar);
 
        stage.setScene(s);
        stage.show();
	}
}
