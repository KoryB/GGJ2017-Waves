package com.korybyrne.ggj2017;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;

public class GGJ2017_Game implements Screen {
    final Drop game;

    SpriteBatch spriteBatch;
    ShapeRenderer shapeRenderer;
    Sound dropSound;
    Music rainMusic;

    OrthographicCamera camera;
    ShaderProgram shaderProgram;

    Vector3 mousePos;
    boolean mouseDown = false;
    Wall wall = new Wall();

    Player player = new Player();
    Body floorBody;

    Box2DManager PHYS_MAN = Box2DManager.getInstance();
    EmissionManager EM_MAN = EmissionManager.getInstance();

    public GGJ2017_Game(final Drop gam) {
        this.game = gam;

        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        // load the drop sound effect and the rain background "music"
        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
        rainMusic.setLooping(true);

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Drop.SCREEN_WIDTH, Drop.SCREEN_HEIGHT);

        shaderProgram = new ShaderProgram(
                Gdx.files.internal("wall.vertex.glsl"),
                Gdx.files.internal("wall.fragment.glsl")
        );

        if (! shaderProgram.isCompiled()) {
            throw new RuntimeException(shaderProgram.getLog());
        }

        for (String string : shaderProgram.getUniforms()) {
            System.out.println(string);
        }

        mousePos = new Vector3();

        /////// BOX 2D //////////
        // Render before physics

        floorBody = PHYS_MAN.getBox(
                Gdx.graphics.getWidth() / 2 - 75, Gdx.graphics.getHeight() / 2,
                100, 5,
                BodyDef.BodyType.StaticBody, true
        );
        floorBody.setUserData("Floor");
    }

    @Override
    public void render(float delta) {
        // clear the screen with a dark blue color. The
        // arguments to glClearColor are the red, green
        // blue and alpha component in the range [0,1]
        // of the color to be used to clear the screen.
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell the camera to update its matrices.
        camera.update();
        PHYS_MAN.updateDebug(camera.combined);

        EM_MAN.update(delta);
        PHYS_MAN.updateSprite(player.getSprite(), player.getBody());

        if (!mouseDown && Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            mouseDown = true;
        } else {
            mouseDown = false;
        }

        mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mousePos);

        shaderProgram.begin();
        shaderProgram.setUniformMatrix("u_projTrans", camera.combined);
        EM_MAN.render(shaderProgram);
        wall.render(shaderProgram);

        shaderProgram.end();

        spriteBatch.begin();
        player.render(spriteBatch);
        spriteBatch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        EM_MAN.renderCircles(shapeRenderer);
        shapeRenderer.end();

        PHYS_MAN.renderDebug();

        PHYS_MAN.step(delta);
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        // start the playback of the background music
        // when the screen is shown
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        player.dispose();
        dropSound.dispose();
        rainMusic.dispose();

        PHYS_MAN.dispose();
    }
}