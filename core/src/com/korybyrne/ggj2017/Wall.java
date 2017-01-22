package com.korybyrne.ggj2017;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;


public abstract class Wall {
    public static final int SIZE = 50;
    public static final int HALF_SIZE = SIZE/2;
    public static final int THICKNESS = 7;
    public static final int HALF_THICKNESS = 7/2;

    protected Vector3 mPos;
    protected Matrix4 mTransform;

    protected Wall(Vector3 pos) {
        mPos = pos.cpy();
        mTransform = new Matrix4();
    }

    protected abstract Mesh getMESH();

    public void update() {

    }

    public void render(ShaderProgram shaderProgram) {
        mTransform.setTranslation(mPos);
        shaderProgram.setUniformMatrix("u_trans", mTransform);
        getMESH().render(shaderProgram, GL20.GL_TRIANGLE_STRIP);
    }
}
