/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.idrinth.factory;

/**
 *
 * @author BJ
 */
public class Logger {

    protected static de.idrinth.Logger instance = null;

    public synchronized static de.idrinth.Logger build() {
        if (instance == null) {
            instance = new de.idrinth.Logger();
        }
        return instance;
    }
}
