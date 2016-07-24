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
package de.idrinth.waraddonclient.factory;

public class User {

    private static de.idrinth.waraddonclient.implementation.model.User instance;

    /**
     * prevent initialization
     */
    private User() {
        //this is static only
    }

    /**
     * returns data of the current user
     *
     * @return de.idrinth.waraddonclient.implementation.model.User
     */
    public synchronized static de.idrinth.waraddonclient.implementation.model.User build() {
        if (instance == null) {
            instance = new de.idrinth.waraddonclient.implementation.model.User();
        }
        return instance;
    }
}
