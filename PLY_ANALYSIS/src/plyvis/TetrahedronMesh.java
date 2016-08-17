package plyvis;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.vecmath.Point3d;

import javafx.scene.shape.TriangleMesh;

public class TetrahedronMesh extends TriangleMesh {
    private ArrayList<Point4f> vertices;
    private int[] facesLink;

    public TetrahedronMesh(double length, ArrayList<Point4f> v) {
        this.vertices = (ArrayList<Point4f>) deepClone(v);
        if (length > 0.0) {
            float[] points = new float[vertices.size() * 12];
            int[] faces = new int[vertices.size() * 6]; // 24
            facesLink = new int[vertices.size() * 4];
            float[] texCoords = new float[vertices.size() * 12];
            int vertexCounter = 0;
            int primitiveCounter = 0;
            int pointCounter = 0;
            int facesCounter = 0;
            int texCounter = 0;
            for (Point4f vertex : vertices) {
                vertex.scale(100);
                points[primitiveCounter] = (float) vertex.x;
                points[primitiveCounter + 1] = (float) (vertex.y - (length
                        * Math.sqrt(3.0) / 3.0));
                points[primitiveCounter + 2] = (float) -vertex.z;
                points[primitiveCounter + 3] = (float) (vertex.x + (length / 2.0));
                points[primitiveCounter + 4] = (float) (vertex.y + (length
                        * Math.sqrt(3.0) / 6.0));
                points[primitiveCounter + 5] = (float) -vertex.z;
                points[primitiveCounter + 6] = (float) (vertex.x - (length / 2.0));
                points[primitiveCounter + 7] = (float) (vertex.y + (length
                        * Math.sqrt(3.0) / 6.0));
                points[primitiveCounter + 8] = (float) -vertex.z;
                points[primitiveCounter + 9] = (float) vertex.x;
                points[primitiveCounter + 10] = (float) vertex.y;
                points[primitiveCounter + 11] = (float) -(vertex.z - (length * Math
                        .sqrt(2.0 / 3.0)));
                faces[facesCounter] = pointCounter + 0;
                faces[facesCounter + 1] = pointCounter + 0;
                faces[facesCounter + 2] = pointCounter + 1;
                faces[facesCounter + 3] = pointCounter + 1;
                faces[facesCounter + 4] = pointCounter + 2;
                faces[facesCounter + 5] = pointCounter + 2;
//                faces[facesCounter + 6] = pointCounter + 1;
//                faces[facesCounter + 7] = pointCounter + 1;
//                faces[facesCounter + 8] = pointCounter + 0;
//                faces[facesCounter + 9] = pointCounter + 0;
//                faces[facesCounter + 10] = pointCounter + 3;
//                faces[facesCounter + 11] = pointCounter + 3;
//                faces[facesCounter + 12] = pointCounter + 2;
//                faces[facesCounter + 13] = pointCounter + 2;
//                faces[facesCounter + 14] = pointCounter + 1;
//                faces[facesCounter + 15] = pointCounter + 1;
//                faces[facesCounter + 16] = pointCounter + 3;
//                faces[facesCounter + 17] = pointCounter + 4;
//                faces[facesCounter + 18] = pointCounter + 0;
//                faces[facesCounter + 19] = pointCounter + 0;
//                faces[facesCounter + 20] = pointCounter + 2;
//                faces[facesCounter + 21] = pointCounter + 2;
//                faces[facesCounter + 22] = pointCounter + 3;
//                faces[facesCounter + 23] = pointCounter + 5;
                facesLink[pointCounter] = vertexCounter;
                facesLink[pointCounter + 1] = vertexCounter;
                facesLink[pointCounter + 2] = vertexCounter;
                facesLink[pointCounter + 3] = vertexCounter;
                texCoords[texCounter] = 0.5f;
                texCoords[texCounter + 1] = 1.0f;
                texCoords[texCounter + 2] = 0.75f;
                texCoords[texCounter + 3] = (float) (1.0 - Math.sqrt(3.0) / 4.0);
                texCoords[texCounter + 4] = 0.25f;
                texCoords[texCounter + 5] = (float) (1.0 - Math.sqrt(3.0) / 4.0);
                texCoords[texCounter + 6] = 1.0f;
                texCoords[texCounter + 7] = 1.0f;
                texCoords[texCounter + 8] = 0.5f;
                texCoords[texCounter + 9] = (float) (1.0 - Math.sqrt(3.0) / 2.0);
                texCoords[texCounter + 10] = 0.0f;
                texCoords[texCounter + 11] = 1.0f;
                vertexCounter++;
                primitiveCounter += 12;
                pointCounter += 4;
                facesCounter += 6; // 24
                texCounter += 12;
            }
            getPoints().setAll(points);
            getFaces().setAll(faces);
            getTexCoords().setAll(texCoords);
            // getFaceSmoothingGroups().setAll(0, 1, 2, 3);
        }
    }

    public Point4f getPointFromFace(int faceId) {
        return vertices.get(facesLink[faceId]);
    }
    
    /**
     * This method makes a "deep clone" of any object it is given.
     */
    public static Object deepClone(Object object) {
      try {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(object);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        return ois.readObject();
      }
      catch (Exception e) {
        e.printStackTrace();
        return null;
      }
    }
}