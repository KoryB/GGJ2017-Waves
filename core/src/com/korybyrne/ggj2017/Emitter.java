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
    private static float AMPLITUDE = 1.0f;
    private static float PERIOD = 10f;
    private static float FREQUENCY = (float) ( (2*Math.PI) / 10 );  // Period of 10

    private Vector3 mPos;
    private float mSpinRate = (float) (2*Math.PI);
    private float mTheta;
    private Matrix4 mTransform;

    private float mTriggerTime = 0.0f;
    private float mTriggerLength = 10.0f;
    private float mEnd = 0.0f;
    private float mFinalEnd = 800.0f;
    private float mTriggerSpeed = mFinalEnd / mTriggerLength;
    private float mTriggerWidth = 50.0f;
    private int mGLIndex;
    private String mGLIndexString;

    public Emitter(int index) {
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

        mGLIndex = index;
        mGLIndexString = String.format("[%d]", mGLIndex);
    }

    public void setEmissionUniforms(ShaderProgram shaderProgram) {
//        uniform vec4 u_wavesOrigin;
//        uniform float u_wavesTheta;
//        uniform float u_wavesAmplitude;
//        uniform float u_wavesFreq;

        shaderProgram.setUniform4fv("u_wavesOrigin" + mGLIndexString, new float[] {
                mPos.x, mPos.y, mPos.z, 1
        }, 0, 4);
        shaderProgram.setUniform1fv("u_wavesAmplitude" + mGLIndexString, new float[] {
                AMPLITUDE
        }, 0, 1);
        shaderProgram.setUniform1fv("u_wavesFreq" + mGLIndexString, new float[] {
                FREQUENCY
        }, 0, 1);
        shaderProgram.setUniform1fv("u_wavesTheta" + mGLIndexString, new float[]{mTheta}, 0, 1);
        shaderProgram.setUniform1fv("u_wavesWidth" + mGLIndexString, new float[]{mTriggerWidth}, 0, 1);
        shaderProgram.setUniform1fv("u_wavesEnd" + mGLIndexString, new float[]{mEnd}, 0, 1);
    }

    public void trigger(Vector2 pos) {
        mTriggerTime = mTriggerLength;
        mEnd = 0.0f;
        mTheta = 0.0f;

        mPos.x = pos.x;
        mPos.y = pos.y;
    }

    public void update(float delta) {

        if (mTriggerTime > 0) {
            mTriggerTime -= delta;
            mEnd += mTriggerSpeed*delta;
            mTheta += mSpinRate * delta;
        }
    }

    public void render(ShaderProgram shaderProgram) {
        mTransform.setTranslation(mPos);
        shaderProgram.setUniformMatrix("u_trans", mTransform);
        MESH.render(shaderProgram, GL20.GL_TRIANGLE_STRIP);
    }

    public void renderCircle(ShapeRenderer shapeRenderer) {
        shapeRenderer.circle(mPos.x, mPos.y, mEnd);
        shapeRenderer.circle(mPos.x, mPos.y, mEnd-mTriggerWidth);
    }
}
