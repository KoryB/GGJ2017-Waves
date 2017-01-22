package com.korybyrne.ggj2017;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;

import java.util.ArrayList;

public class PhysHorizWall extends PhysWall {

    public PhysHorizWall(float width, Vector2 bottomleft) {
        this(Math.round(width / Wall.SIZE), bottomleft);
    }

    public PhysHorizWall(int segments, Vector2 bottomleft) {
        super(segments);

        mWidth = segments*Wall.SIZE;
        mHeight = Wall.THICKNESS;
        mPhysWidth = mWidth / Box2DManager.WORLD_SCALE;
        mPhysHeight = mHeight / Box2DManager.WORLD_SCALE;

        mWalls = new ArrayList<Wall>(segments);
        mVertHoriz = new float[] {0.0f, 1.0f};

        for (int i = 0; i < segments; i++) {
            float x = bottomleft.x + Wall.HALF_SIZE +i*Wall.SIZE;
            float y = bottomleft.y + Wall.HALF_THICKNESS;

            mWalls.add(new HorizWall(new Vector3(
                    x, y, 0
            )));
        }

        setupBody(bottomleft);
    }
}
