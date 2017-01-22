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
            MESH = new Mesh(true, 42, 0, VertexAttribute.Position());
            MESH.setVertices(new float[] {
                    -3.0f, -25.00f, 0f, 3.0f, -25.00f, 0f,
                    -0.54f, -22.50f, 0f, 5.46f, -22.50f, 0f,
                    1.68f, -20.00f, 0f, 7.68f, -20.00f, 0f,
                    3.44f, -17.50f, 0f, 9.44f, -17.50f, 0f,
                    4.57f, -15.00f, 0f, 10.57f, -15.00f, 0f,
                    4.96f, -12.50f, 0f, 10.96f, -12.50f, 0f,
                    4.57f, -10.00f, 0f, 10.57f, -10.00f, 0f,
                    3.44f, -7.50f, 0f, 9.44f, -7.50f, 0f,
                    1.68f, -5.00f, 0f, 7.68f, -5.00f, 0f,
                    -0.54f, -2.50f, 0f, 5.46f, -2.50f, 0f,
                    -3.0f, 0.00f, 0f, 3.0f, 0.00f, 0f,
                    -5.46f, 2.50f, 0f, 0.54f, 2.50f, 0f,
                    -7.68f, 5.00f, 0f, -1.68f, 5.00f, 0f,
                    -9.44f, 7.50f, 0f, -3.44f, 7.50f, 0f,
                    -10.57f, 10.00f, 0f, -4.57f, 10.00f, 0f,
                    -10.96f, 12.50f, 0f, -4.96f, 12.50f, 0f,
                    -10.57f, 15.00f, 0f, -4.57f, 15.00f, 0f,
                    -9.44f, 17.50f, 0f, -3.44f, 17.50f, 0f,
                    -7.68f, 20.00f, 0f, -1.68f, 20.00f, 0f,
                    -5.46f, 22.50f, 0f, 0.54f, 22.50f, 0f,
                    -3.0f, 25.00f, 0f, 3.0f, 25.00f, 0f,
            });
        }
    }

    @Override
    protected Mesh getMESH() {
        return MESH;
    }
}
