package com.gabilamnanodegree.spotifystreaming.model.interactors;

/**
 * Created by gabrielmarcos on 6/4/15.
 *
 * Interactors can perform a user case working in background thread.
 * They implement an async task for calling the methods in the Repository
 * to get the data
 *
 */
public interface Interactor {

    void cancel();
}
