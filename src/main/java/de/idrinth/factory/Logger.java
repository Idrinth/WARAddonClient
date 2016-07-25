/*
 * Copyright (C) 2016 Björn Büttner
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.idrinth.factory;

public final class Logger {

    private static de.idrinth.Logger instance;

    /**
     * Prevent objects of this class
     */
    private Logger() {
        //not mean to be used
    }

    /**
     *
     * @return de.idrinth.Logger
     */
    public synchronized static de.idrinth.Logger build() {
        if (instance == null) {
            instance = new de.idrinth.Logger();
        }
        return instance;
    }
}
