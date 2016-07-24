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
package de.idrinth.waraddonclient.configuration;

public class Version implements java.lang.Runnable {

    /**
     * the locally installed version
     *
     * @return String
     */
    public static String getLocalVersion() {
        return "1.3.0";
    }

    /**
     * gets the newest version from github and ads it to the displayed version
     */
    @Override
    public void run() {
        de.idrinth.waraddonclient.implementation.service.Sleeper.sleep(2500);
        try {
            de.idrinth.waraddonclient.factory.Interface.build().getRemoteVersionLabel().setText(de.idrinth.waraddonclient.factory.RemoteRequest.build().getVersion());
        } catch (java.lang.Exception exception) {
            de.idrinth.factory.Logger.build().log(exception.getMessage(), de.idrinth.Logger.levelError);
        }
    }
}