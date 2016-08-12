

import iap.blocks.debug.FxCameraAnnimation;
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

public class FxCameraInteractionPlyVis {
	
	private int camZdist;
	private final int numberOfBins;
	double anchorX, anchorY, anchorAngle;
	double oldX, oldY;
	boolean aniStop = false;
	private boolean showAxis = true;
	private Group group;
	
	public FxCameraInteractionPlyVis(SubScene scene, Group group, PerspectiveCamera cam, FxCameraAnnimation camAni, int camZdist, int numberOfBins) {
		this.camZdist = camZdist;
		this.numberOfBins = numberOfBins;
		addInteraction(scene, cam, camAni);
		this.group = group;
	}
	
	public void addInteraction(SubScene scene, final PerspectiveCamera cam, FxCameraAnnimation camAni) {
		scene.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				anchorX = event.getSceneX();
				anchorY = event.getSceneY();
				anchorAngle = cam.getRotate();
			}
		});
		
		scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// System.out.println("ang " + anchorAngle + "   |X " + anchorX + " | evX" + event.getSceneX() + " | ");
				double rot = 4;
				double delay = 0.0;
				
				if (delay < Math.abs(anchorX - event.getSceneX()))
					if (anchorX < event.getSceneX()) {
						cam.getTransforms().add(new Translate(0, 0, -camZdist));
						cam.getTransforms().add(new Rotate(rot, Rotate.Y_AXIS));
						cam.getTransforms().add(new Translate(0, 0, camZdist));
					} else {
						cam.getTransforms().add(new Translate(0, 0, -camZdist));
						cam.getTransforms().add(new Rotate(-rot, Rotate.Y_AXIS));
						cam.getTransforms().add(new Translate(0, 0, camZdist));
					}
				anchorX = event.getSceneX();
				
				if (delay < Math.abs(anchorY - event.getSceneY()))
					if (anchorY < event.getSceneY()) {
						cam.getTransforms().add(new Translate(0, 0, -camZdist));
						cam.getTransforms().add(new Rotate(-rot, Rotate.X_AXIS));
						cam.getTransforms().add(new Translate(0, 0, camZdist));
					} else {
						cam.getTransforms().add(new Translate(0, 0, -camZdist));
						cam.getTransforms().add(new Rotate(rot, Rotate.X_AXIS));
						cam.getTransforms().add(new Translate(0, 0, camZdist));
					}
				anchorY = event.getSceneY();
			}
		});
		
		scene.setOnScroll(new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				// System.out.println("x: " + event.getDeltaX() + " y: " + event.getDeltaY());
				// System.out.println(camZdist - Math.log((event.getDeltaY() * 0.5 * numberOfBins)));
				camZdist += (event.getDeltaY() * 0.5 * numberOfBins);
				Translate tz = new Translate(0.0, 0.0, cam.getTranslateZ() + event.getDeltaY() * 0.5 * numberOfBins);
				cam.getTransforms().add(tz);
			}
		});
		
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode() == KeyCode.UP) {
					// System.out.println("Key Pressed: " + ke.getCode() + camZdist);
					cam.getTransforms().add(new Translate(0, 0, -camZdist));
					cam.getTransforms().add(new Rotate(5, Rotate.X_AXIS));
					cam.getTransforms().add(new Translate(0, 0, camZdist));
				}
				
				if (ke.getCode() == KeyCode.DOWN) {
					// System.out.println("Key Pressed: " + ke.getCode() + camZdist);
					cam.getTransforms().add(new Translate(0, 0, -camZdist));
					cam.getTransforms().add(new Rotate(-5, Rotate.X_AXIS));
					cam.getTransforms().add(new Translate(0, 0, camZdist));
				}
				
				if (ke.getCode() == KeyCode.LEFT) {
					// System.out.println("Key Pressed: " + ke.getCode());
					cam.getTransforms().add(new Translate(0, 0, -camZdist));
					cam.getTransforms().add(new Rotate(5, Rotate.Y_AXIS));
					cam.getTransforms().add(new Translate(0, 0, camZdist));
				}
				
				if (ke.getCode() == KeyCode.RIGHT) {
					// System.out.println("Key Pressed: " + ke.getCode());
					cam.getTransforms().add(new Translate(0, 0, -camZdist));
					cam.getTransforms().add(new Rotate(-5, Rotate.Y_AXIS));
					cam.getTransforms().add(new Translate(0, 0, camZdist));
				}
				
				if (ke.getCode() == KeyCode.S) {
					if (!aniStop) {
						camAni.pauseAnimation();
						aniStop = true;
					} else {
						camAni.startAnimation();
						aniStop = false;
					}
				}
				
				if (ke.getCode() == KeyCode.G) {
					if (showAxis) {
						System.out.println("Pressed G - > dis");
						group.getChildren().get(0).setVisible(false);
						showAxis = false;
					} else {
						System.out.println("Pressed G - > en");
						group.getChildren().get(0).setVisible(true);
						showAxis = true;
					}
				}
			}
		});
	};
}