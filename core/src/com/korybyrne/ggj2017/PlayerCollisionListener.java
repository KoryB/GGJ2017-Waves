package com.korybyrne.ggj2017;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 * Created by kory on 1/21/17.
 */
public class PlayerCollisionListener implements ContactListener {

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void beginContact(Contact contact) {
        Object userData = contact.getFixtureA().getBody().getUserData();
        if (userData != null) {
            userData = contact.getFixtureB().getBody().getUserData();

            if (userData != null) {
                Vector2 finalPos = new Vector2(0, 0);
                Vector2[] points = contact.getWorldManifold().getPoints();
                for (Vector2 pos : points) {
                    finalPos.add(pos.cpy().scl(1.0f / points.length));
                }
                finalPos = finalPos.scl(Box2DManager.WORLD_SCALE);

                EmissionManager.getInstance().trigger(finalPos);
            }
        }

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
