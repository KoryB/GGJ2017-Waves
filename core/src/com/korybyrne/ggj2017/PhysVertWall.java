package com.korybyrne.ggj2017;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;

import java.util.ArrayList;

public class PhysVertWall extends PhysWall {

    public PhysVertWall(int segments, Vector2 bottomleft) {
        super(segments);

        mWidth = Wall.THICKNESS;
        mHeight = segments*Wall.SIZE;
        mPhysWidth = mWidth / Box2DManager.WORLD_SCALE;
        mPhysHeight = mHeight / Box2DManager.WORLD_SCALE;

        mWalls = new ArrayList<Wall>(segments);
        mVertHoriz = new float[] {1.0f, 0.0f};

        for (int i = 0; i < segments; i++) {
            float x = bottomleft.x + Wall.HALF_THICKNESS;
            float y = bottomleft.y + Wall.HALF_SIZE +i*Wall.SIZE;

            mWalls.add(new VertWall(new Vector3(
                    x, y, 0
            )));
        }

        setupBody(bottomleft);
    }
}