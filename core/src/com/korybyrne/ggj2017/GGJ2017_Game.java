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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;

public class GGJ2017_Game implements Screen {
    final Drop game;

    Texture bucketImage;
    Sprite bucketSprite;
    SpriteBatch spriteBatch;
    Sound dropSound;
    Music rainMusic;

    OrthographicCamera camera;
    ShaderProgram shaderProgram;

    Vector3 mousePos;
    boolean mouseDown = false;

    Emitter emitter = new Emitter(0, new Vector3(400, 100, 0));
    Emitter emitter2 = new Emitter(1, new Vector3(375, 100, 0));
    Wall wall = new Wall();

    Body body;
    Body floorBody;

    Box2DManager PHYS_MAN = Box2DManager.getInstance();

    public GGJ2017_Game(final Drop gam) {
        this.game = gam;

        // load the images for the droplet and the bucket, 64x64 pixels each
        bucketImage = new Texture(Gdx.files.internal("bucket.png"));
        bucketSprite = new Sprite(bucketImage);
        bucketSprite.setOriginCenter();

        spriteBatch = new SpriteBatch();

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

        body = PHYS_MAN.getBox(
                Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight(),
                bucketSprite.getWidth(), bucketSprite.getHeight(),
                BodyDef.BodyType.DynamicBody, true
        );

        floorBody = PHYS_MAN.getBox(
                Gdx.graphics.getWidth() / 2 - 75, Gdx.graphics.getHeight() / 2,
                100, 5,
                BodyDef.BodyType.StaticBody, true
        );
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

        emitter.update(delta);
        emitter2.update(delta);
        PHYS_MAN.updateSprite(bucketSprite, body);

        if (!mouseDown && Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            mouseDown = true;

            emitter.trigger();
            emitter2.trigger();
        } else {
            mouseDown = false;
        }

        mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mousePos);

        shaderProgram.begin();
        shaderProgram.setUniformMatrix("u_projTrans", camera.combined);
        emitter.setEmissionUniforms(shaderProgram);
        emitter2.setEmissionUniforms(shaderProgram);

        emitter.render(shaderProgram);
        emitter2.render(shaderProgram);
        wall.render(shaderProgram);

        shaderProgram.end();

        spriteBatch.begin();

        spriteBatch.draw(
                bucketSprite,
                bucketSprite.getX() - bucketSprite.getWidth() / 2, bucketSprite.getY() - bucketSprite.getHeight() / 2,
                bucketSprite.getOriginX(), bucketSprite.getOriginY(),
                bucketSprite.getWidth(), bucketSprite.getHeight(),
                bucketSprite.getScaleX(), bucketSprite.getScaleY(),
                bucketSprite.getRotation());
        spriteBatch.end();

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
        bucketImage.dispose();
        dropSound.dispose();
        rainMusic.dispose();

        PHYS_MAN.dispose();
    }
}