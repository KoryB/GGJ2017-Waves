package com.korybyrne.ggj2017;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by kory on 1/21/17.
 */
public class VertWall extends Wall {
    private static Mesh MESH;

    public VertWall(Vector3 pos) {
        super(pos);
        if (MESH == null) {
            // (steps=30, hw=3, cycles=1, length=50)
            MESH = new Mesh(true, 62, 0, VertexAttribute.Position());
            MESH.setVertices(new float[] {
                    -3.0f, -25.00f, 0f, 3.0f, -25.00f, 0f,
                    -1.35f, -23.33f, 0f, 4.65f, -23.33f, 0f,
                    0.24f, -21.67f, 0f, 6.24f, -21.67f, 0f,
                    1.68f, -20.00f, 0f, 7.68f, -20.00f, 0f,
                    2.91f, -18.33f, 0f, 8.91f, -18.33f, 0f,
                    3.89f, -16.67f, 0f, 9.89f, -16.67f, 0f,
                    4.57f, -15.00f, 0f, 10.57f, -15.00f, 0f,
                    4.91f, -13.33f, 0f, 10.91f, -13.33f, 0f,
                    4.91f, -11.67f, 0f, 10.91f, -11.67f, 0f,
                    4.57f, -10.00f, 0f, 10.57f, -10.00f, 0f,
                    3.89f, -8.33f, 0f, 9.89f, -8.33f, 0f,
                    2.91f, -6.67f, 0f, 8.91f, -6.67f, 0f,
                    1.68f, -5.00f, 0f, 7.68f, -5.00f, 0f,
                    0.24f, -3.33f, 0f, 6.24f, -3.33f, 0f,
                    -1.35f, -1.67f, 0f, 4.65f, -1.67f, 0f,
                    -3.0f, 0.00f, 0f, 3.0f, 0.00f, 0f,
                    -4.65f, 1.67f, 0f, 1.35f, 1.67f, 0f,
                    -6.24f, 3.33f, 0f, -0.24f, 3.33f, 0f,
                    -7.68f, 5.00f, 0f, -1.68f, 5.00f, 0f,
                    -8.91f, 6.67f, 0f, -2.91f, 6.67f, 0f,
                    -9.89f, 8.33f, 0f, -3.89f, 8.33f, 0f,
                    -10.57f, 10.00f, 0f, -4.57f, 10.00f, 0f,
                    -10.91f, 11.67f, 0f, -4.91f, 11.67f, 0f,
                    -10.91f, 13.33f, 0f, -4.91f, 13.33f, 0f,
                    -10.57f, 15.00f, 0f, -4.57f, 15.00f, 0f,
                    -9.89f, 16.67f, 0f, -3.89f, 16.67f, 0f,
                    -8.91f, 18.33f, 0f, -2.91f, 18.33f, 0f,
                    -7.68f, 20.00f, 0f, -1.68f, 20.00f, 0f,
                    -6.24f, 21.67f, 0f, -0.24f, 21.67f, 0f,
                    -4.65f, 23.33f, 0f, 1.35f, 23.33f, 0f,
                    -3.0f, 25.00f, 0f, 3.0f, 25.00f, 0f,
            });
        }
    }

    @Override
    protected Mesh getMESH() {
        return MESH;
    }
}
