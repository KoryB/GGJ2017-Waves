package com.korybyrne.ggj2017;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

public class EmissionManager {
    private static final EmissionManager mInstance;
    static {
        mInstance = new EmissionManager();
    }
    private static final int WAVES = 2;

    private List<Emitter> mEmitters;

    private EmissionManager() {
        mEmitters = new ArrayList<Emitter>();

        for (int i = 0; i < WAVES; i++) {
            mEmitters.add(new Emitter(i));
        }
    }

    public void update(float delta) {
        for (Emitter emitter : mEmitters) {
            emitter.update(delta);
        }
    }

    public void render(ShaderProgram shaderProgram) {
        for (Emitter emitter : mEmitters) {
            emitter.setEmissionUniforms(shaderProgram);
            emitter.render(shaderProgram);
        }
    }

    public void trigger(int index, Vector2 pos) {
        mEmitters.get(index).trigger(pos);
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
