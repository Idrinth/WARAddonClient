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
public class Version implements java.lang.Runnable {

    private Service.Request request;
    private javax.swing.JLabel local;
    private javax.swing.JLabel remote;
    public static String version = "1.1.0";

    public Version(Service.Request request, javax.swing.JLabel local, javax.swing.JLabel remote) {
        this.request = request;
        this.local = local;
        this.remote = remote;
        this.local.setText(version);
    }

    public void run() {
        try {
            Thread.sleep(2500);
        } catch (java.lang.InterruptedException exception) {
            System.out.println(exception.getMessage());
        }
        remote.setText(request.getVersion());
    }
}
