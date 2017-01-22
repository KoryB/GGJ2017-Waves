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

        Object userDataA = contact.getFixtureA().getBody().getUserData();
        Object userDataB = contact.getFixtureB().getBody().getUserData();
        Player player = null;
        
        if (userDataA != null) {
            if (userDataA instanceof Player) {
                player = (Player) userDataA;
            }
        }

        if (userDataB != null) {
            if (userDataB instanceof Player) {
                player = (Player) userDataB;
            }
        }

        if (player != null) {
            EmissionManager.getInstance().trigger(new Vector2(
                    player.getSprite().getX(),
                    player.getSprite().getY()
            ));
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }
}
