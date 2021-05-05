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

package de.idrinth.waraddonclient.implementation.service;

public final class FindAddonFolder {

    /**
     * not to be callsed
     */
    private FindAddonFolder() {
        //should never be constructed
    }

    /**
     *
     * @param name
     * @return java.io.File
     */
    public static java.io.File find(String name) {
        String[] path = new String[3];
        path[0] = "interface";
        path[1] = "addons";
        path[2] = name;
        java.io.File folder = new java.io.File(System.getProperty("user.dir"));
        for (int counter = 0; counter < 3; counter++) {
            folder = findMatch(folder, path[counter]);
            if (folder == null || !folder.exists()) {
                return null;
            }
        }
        return folder;
    }

    /**
     *
     * @param folder
     * @param search
     * @return java.io.Folder
     */
    private static java.io.File findMatch(java.io.File folder, String search) {
        if (!folder.exists()) {
            return null;
        }
        for (java.io.File innerFolder : folder.listFiles()) {
            if (innerFolder.getName().equalsIgnoreCase(search)) {
                return innerFolder;
            }
        }
        return null;
    }

}
