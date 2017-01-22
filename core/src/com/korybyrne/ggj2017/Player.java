package com.korybyrne.ggj2017;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kory on 1/21/17.
 */
public class Player {
    private static final short JOINT_GROUP = (short)-1;
    private static final float IMPULSE_VALUE = 40.0f;
    private static final float JOINT_HW = 1;
    private static final float SPEED_THRESHOLD = 2;
    private static final Vector2 UR_VEC = new Vector2(1, 1).nor();
    private static final Vector2 UL_VEC = new Vector2(-1, 1).nor();

    private Texture mTexture;
    private Sprite mSprite;
    private float mHalfWidth, mHalfHeight;
    private float mHalfPhysWidth, mHalfPhysHeight;
    private float mHalfPhysDiagonal;

    private float mCurrentSpeed = 0;
    private float mOldSpeed = 0;
    private boolean mMovingLeft = false;

    private Vector3 mPos;
    private Body mBody;

    private List<RevoluteJoint> mJoints;    // 2 3
                                            // 1 0
    private RevoluteJoint mStaticJoint = null;

    public Player() {
        // load the images for the droplet and the bucket, 64x64 pixels each
        mTexture = new Texture(Gdx.files.internal("bucket.png"));
        mSprite = new Sprite(mTexture);
        mSprite.setOriginCenter();
        mHalfWidth = mSprite.getWidth()/2;
        mHalfHeight = mSprite.getHeight()/2;
        mHalfPhysWidth = mHalfWidth / Box2DManager.WORLD_SCALE;
        mHalfPhysHeight = mHalfHeight / Box2DManager.WORLD_SCALE;
        mHalfPhysDiagonal =(float) (mHalfPhysHeight*Math.sqrt(2));

        mPos = new Vector3(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight(), 0);
        setupBody();
    }

    protected void setupBody() {
        mBody = Box2DManager.getInstance().getBox(
                mPos.x, mPos.y,
                mSprite.getWidth(), mSprite.getHeight(),
                BodyDef.BodyType.DynamicBody, true, Box2DManager.NORMAL_GROUP
        );
        mBody.setUserData(this);

        ////////// JOINTS //////////
        mJoints = new ArrayList<RevoluteJoint>(4);
        // br
        mJoints.add(createJoint(
                mPos.x + mHalfWidth - JOINT_HW,
                mPos.y - mHalfHeight + JOINT_HW
        ));
        // bl
        mJoints.add(createJoint(
                mPos.x - mHalfWidth + JOINT_HW,
                mPos.y - mHalfHeight + JOINT_HW
        ));
        // tl
        mJoints.add(createJoint(
                mPos.x - mHalfWidth + JOINT_HW,
                mPos.y + mHalfHeight - JOINT_HW
        ));
        // tr
        mJoints.add(createJoint(
                mPos.x + mHalfWidth - JOINT_HW,
                mPos.y + mHalfHeight - JOINT_HW
        ));

    }

    protected RevoluteJoint createJoint(float jx, float jy) {
        RevoluteJointDef jointDef;
        RevoluteJoint joint;
        Body jointBody;

        jointBody = Box2DManager.getInstance().getBox(
                jx, jy,
                2*JOINT_HW, 2*JOINT_HW,
                BodyDef.BodyType.DynamicBody, true, JOINT_GROUP
        );

        jointDef = new RevoluteJointDef();
        jointDef.initialize(
                mBody, jointBody,
                new Vector2(jx, jy).scl(1 / Box2DManager.WORLD_SCALE)
        );
        joint = (RevoluteJoint)Box2DManager.getInstance().createJoint(jointDef);

        return joint;
    }

    protected int getDegrees() {
        return Math.round(mBody.getAngle()*MathUtils.radDeg);
    }

    public void update(float delta) {
        Box2DManager.getInstance().updateSprite(mSprite, mBody);

        if (mStaticJoint != null) {
            mCurrentSpeed = mBody.getLinearVelocity().len() + Math.abs(mBody.getAngularVelocity());
            mOldSpeed = 0.1f*mCurrentSpeed + 0.9f*mOldSpeed;

            if (mCurrentSpeed <= SPEED_THRESHOLD) {
                mBody.setAngularVelocity(0);
                mBody.setLinearVelocity(Vector2.Zero);
                mStaticJoint.getBodyB().setType(BodyDef.BodyType.DynamicBody);
                mStaticJoint = null;

                if (mMovingLeft) {
                    EmissionManager.getInstance().trigger(
                            mBody.getWorldCenter().cpy()
                            .add(UR_VEC.cpy().scl(-mHalfPhysDiagonal))
                            .scl(Box2DManager.WORLD_SCALE)
                    );
                } else {
                    EmissionManager.getInstance().trigger(
                            mBody.getWorldCenter().cpy()
                            .add(UL_VEC.cpy().scl(-mHalfPhysDiagonal))
                            .scl(Box2DManager.WORLD_SCALE)
                    );
                }
            }
        }
    }

    private int getRightIndex() {
        return Math.floorMod(getDegrees() / 90, 4);
    }

    private int getLeftIndex() {
        return (getRightIndex()+1) % 4;
    }

    public void rotateRight() {
        if (mStaticJoint == null) {
            mStaticJoint = mJoints.get(
                    getRightIndex()
            );
            mStaticJoint.getBodyB().setType(BodyDef.BodyType.StaticBody);
            mBody.applyLinearImpulse(
                    UR_VEC.cpy().scl(IMPULSE_VALUE),
                    mBody.getWorldCenter().cpy().add(UL_VEC.cpy().scl(mHalfPhysDiagonal)),
                    true
            );

            mOldSpeed = IMPULSE_VALUE;
            mMovingLeft = false;
        }
    }

    public void rotateLeft() {
        if (mStaticJoint == null) {
            mStaticJoint = mJoints.get(
                    getLeftIndex()
            );
            mStaticJoint.getBodyB().setType(BodyDef.BodyType.StaticBody);
            mBody.applyLinearImpulse(
                    UL_VEC.cpy().scl(IMPULSE_VALUE),
                    mBody.getWorldCenter().cpy().add(UR_VEC.cpy().scl(mHalfPhysDiagonal)),
                    true
            );

            mOldSpeed = IMPULSE_VALUE;
            mMovingLeft = true;
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(
                mSprite,
                mSprite.getX() - mSprite.getWidth() / 2, mSprite.getY() - mSprite.getHeight() / 2,
                mSprite.getOriginX(), mSprite.getOriginY(),
                mSprite.getWidth(), mSprite.getHeight(),
                mSprite.getScaleX(), mSprite.getScaleY(),
                mSprite.getRotation()
        );
    }

    public Sprite getSprite() {
        return mSprite;
    }

    public Body getBody() {
        return mBody;
    }

    public void dispose() {
        mTexture.dispose();
    }
}
