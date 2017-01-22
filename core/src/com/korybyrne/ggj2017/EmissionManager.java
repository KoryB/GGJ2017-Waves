package com.korybyrne.ggj2017;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class EmissionManager {
    private static final EmissionManager mInstance;
    static {
        mInstance = new EmissionManager();
    }
    private static final int WAVES = 10;

    private final Emitter[] mEmitters = new Emitter[WAVES];
    private int mCurrentIndex = 0;

    private EmissionManager() {
        for (int i = 0; i < WAVES; i++) {
            mEmitters[i] = new Emitter();
        }
    }
    
    public void init(ShaderProgram shaderProgram) {

        // Initialize gl
        shaderProgram.setUniform4fv("u_wavesOrigin[0]", new float[WAVES*4], 0, WAVES*4);
        shaderProgram.setUniform1fv("u_wavesAmplitude[0]", new float[WAVES], 0, WAVES);
        shaderProgram.setUniform1fv("u_wavesFreq[0]", new float[WAVES], 0, WAVES);
        shaderProgram.setUniform1fv("u_wavesTheta[0]", new float[WAVES], 0, WAVES);
        shaderProgram.setUniform1fv("u_wavesWidth[0]", new float[WAVES], 0, WAVES);
        shaderProgram.setUniform1fv("u_wavesEnd[0]", new float[WAVES], 0, WAVES);
        shaderProgram.setUniform1fv("u_wavesFinal[0]", new float[WAVES], 0, WAVES);
    }

    public void update(float delta) {
        for (Emitter emitter : mEmitters) {
            emitter.update(delta);
        }
    }

    public void render(ShaderProgram shaderProgram) {
        for (Emitter emitter : mEmitters) {
            emitter.setEmissionUniforms(shaderProgram);
        }
    }

    public void trigger(Vector2 pos) {
        int startIndex = mCurrentIndex;

        while (mEmitters[mCurrentIndex].isActive()) {
            mCurrentIndex = (mCurrentIndex + 1) % mEmitters.length;
            if (startIndex == mCurrentIndex) {
//                System.out.println("All emitters full");
                return;
            }
        }
        
        mEmitters[mCurrentIndex].trigger(pos, mCurrentIndex);
        mCurrentIndex = (mCurrentIndex + 1) % mEmitters.length;
    }

    public static EmissionManager getInstance() {
        return mInstance;
    }

    public void renderCircles(ShapeRenderer shapeRenderer) {
        for (Emitter emitter : mEmitters) {
            emitter.renderCircle(shapeRenderer);
        }
    }
}
