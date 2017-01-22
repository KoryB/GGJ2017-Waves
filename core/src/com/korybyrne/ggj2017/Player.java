package com.korybyrne.ggj2017;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
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
    private static Mesh MESH;
    private static final short JOINT_GROUP = (short)-1;
    private static final float IMPULSE_VALUE = 40.0f;
    private static final float JUMP_VALUE = 40.0f;
    private static final float JOINT_HW = 1;
    private static final float SPEED_THRESHOLD = 1;
    private static final float IN_AIR_THRESHOLD = 0.2f;
    private static final Vector2 UR_VEC = new Vector2(1, 1).nor();
    private static final Vector2 UL_VEC = new Vector2(-1, 1).nor();

    private static Texture TEXTURE;
    private Sprite mSprite;
    private float mHalfWidth, mHalfHeight;
    private float mHalfPhysWidth, mHalfPhysHeight;
    private float mHalfPhysDiagonal;

    private float mCurrentSpeed = 0;
    private float mOldSpeed = 0;
    private boolean mMovingLeft = false;
    private float mStartTheta = 0.0f;
    private boolean mInAir = false;
    private boolean mCanRotate = true;

    private Vector3 mPos;
    private Body mBody;
    private Matrix4 mTransform = new Matrix4();

    private List<RevoluteJoint> mJoints;    // 2 3
                                            // 1 0
    private RevoluteJoint mStaticJoint = null;
    private boolean mRotating = false;

    public Player() {
        if (MESH == null) {
            MESH = new Mesh(true, 4, 0, VertexAttribute.Position());
            MESH.setVertices(new float[]{
                    -32, -32, 0,
                    -32, 32, 0,
                    32, -32, 0,
                    32, 32, 0
            });
        }
        // load the images for the droplet and the bucket, 64x64 pixels each
        TEXTURE = new Texture(Gdx.files.internal("player.png"));
        mSprite = new Sprite(TEXTURE);
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

        mInAir = (Math.abs(mBody.getLinearVelocity().y) > IN_AIR_THRESHOLD);

        mCurrentSpeed = Math.abs(mBody.getLinearVelocity().y) + Math.abs(mBody.getAngularVelocity());
        mOldSpeed = 0.1f*mCurrentSpeed + 0.9f*mOldSpeed;

        boolean slowed = mCurrentSpeed <= SPEED_THRESHOLD;
        boolean rotated = Math.abs(getDegrees() - mStartTheta) >= 90;

        if (slowed) {
            mCanRotate = true;
        }

        if (mRotating) {

            if (slowed || rotated) {
                if (slowed) {
                    mBody.setAngularVelocity(0);
                    mBody.setLinearVelocity(Vector2.Zero);
                }

                if (mStaticJoint != null) {
                    mStaticJoint.getBodyB().setType(BodyDef.BodyType.DynamicBody);
                    mStaticJoint = null;
                }
                mRotating = false;

                if (!mInAir) {
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
    }

    private int getRightIndex() {
        return Math.floorMod(getDegrees() / 90, 4);
    }

    private int getLeftIndex() {
        return (getRightIndex()+1) % 4;
    }

    private void rotate(int index, Vector2 impulse, Vector2 pos, boolean left) {
        if (!mRotating && mCanRotate) {
            mCanRotate = !mInAir;
            mRotating = true;

            if (!mInAir) {
                mStaticJoint = mJoints.get(index);
                mStaticJoint.getBodyB().setType(BodyDef.BodyType.StaticBody);
                mBody.applyLinearImpulse(impulse, pos, true);
            } else {
                mBody.applyLinearImpulse(impulse.scl(1), mBody.getWorldCenter(), true);
                mBody.applyAngularImpulse(15 * (left? 1:-1), true);
            }

            mOldSpeed = IMPULSE_VALUE;
            mStartTheta = getDegrees();
            mMovingLeft = left;
        }
    }

    public void jump() {
        if (!mInAir) {
            mBody.applyLinearImpulse(Vector2.Y.cpy().scl(JUMP_VALUE), mBody.getWorldCenter(), true);
        }
    }

    public void rotateRight() {
        rotate(
                getRightIndex(),
                UR_VEC.cpy().scl(IMPULSE_VALUE),
                mBody.getWorldCenter().cpy().add(UL_VEC.cpy().scl(mHalfPhysDiagonal)),
                false
        );
    }

    public void rotateLeft() {
        rotate(
                getLeftIndex(),
                UL_VEC.cpy().scl(IMPULSE_VALUE),
                mBody.getWorldCenter().cpy().add(UR_VEC.cpy().scl(mHalfPhysDiagonal)),
                true
        );
    }

    public void render(ShaderProgram shaderProgram) {
        mTransform.idt().translate(mSprite.getX(), mSprite.getY(), 0).rotate(Vector3.Z, mSprite.getRotation());
        shaderProgram.setUniformMatrix("u_trans", mTransform);
        TEXTURE.bind(0);
        shaderProgram.setUniformi("u_texture", 0);
        MESH.render(shaderProgram, GL20.GL_TRIANGLE_STRIP);
    }

    public void render(ShapeRenderer renderer) {
        renderer.setColor(0.2f, 0.2f, 0.2f, 1.0f);
        renderer.rect(
                mSprite.getX() - mSprite.getWidth()/2, mSprite.getY() - mSprite.getHeight()/2,
                mSprite.getOriginX(), mSprite.getOriginY(),
                mSprite.getWidth(), mSprite.getHeight(),
                mSprite.getScaleX(), mSprite.getScaleY(),
                mSprite.getRotation()
        );
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
        TEXTURE.dispose();
    }
}
