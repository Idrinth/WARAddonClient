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

package de.idrinth.waraddonclient.interfaces.model;

public interface Addon {

    /**
     * get the tags of this addon
     *
     * @return java.util.ArrayList
     */
    public java.util.ArrayList<String> getTags();

    /**
     * does this addon have the given tag?
     *
     * @param tag
     * @return boolean
     */
    public boolean hasTag(String tag);

    /**
     * the avaible version
     *
     * @return String
     */
    public String getVersion();

    /**
     * the installed version
     *
     * @return String
     */
    public String getInstalled();

    /**
     * returns the settings for this addon
     *
     * @return AddonSettings
     */
    public de.idrinth.waraddonclient.implementation.model.AddonSettings getUploadData();

    /**
     * return a languages description if avaible, otherwise a default
     *
     * @param language
     * @return String
     */
    public String getDescription(String language);

    /**
     * get addon name
     *
     * @return String
     */
    public String getName();

    /**
     * downloads a zip and unpacks it
     *
     * @throws java.lang.Exception
     */
    public void install() throws java.lang.Exception;

    /**
     * downloads a zip and unpacks it
     *
     * @throws java.lang.Exception
     */
    public void uninstall() throws java.lang.Exception;
}
