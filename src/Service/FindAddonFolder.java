/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

/**
 *
 * @author BJ
 */
public class FindAddonFolder {

    public java.io.File find(String name) {
        String[] path = new String[3];
        path[0] = "interface";
        path[1] = "addons";
        path[2] = name;
        java.io.File folder = new java.io.File("./");
        for (int counter = 0; counter < 3; counter++) {
            folder = findMatch(folder, path[counter]);
            if (folder == null || !folder.exists()) {
                return null;
            }
        }
        return folder;
    }

    protected java.io.File findMatch(java.io.File folder, String search) {
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
