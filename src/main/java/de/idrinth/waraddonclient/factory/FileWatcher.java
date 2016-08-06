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

public final class FileWatcher {

    private static de.idrinth.waraddonclient.implementation.service.FileWatcher instance;

    /**
     * prevent initialization
     */
    private FileWatcher() {
        //this is static only
    }

    /**
     * Gets the filewatcher
     *
     * @return de.idrinth.waraddonclient.implementation.service.FileWatcher
     */
    public synchronized static de.idrinth.waraddonclient.implementation.service.FileWatcher build() {
        if (instance == null) {
            instance = new de.idrinth.waraddonclient.implementation.service.FileWatcher();
            new java.lang.Thread(instance).start();
        }
        return instance;
    }
}
