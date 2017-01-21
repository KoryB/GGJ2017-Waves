package com.korybyrne.ggj2017;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by kory on 1/21/17.
 */
public class HorizWall extends Wall {
    private static Mesh MESH;

    public HorizWall(Vector3 pos) {
        super(pos);
        if (MESH == null) {
            // (steps=30, hw=3, cycles=1, length=50)
            MESH = new Mesh(true, 62, 0, VertexAttribute.Position());
            MESH.setVertices(new float[]{
                    -25.00f, -3.0f, 0f, -25.00f, 3.0f, 0f,
                    -23.33f, -1.35f, 0f, -23.33f, 4.65f, 0f,
                    -21.67f, 0.24f, 0f, -21.67f, 6.24f, 0f,
                    -20.00f, 1.68f, 0f, -20.00f, 7.68f, 0f,
                    -18.33f, 2.91f, 0f, -18.33f, 8.91f, 0f,
                    -16.67f, 3.89f, 0f, -16.67f, 9.89f, 0f,
                    -15.00f, 4.57f, 0f, -15.00f, 10.57f, 0f,
                    -13.33f, 4.91f, 0f, -13.33f, 10.91f, 0f,
                    -11.67f, 4.91f, 0f, -11.67f, 10.91f, 0f,
                    -10.00f, 4.57f, 0f, -10.00f, 10.57f, 0f,
                    -8.33f, 3.89f, 0f, -8.33f, 9.89f, 0f,
                    -6.67f, 2.91f, 0f, -6.67f, 8.91f, 0f,
                    -5.00f, 1.68f, 0f, -5.00f, 7.68f, 0f,
                    -3.33f, 0.24f, 0f, -3.33f, 6.24f, 0f,
                    -1.67f, -1.35f, 0f, -1.67f, 4.65f, 0f,
                    0.00f, -3.0f, 0f, 0.00f, 3.0f, 0f,
                    1.67f, -4.65f, 0f, 1.67f, 1.35f, 0f,
                    3.33f, -6.24f, 0f, 3.33f, -0.24f, 0f,
                    5.00f, -7.68f, 0f, 5.00f, -1.68f, 0f,
                    6.67f, -8.91f, 0f, 6.67f, -2.91f, 0f,
                    8.33f, -9.89f, 0f, 8.33f, -3.89f, 0f,
                    10.00f, -10.57f, 0f, 10.00f, -4.57f, 0f,
                    11.67f, -10.91f, 0f, 11.67f, -4.91f, 0f,
                    13.33f, -10.91f, 0f, 13.33f, -4.91f, 0f,
                    15.00f, -10.57f, 0f, 15.00f, -4.57f, 0f,
                    16.67f, -9.89f, 0f, 16.67f, -3.89f, 0f,
                    18.33f, -8.91f, 0f, 18.33f, -2.91f, 0f,
                    20.00f, -7.68f, 0f, 20.00f, -1.68f, 0f,
                    21.67f, -6.24f, 0f, 21.67f, -0.24f, 0f,
                    23.33f, -4.65f, 0f, 23.33f, 1.35f, 0f,
                    25.00f, -3.0f, 0f, 25.00f, 3.0f, 0f,
            });
        }
    }

    @Override
    protected Mesh getMESH() {
        return MESH;
    }
}
