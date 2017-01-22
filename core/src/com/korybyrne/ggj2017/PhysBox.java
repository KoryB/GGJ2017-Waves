package com.korybyrne.ggj2017;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by kory on 1/22/17.
 */
public class PhysBox implements Phys{
    private static final int TOP = 0;
    private static final int RIGHT = 1;
    private static final int BOTTOM = 2;
    private static final int LEFT = 3;

    private PhysWall[] mWalls = new PhysWall[4];

    public PhysBox(Vector2 pos, Vector2 extents) {
        this(pos.x, pos.y, extents.x, extents.y);
    }

    public PhysBox(float x, float y, float w, float h) {
        mWalls[TOP] = new PhysHorizWall(w, new Vector2(x, y+h-Wall.THICKNESS));
        mWalls[RIGHT] = new PhysVertWall(h, new Vector2(x+w-Wall.THICKNESS, y));
        mWalls[BOTTOM] = new PhysHorizWall(w, new Vector2(x, y));
        mWalls[LEFT] = new PhysVertWall(h, new Vector2(x, y));
    }

    public void render(ShaderProgram shaderProgram) {
        for (PhysWall wall : mWalls) {
            wall.render(shaderProgram);
        }
    }
}
