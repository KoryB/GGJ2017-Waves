package com.korybyrne.ggj2017;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Emitter {
    private static Mesh MESH = null;
    public static float AMPLITUDE = 10.0f;
    private static float PERIOD = 10f;
    private static float FREQUENCY = (float) (2*Math.PI/20);  // Period of 10

    private Vector3 mPos;
    private float mSpinRate = (float) (2*Math.PI);
    private float mTheta;
    private Matrix4 mTransform;

    public float mTriggerTime = 0.0f;
    public float mTriggerLength = 2.0f;
    private float mEnd = 0.0f;
    public float mFinalEnd = 500.0f;
    public float mTriggerSpeed = mFinalEnd / mTriggerLength;
    public float mTriggerWidth = 50.0f;
    private int mGLIndex;
    private String mGLIndexString;

    private boolean mActive = false;
    private boolean mDrawNextFrame = false;

    private boolean mContinuous = false;

    public Emitter() {
        if (MESH == null) {
            MESH = new Mesh(true, 4, 0, VertexAttribute.Position());
            MESH.setVertices(new float[] {
                    -10, -10, 0,
                    -10, 10, 0,
                    10, -10, 0,
                    10, 10, 0
            });
        }

        mPos = Vector3.Zero.cpy();
        mTransform = new Matrix4();

        mGLIndex = 0;
        mGLIndexString = String.format("[%d]", mGLIndex);
    }

    public void setEmissionUniforms(ShaderProgram shaderProgram) {
//        uniform vec4 u_wavesOrigin;
//        uniform float u_wavesTheta;
//        uniform float u_wavesAmplitude;
//        uniform float u_wavesFreq;

        if (mActive || mDrawNextFrame) {
            mDrawNextFrame = false;
            shaderProgram.setUniform4fv("u_wavesOrigin" + mGLIndexString, new float[]{
                    mPos.x, mPos.y, mPos.z, 1
            }, 0, 4);
            shaderProgram.setUniform1fv("u_wavesAmplitude" + mGLIndexString, new float[]{
                    AMPLITUDE
            }, 0, 1);
            shaderProgram.setUniform1fv("u_wavesFreq" + mGLIndexString, new float[]{
                    FREQUENCY
            }, 0, 1);
            shaderProgram.setUniform1fv("u_wavesTheta" + mGLIndexString, new float[]{mTheta}, 0, 1);
            shaderProgram.setUniform1fv("u_wavesWidth" + mGLIndexString, new float[]{mTriggerWidth}, 0, 1);
            shaderProgram.setUniform1fv("u_wavesEnd" + mGLIndexString, new float[]{mEnd}, 0, 1);
            shaderProgram.setUniform1fv("u_wavesFinal" + mGLIndexString, new float[]{mFinalEnd}, 0, 1);
        }
    }

    protected void setGLIndex(int index) {
        mGLIndex = index;
        mGLIndexString = String.format("[%d]", mGLIndex);
        mActive = true;
    }

    public void trigger(Vector2 pos, int index) {
        trigger(pos, index, false);
    }

    public void trigger(Vector2 pos, int index, boolean continuous) {
        setGLIndex(index);

        mTriggerTime = mTriggerLength;
        mEnd = 0.0f;
        mTheta = 0.0f;

        mPos.x = pos.x;
        mPos.y = pos.y;

        mContinuous = continuous;
    }

    public void update(float delta) {

        if (mActive) {
            mTriggerTime -= delta;
            mEnd += mTriggerSpeed*delta;
            mTheta += mSpinRate * delta;

            if (mTriggerTime < 0 && ! mContinuous) {
                mActive = false;
                mDrawNextFrame = true;
                mEnd = 0.0f;
                mTheta = 0.0f;
            }
        }
    }

    public void render(ShaderProgram shaderProgram) {
        mTransform.setTranslation(mPos);
        shaderProgram.setUniformMatrix("u_trans", mTransform);
        MESH.render(shaderProgram, GL20.GL_TRIANGLE_STRIP);
    }

    public void renderCircle(ShapeRenderer shapeRenderer) {
        if (mActive) {
            float attenuation = ((mFinalEnd - mEnd) / mFinalEnd) / 2;
            shapeRenderer.setColor(attenuation, attenuation, attenuation, attenuation);
            shapeRenderer.circle(mPos.x, mPos.y, mEnd);
            shapeRenderer.circle(mPos.x, mPos.y, mEnd-mTriggerWidth);
            shapeRenderer.circle(mPos.x, mPos.y, 10);
        }
    }

    public boolean isActive() {
        return mActive;
    }
}
