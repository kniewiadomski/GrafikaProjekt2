

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends JPanel {

    private BufferedImage canvas;
    private Graphics2D graphics;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;
    private Mesh meshCube;
    private float[][] matProj;
    private float[][] matRotX, matRotZ, matRotY;
    private float fTheta = 5.0f;
    private Vector3d camera;



    public Dimension getPreferredSize() {
        return new Dimension(canvas.getWidth(), canvas.getHeight());
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(canvas, null, null);
    }

    public Vector3d multiplyMatrixVector(Vector3d v, float[][] f) {
        Vector3d w = new Vector3d();
        w.x = (v.x * f[0][0]) + (v.y * f[1][0]) + (v.z * f[2][0]) + f[3][0];
        w.y = (v.x * f[0][1]) + (v.y * f[1][1]) + (v.z * f[2][1]) + f[3][1];
        w.z = (v.x * f[0][2]) + (v.y * f[1][2]) + (v.z * f[2][2]) + f[3][2];
        float additional = (v.x * f[0][3]) + (v.y * f[1][3]) + (v.z * f[2][3]) + f[3][3];

        if(additional!=0.0f){
            w.x /= additional;
            w.y /= additional;
            w.z /= additional;
        }
        return w;
    }

    private void drawTriangle(float x1, float y1, float x2, float y2, float x3, float y3) {
        //System.out.println("\nx1 = " + x1 +"\ny1 = " + y1 +"\nx2 = " + x2 +"\ny2 = " + y2 +"\nx3 = " + x3 +"\ny3 = " + x3);
        int ix1 = Math.round(x1), ix2 = Math.round(x2), ix3 = Math.round(x3),
                iy1 = Math.round(y1), iy2 = Math.round(y2), iy3 = Math.round(y3);
        graphics.drawLine(ix1, iy1, ix2, iy2);
        graphics.drawLine(ix2, iy2, ix3, iy3);
        graphics.drawLine(ix1, iy1, ix3, iy3);

    }

    private void fillTriangle(float x1, float y1, float x2, float y2, float x3, float y3, float x, float y){

        float A =
    }



    private void clearBackgound() {
        graphics.setBackground(Color.BLACK);
        graphics.clearRect(0, 0, WIDTH, HEIGHT);

        //draw Triangles
        for(Triangle tri:meshCube.tris ){

            Triangle triProjected = new Triangle();
            Triangle triTranslated;
            Triangle triRotatedZ = new Triangle();
            Triangle triRotatedZX = new Triangle();

            triRotatedZ.p[0] = multiplyMatrixVector(tri.p[0], matRotZ);
            triRotatedZ.p[1] = multiplyMatrixVector(tri.p[1], matRotZ);
            triRotatedZ.p[2] = multiplyMatrixVector(tri.p[2], matRotZ);


            triRotatedZX.p[0] = multiplyMatrixVector(triRotatedZ.p[0], matRotX);
            triRotatedZX.p[1] = multiplyMatrixVector(triRotatedZ.p[1], matRotX);
            triRotatedZX.p[2] = multiplyMatrixVector(triRotatedZ.p[2], matRotX);

            triTranslated = triRotatedZX;
            triTranslated.p[0].z = triRotatedZX.p[0].z + 3.0f;
            triTranslated.p[1].z = triRotatedZX.p[1].z + 3.0f;
            triTranslated.p[2].z = triRotatedZX.p[2].z + 3.0f;

            Vector3d vec1 = new Vector3d(triTranslated.p[1].x - triTranslated.p[0].x,
                                         triTranslated.p[1].y - triTranslated.p[0].y,
                                         triTranslated.p[1].z - triTranslated.p[0].z);

            Vector3d vec2 = new Vector3d(triTranslated.p[2].x - triTranslated.p[0].x,
                                         triTranslated.p[2].y - triTranslated.p[0].y,
                                         triTranslated.p[2].z - triTranslated.p[0].z);

            Vector3d normal = new Vector3d(vec1.y*vec2.z - vec1.z*vec2.y ,
                                           vec1.z*vec2.x - vec1.x*vec2.z ,
                                           vec1.x*vec2.y - vec1.y*vec2.x);

            float lenNormal = (float) Math.sqrt(normal.x*normal.x + normal.y*normal.y+normal.z*normal.z);

            normal.x /= lenNormal;
            normal.y /= lenNormal;
            normal.z /= lenNormal;

            Vector3d sight = new Vector3d(triTranslated.p[0].x - camera.x,
                                          triTranslated.p[0].y - camera.y,
                                          triTranslated.p[0].z - camera.z);

            float isVisible = sight.x*normal.x + sight.y*normal.y + sight.z*normal.z;
            if(isVisible <0) {
                triProjected.p[0] = multiplyMatrixVector(triTranslated.p[0], matProj);
                triProjected.p[1] = multiplyMatrixVector(triTranslated.p[1], matProj);
                triProjected.p[2] = multiplyMatrixVector(triTranslated.p[2], matProj);

                triProjected.p[0].x += 1.0f;
                triProjected.p[0].y += 1.0f;
                triProjected.p[1].x += 1.0f;
                triProjected.p[1].y += 1.0f;
                triProjected.p[2].x += 1.0f;
                triProjected.p[2].y += 1.0f;

                triProjected.p[0].x *= 0.5f * (float) WIDTH;
                triProjected.p[0].y *= 0.5f * (float) HEIGHT;
                triProjected.p[1].x *= 0.5f * (float) WIDTH;
                triProjected.p[1].y *= 0.5f * (float) HEIGHT;
                triProjected.p[2].x *= 0.5f * (float) WIDTH;
                triProjected.p[2].y *= 0.5f * (float) HEIGHT;

                graphics.setColor(Color.WHITE);
                drawTriangle(triProjected.p[0].x, triProjected.p[0].y,
                        triProjected.p[1].x, triProjected.p[1].y,
                        triProjected.p[2].x, triProjected.p[2].y);
            }
        }
        repaint();
    }




    public static void main(String[] args) { new Main(); }

    public Main() {
        meshCube = new Mesh();
        camera = new Vector3d(0.0f,0.0f,0.0f);
        //SOUTH
        float[][] arr = new float[][]{{0.0f, 0.0f, 0.0f}, {0.0f, 1.0f, 0.0f}, {1.0f, 1.0f, 0.0f}}; meshCube.addTriangle(arr);
        arr = new float[][]{ {0.0f, 0.0f, 0.0f}, {1.0f, 1.0f, 0.0f}, {1.0f, 0.0f, 0.0f} }; meshCube.addTriangle(arr);

        //EAST
        arr = new float[][]{ {1.0f, 0.0f, 0.0f}, {1.0f, 1.0f, 0.0f}, {1.0f, 1.0f, 1.0f} }; meshCube.addTriangle(arr);
        arr = new float[][]{ {1.0f, 0.0f, 0.0f}, {1.0f, 1.0f, 1.0f}, {1.0f, 0.0f, 1.0f} }; meshCube.addTriangle(arr);

        //NORTH
        arr = new float[][]{ {1.0f, 0.0f, 1.0f}, {1.0f, 1.0f, 1.0f}, {0.0f, 1.0f, 1.0f}}; meshCube.addTriangle(arr);
        arr = new float[][]{{1.0f, 0.0f, 1.0f}, {0.0f, 1.0f, 1.0f}, {0.0f, 0.0f, 1.0f} }; meshCube.addTriangle(arr);

        //WEST
        arr = new float[][]{{0.0f, 0.0f, 1.0f}, {0.0f, 1.0f, 1.0f}, {0.0f, 1.0f, 0.0f}}; meshCube.addTriangle(arr);
        arr = new float[][]{{ 0.0f, 0.0f, 1.0f}, {0.0f, 1.0f, 0.0f}, {0.0f, 0.0f, 0.0f }}; meshCube.addTriangle(arr);

        //TOP
        arr = new float[][]{ {0.0f, 1.0f, 0.0f}, {0.0f, 1.0f, 1.0f}, {1.0f, 1.0f, 1.0f}}; meshCube.addTriangle(arr);
        arr = new float[][]{ {0.0f, 1.0f, 0.0f}, {1.0f, 1.0f, 1.0f}, {1.0f, 1.0f, 0.0f}}; meshCube.addTriangle(arr);

        //BOTTOM
        arr = new float[][]{{1.0f, 0.0f, 1.0f}, {0.0f, 0.0f, 1.0f}, {0.0f, 0.0f, 0.0f}}; meshCube.addTriangle(arr);
        arr = new float[][]{{1.0f, 0.0f, 1.0f}, {0.0f, 0.0f, 0.0f}, {1.0f, 0.0f, 0.0f} }; meshCube.addTriangle(arr);

        float fNear = 0.1f;
        float fFar = 1000.0f;
        float fFov = (float) Math.toRadians(90.0f);
        float fAspectRatio = (float)HEIGHT/(float)WIDTH;
        float fFovRad = (float) (1.0f/Math.tan(fFov/2));

        matProj = new float[][]{{fAspectRatio*fFovRad, 0.0f, 0.0f ,0.0f},
                              {0.0f, fFovRad, 0.0f, 0.0f},
                              {0.0f, 0.0f, fFar/(fFar-fNear), 1.0f},
                              {0.0f, 0.0f, -fFar*fNear/(fFar - fNear), 0.0f}};
        

        canvas = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        graphics = canvas.createGraphics();
        graphics.setBackground(Color.BLACK);
        JFrame frame = new JFrame("Projekt1 - Rysowanie Lini");

        frame.add(this);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ListenForMouseClick lForMouse = new ListenForMouseClick();
        this.addMouseListener(lForMouse);

    }

    private class ListenForMouseClick  implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            fTheta += 5;
            float fi = (float) Math.toRadians(fTheta);
            matRotZ = new float[][]{{(float) Math.cos(fi), -(float) Math.sin(fi), 0.0f, 0.0f},
                    {(float) Math.sin(fi), (float) Math.cos(fi), 0.0f, 0.0f},
                    {0.0f, 0.0f, 1.0f, 0.0f},
                    {0.0f, 0.0f, 0.0f, 1.0f}};

            matRotY = new float[][]{{(float) Math.cos(fi), 0.0f, (float) Math.sin(fi), 0.0f},
                    {0.0f, 1.0f, 0.0f, 0.0f},
                    {-(float) Math.sin(fi), 0.0f, (float) Math.cos(fi), 0.0f},
                    {0.0f, 0.0f, 0.0f, 1.0f}};

            matRotX = new float[][]{{1.0f, 0.0f, 0.0f, 0.0f},
                    {0.0f, (float) Math.cos((fi * 0.5f)), (float) Math.sin((fi * 0.5f)), 0.0f},
                    {0.0f, -(float) Math.sin((fi * 0.5f)), (float) Math.cos((fi * 0.5f)), 0.0f},
                    {0.0f, 0.0f, 0.0f, 1.0f}};

            clearBackgound();

        }

        @Override
        public void mousePressed(MouseEvent e) {
        }


        @Override
        public void mouseReleased(MouseEvent e) { }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) { }
    }




}