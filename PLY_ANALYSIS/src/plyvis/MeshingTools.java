package plyvis;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import vtk.vtkActor;
import vtk.vtkCleanPolyData;
import vtk.vtkDataObject;
import vtk.vtkDataSetMapper;
import vtk.vtkDelaunay3D;
import vtk.vtkPanel;
import vtk.vtkPoints;
import vtk.vtkPolyData;

public class MeshingTools extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private vtkPanel renWin;
    private vtkActor cutActor;
    private vtkActor isoActor;
	
    private JPanel buttons;
    private JToggleButton slicesButton;
    private JToggleButton isoButton;
    private JButton exitButton;
    static Container con;
    
	public MeshingTools(ArrayList<Point4f> data) {
		vtkDataObject vtkdata = getVTKData(data);
		vtkDataObject clean = cleanData(vtkdata);
		vtkDataSetMapper mesh = clacMesh(clean);
		con = visgogo(mesh);
	}

	private Container visgogo(vtkDataSetMapper mesh) {
		vtkActor surfActor = new vtkActor();
		surfActor.SetMapper(mesh);
		surfActor.GetProperty().EdgeVisibilityOn();
		surfActor.GetProperty().SetEdgeColor(0.2,0.2,0.2);
		surfActor.GetProperty().SetInterpolationToFlat();

		/**** 5) RENDER WINDOW ****/
			
		/* vtkPanel - this is the interface between Java and VTK */
		renWin = new vtkPanel();
			
		/* add the surface geometry plus the isosurface */
		renWin.GetRenderer().AddActor(surfActor);
			
		/* the default zoom is whacky, zoom out to see the whole domain */
	        renWin.GetRenderer().GetActiveCamera().Dolly(0.2); 
		renWin.GetRenderer().SetBackground(1, 1, 1);
			
		/**** 6) CREATE PANEL FOR BUTTONS ****/
		buttons  = new JPanel();
		buttons.setLayout(new GridLayout(1,0));
			
	        /* isosurface button, clicked by default */
		isoButton = new JToggleButton("Isosurfaces",true);
//	        isoButton.addActionListener(this);
			
		/* cutting planes button */
	        slicesButton = new JToggleButton("Slices");
//	        slicesButton.addActionListener(this);
			
		/* exit button */
		exitButton = new JButton("Exit");
//	        exitButton.addActionListener(this);
			
		/* add buttons to the panel */
		buttons.add(isoButton); 
		buttons.add(slicesButton);
		buttons.add(exitButton); 

		Container co = new Container();
		/**** 7) POPULATE MAIN PANEL ****/
	        co.add(renWin, BorderLayout.CENTER);
	        co.add(buttons, BorderLayout.SOUTH);
	        
			return co;	
	    }

	    /* ActionListener that responds to button clicks
	     * Toggling iso/slices buttons results in addition or removal
	     * of the corresponding actor */
	    public void actionPerformed(ActionEvent e) 
	    {
		/*cutting planes button, add or remove cutActor */
		if (e.getSource().equals(slicesButton))
		{
			if (slicesButton.isSelected())
				renWin.GetRenderer().AddActor(cutActor);
			else
				renWin.GetRenderer().RemoveActor(cutActor);
				
			renWin.Render();
		}
		
		/*exit button, end application */
		else if (e.getSource().equals(exitButton)) 
		{
	            System.exit(0);
	        }
	    }

	    /* main, creates a new JFrame and populates it with the DemoJavaVTK panel */
	    public static void main(String s[]) 
	    {
	        SwingUtilities.invokeLater(new Runnable() 
		{
	            @Override
	            public void run() 
		    {
	                JFrame frame = new JFrame("Java and VTK Demo");
	                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	                frame.getContentPane().setLayout(new BorderLayout());
	                frame.getContentPane().add(con, BorderLayout.CENTER);
	                frame.setSize(400, 400);
	                frame.setLocationRelativeTo(null);
	                frame.setVisible(true);
	            }
	        });
	    }

	private vtkDataObject getVTKData(ArrayList<Point4f> data) {
		vtkPolyData poly = new vtkPolyData();
		
		for (Point4f p : data) {
			vtkPoints vtkp = new vtkPoints();
			// vtkp.SetDataTypeToFloat();
			vtkp.InsertPoint(10, (double) p.x, (double) p.y, (double) p.z);
			poly.SetPoints(vtkp);
		}
		
		return poly;
	}

	private vtkDataObject cleanData(vtkDataObject vtkdata) {
		vtkCleanPolyData clean = new vtkCleanPolyData();
		
		return vtkdata;
	}

	private vtkDataSetMapper clacMesh(vtkDataObject clean) {
		vtkDelaunay3D delaunay = new vtkDelaunay3D();
		
		delaunay.SetAlpha(0.1);
		delaunay.AddInputData(clean);
		
		vtkDataSetMapper mapper = new vtkDataSetMapper();
		
		mapper.SetInputConnection(10, delaunay.GetOutputPort());
		 
		vtkActor delaunayAlphaActor = new vtkActor(); 

		delaunayAlphaActor.SetMapper(mapper);
		delaunayAlphaActor.GetProperty().SetColor(1,0,0);
		
		return mapper;
	}
}
