package com.korybyrne.ggj2017;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

/**
 * Created by kory on 1/21/17.
 */
public class Player {

    private Texture mTexture;
    private Sprite mSprite;

    private Vector3 mPos;
    private Body mBody;

    public Player() {
        // load the images for the droplet and the bucket, 64x64 pixels each
        mTexture = new Texture(Gdx.files.internal("bucket.png"));
        mSprite = new Sprite(mTexture);
        mSprite.setOriginCenter();

        mPos = new Vector3(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight(), 0);
        mBody = Box2DManager.getInstance().getBox(
                mPos.x, mPos.y,
                mSprite.getWidth(), mSprite.getHeight(),
                BodyDef.BodyType.DynamicBody, true
        );
        mBody.setUserData(this);
    }

    public void update(float delta) {

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
        mTexture.dispose();
    }
}
