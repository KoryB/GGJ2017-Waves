package com.korybyrne.ggj2017;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

public class Box2DManager {
    private final static Box2DManager mInstance;
    public final static Vector2 GRAVITY = new Vector2(0, -10f);
    public final static float TIME_STEP = 1/300f;
    public final static int VELOCITY_ITERATIONS = 6;
    public final static int POSITION_ITERATIONS = 2;
    public final static float WORLD_SCALE = 30; // 1 meter is 30 pixels
    public final static short NORMAL_GROUP = 1;
    static {
        mInstance = new Box2DManager();
    }

    public World mWorld; // My world

    Box2DDebugRenderer mDebugRenderer;
    Matrix4 mDebugMatrix;

    private float accumulator = 0;

    private Box2DManager() {
        Box2D.init();

        if (GRAVITY == null) {
            throw new RuntimeException("WTF?");
        }

        mWorld = new World(GRAVITY, true);
        mWorld.setContactListener(new PlayerCollisionListener());

        mDebugRenderer = new Box2DDebugRenderer();
        mDebugMatrix = new Matrix4();
    }

    public static Box2DManager getInstance() {
        return mInstance;
    }

    public Body getBox(Vector2 pos, Vector2 extents, BodyDef.BodyType type, boolean pixels) {
        return getBox(pos.x, pos.y, extents.x, extents.y, type, pixels, (short)1);
    }

    public Body getBox(float x, float y, float w, float h, BodyDef.BodyType type, boolean pixels, short group) {
        float scaleFactor = (pixels? WORLD_SCALE : 1);

        BodyDef bodyDef = new BodyDef();
        Body body;
        PolygonShape polygonShape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();

        bodyDef.type = type;
        bodyDef.position.set(
                x / scaleFactor,
                y / scaleFactor
        );
        body = mWorld.createBody(bodyDef);

        polygonShape.setAsBox(
                w/2 / scaleFactor,
                h/2 / scaleFactor
        );

        fixtureDef.shape = polygonShape;
        fixtureDef.density = 1f;
        if (group < 0) {
            fixtureDef.isSensor = true;
        }

        body.createFixture(fixtureDef);

        polygonShape.dispose();

        return body;
    }

    public void updateSprite(Sprite sprite, Body body) {
        sprite.setPosition(
                body.getPosition().x * WORLD_SCALE,
                body.getPosition().y * WORLD_SCALE
        );

        sprite.setRotation(
                body.getAngle() * MathUtils.radDeg
        );
    }

    public void updateDebug(Matrix4 cam) {
        mDebugMatrix.set(cam.cpy());
        mDebugMatrix.scale(WORLD_SCALE, WORLD_SCALE, 1f);
    }

    public void step(float delta) {

        accumulator += delta;

        while (accumulator >= TIME_STEP) {
            mWorld.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
            accumulator -= TIME_STEP;
        }
    }

    public void renderDebug() {
        mDebugRenderer.render(mWorld, mDebugMatrix);
    }

    public void dispose() {
        mWorld.dispose();
    }

    public Joint createJoint(JointDef jointDef) {
        return mWorld.createJoint(jointDef);
    }
}
