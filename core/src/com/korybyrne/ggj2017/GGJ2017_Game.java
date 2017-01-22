package com.korybyrne.ggj2017;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import org.w3c.dom.css.Rect;

import java.util.ArrayList;
import java.util.List;

public class GGJ2017_Game implements Screen {
    private static final boolean DEBUG = true;
    private static final float DEBUG_SPEED = 1200;
    public static Rectangle SCREEN;
    final Drop game;

    SpriteBatch spriteBatch;
    ShapeRenderer shapeRenderer;
    Sound dropSound;
    Music rainMusic;

    OrthographicCamera camera;
    Vector2 camPos;
    ShaderProgram shaderProgram;

    Vector3 mousePos;

    Player player;

    Box2DManager PHYS_MAN = Box2DManager.getInstance();
    EmissionManager EM_MAN = EmissionManager.getInstance();

    List<Phys> mLevel;
    float mLastX;
    float mLastY;
    float mLastW;
    float mLastH;

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
        camera.zoom = 1f;
        camPos = new Vector2(
                Drop.SCREEN_WIDTH/2, Drop.SCREEN_HEIGHT/2
        );

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

        float unit = Wall.SIZE;

        mLevel = new ArrayList<Phys>();


        // Intro Fall
        float hallwidth = unit*10;
        float hallheight = unit*50;
        float twenty = unit*20;
        float ten = unit*10;
        float two = unit*2;
        float three = unit*3;
        float four = unit*4;
        float five = unit*5;
        float six = unit*6;

        player = new Player(new Vector3(hallwidth/2f - unit/2, hallheight, 0f));

        add(-unit*2, 0, unit*2, hallheight);
        add(hallwidth, unit*10, unit*2, hallheight);

        //Gubbins
        add(0, hallheight/2f + five, hallwidth - unit*6, unit);
        add(hallwidth - unit*1, hallheight - unit*39, unit*1, unit);


        // Floor
        add(0, -three, unit*30, three);
        add(hallwidth, unit*15, 72*unit, two);
        add(hallwidth*2, 0, twenty, three);
        add(mLastX+mLastW, -unit*2, six, unit);
        add(mLastX, -unit, three, unit);
        add(mLastX+six, -unit, twenty, four);
        add(mLastX+mLastW, -unit*2, six, unit);
        add(mLastX, -unit, three, unit);

        // Staircase
        add(mLastX+six, -unit, twenty, four);
        add(mLastX+ten, mLastY+mLastH, ten, three);
        add(mLastX+six, mLastY+mLastH, four, three);
        float endx = mLastX + mLastW;
        add(mLastX-six, unit*13, three, two);
        add(mLastX+mLastW, mLastY-unit, three, unit);
        add(endx, -unit, two, hallheight);

        // Leveltwo
        add(hallwidth, five + unit*20, unit*33, five);
        add(mLastX+mLastW, mLastY, six, two);
        add(mLastX+mLastW+five, mLastY - four, unit*15, two);
        add(mLastX+mLastW+three, mLastY - four, six, two);


        //////// SNAP CAMERA ////////////
        Vector2 camDelta = new Vector2(
                player.getSprite().getX() - camPos.x,
                player.getSprite().getY() - camPos.y
        );
        camera.translate(camDelta);
        camPos.add(camDelta);

        SCREEN = new Rectangle();
        updateScreen();
    }

    private void updateScreen() {
        SCREEN.setPosition(
                camera.position.x - (camera.zoom * camera.viewportWidth / 2),
                camera.position.y - (camera.zoom * camera.viewportHeight / 2)
        );
        SCREEN.setSize(
                camera.zoom * camera.viewportWidth,
                camera.zoom * camera.viewportHeight
        );
    }
    
    private Phys add(float x, float y, float w, float h) {
        mLastX = x;
        mLastY = y;
        mLastW = w;
        mLastH = h;
        
        return levelAdd(new PhysBox(x, y, w, h));
    }

    private Phys levelAdd(Phys phys) {
        mLevel.add(phys);
        return phys;
    }

    @Override
    public void render(float delta) {
        // clear the screen with a dark blue color. The
        // arguments to glClearColor are the red, green
        // blue and alpha component in the range [0,1]
        // of the color to be used to clear the screen.
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        PHYS_MAN.updateDebug(camera.combined);
        spriteBatch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.rotateLeft();
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.rotateRight();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            player.jump();
        }

        EM_MAN.update(delta);
        player.update(delta);

        // tell the camera to update its matrices.
        if (!DEBUG) {
            Vector2 camDelta = new Vector2(
                    player.getSprite().getX() - camPos.x,
                    player.getSprite().getY() - camPos.y
            ).scl(0.1f);
            camera.translate(camDelta);
            camPos.add(camDelta);
            updateScreen();
        } else {
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                camera.translate(Vector2.Y.cpy().scl(DEBUG_SPEED*delta));
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                camera.translate(Vector2.Y.cpy().scl(-DEBUG_SPEED*delta));
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                camera.translate(Vector2.X.cpy().scl(-DEBUG_SPEED*delta));
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                camera.translate(Vector2.X.cpy().scl(DEBUG_SPEED*delta));
            }
        }

        mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mousePos);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//        EM_MAN.renderCircles(shapeRenderer);
        shapeRenderer.end();

        if (!DEBUG) {
            shaderProgram.begin();
            shaderProgram.setUniformMatrix("u_projTrans", camera.combined);
            EM_MAN.render(shaderProgram);
            for (Phys phys : mLevel) {
                phys.render(shaderProgram);
            }
            player.render(shaderProgram);

            shaderProgram.end();
        }
        if (DEBUG) {
            PHYS_MAN.renderDebug();
        }
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