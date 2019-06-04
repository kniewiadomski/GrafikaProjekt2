public class Triangle {

    Vector3d[] p = new Vector3d[3];

    public Triangle() {
    }

    public Triangle(float[][] v) {
        p[0] = new Vector3d(v[0][0], v[0][1], v[0][2]);
        p[1] = new Vector3d(v[1][0], v[1][1], v[1][2]);
        p[2] = new Vector3d(v[2][0], v[2][1], v[2][2]);
    }
}
