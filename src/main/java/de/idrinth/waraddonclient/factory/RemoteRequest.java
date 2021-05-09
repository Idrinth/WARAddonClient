package de.idrinth.waraddonclient.factory;

import de.idrinth.waraddonclient.model.TrustManager;
import de.idrinth.waraddonclient.service.Request;

public final class RemoteRequest {

    private static de.idrinth.waraddonclient.service.Request instance;

    private RemoteRequest() {
        //this is static only
    }

    public static synchronized Request build() throws Exception {
        if (instance == null) {
            instance = new Request(new TrustManager(Logger.build()), Logger.build());
        }
        return instance;
    }
}
