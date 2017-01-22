package com.korybyrne.ggj2017;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

import java.util.List;

/**
 * Created by kory on 1/21/17.
 */
public abstract class PhysWall implements Phys {
    protected List<Wall> mWalls;
    protected int mSegments;

    protected Body mBody;

    protected float mWidth, mHeight;
    protected float mPhysWidth, mPhysHeight;

    protected float[] mVertHoriz;

    public PhysWall(int segments) {
        mSegments = segments;
    }

    protected void setupBody(Vector2 bottomleft) {
        mBody = Box2DManager.getInstance().getBox(
                new Vector2(bottomleft.x + mWidth/2, bottomleft.y + mHeight/2),
                new Vector2(mWidth, mHeight),
                BodyDef.BodyType.StaticBody, true
        );
        mBody.setUserData(this);
    }

    public void render(ShaderProgram shaderProgram) {
        for (Wall wall : mWalls) {
            wall.render(shaderProgram);
        }
    }
}
