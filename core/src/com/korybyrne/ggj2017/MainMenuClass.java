package com.korybyrne.ggj2017;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Created by kory on 1/20/17.
 */
public class MainMenuClass implements Screen {
    final Drop game;
    OrthographicCamera camera;

    public MainMenuClass(final Drop game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.draw(game.batch, "The 3rd dimension has sucked in the 2nd. Nothing is left in the world...", 100, 400);
        game.font.draw(game.batch, "It's up to you! Every step you take gets you one step closer to...", 100, 350);
        game.font.draw(game.batch, "SAVING THE WORLD!!!!!!!!!", 100, 300);
        game.font.draw(game.batch, "<Z> is JUMP, <Left> and <Right> to MOVE. (JUMP then MOVE to JUMPMOVE!!!!!)", 100, 150);
        game.font.draw(game.batch, "Press <Z> to begin!!", 100, 100);
        game.batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            game.setScreen(new GGJ2017_Game(game));
            dispose();
        }
    }

    @Override
    public void show()
    {

    }

    @Override
    public void resize(int width, int height)
    {

    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void hide()
    {

    }

    @Override
    public void dispose() {

    }
}
