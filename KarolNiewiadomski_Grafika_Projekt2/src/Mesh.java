
import java.util.Vector;

public class Mesh {

    Vector<Triangle> tris = new Vector<>();

    public void addTriangle(float[][] v)
    {
        tris.add(new Triangle(v));
    }
}
