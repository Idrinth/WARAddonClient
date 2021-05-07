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

package de.idrinth.waraddonclient.model;

public class NoAddon implements de.idrinth.waraddonclient.model.Addon {

    /**
     *
     * @return java.util.ArrayList
     */
    @Override
    public java.util.ArrayList<String> getTags() {
        return new java.util.ArrayList<>();
    }

    /**
     *
     * @param tag
     * @return boolean
     */
    @Override
    public boolean hasTag(String tag) {
        return false;
    }

    /**
     *
     * @return String
     */
    @Override
    public String getVersion() {
        return "";
    }

    /**
     *
     * @return String
     */
    @Override
    public String getInstalled() {
        return "";
    }

    /**
     *
     * @return AddonSettings
     */
    @Override
    public AddonSettings getUploadData() {
        return null;
    }

    /**
     *
     * @param language
     * @return String
     */
    @Override
    public String getDescription(String language) {
        return "<html><h1>Welcome to the client.</h1><p>To get something more useful here, select an addon to the left.</p>";
    }

    /**
     *
     * @return String
     */
    @Override
    public String getName() {
        return "Welcome";
    }

    /**
     * does NOT install the none existing addon
     *
     * @throws java.lang.Exception
     */
    @Override
    public void install() throws java.lang.Exception {
        throw new UnsupportedOperationException("You can not uninstall an addon that doesn't exist.");
    }

    /**
     * does NOT uninstall the not existing addon
     *
     * @throws java.lang.Exception
     */
    @Override
    public void uninstall() throws java.lang.Exception {
        throw new UnsupportedOperationException("You can not uninstall an addon that doesn't exist");
    }

    @Override
    public String getStatus() {
        return "unknown";
    }

}
