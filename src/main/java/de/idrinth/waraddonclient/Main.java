package de.idrinth.waraddonclient;

import de.idrinth.waraddonclient.service.FileSystem;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.cli.ParseException;

public final class Main {

    private Main() {
        //not to be used
    }

    public static void main(String[] args) {
        BaseMain main = get(args);
        try {
            main.run();
        } catch (ParserConfigurationException|FileSystem.FileSystemException|IOException|CertificateException|KeyManagementException|KeyStoreException|NoSuchAlgorithmException|URISyntaxException ex) {
            main.error(ex);
            Runtime.getRuntime().exit(0);
        }
    }
    private static BaseMain get(String[] args) {
        return args.length > 0 ? new CliMain(args) : new GuiMain();
    }
}
