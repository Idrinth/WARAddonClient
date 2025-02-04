package de.idrinth.waraddonclient.model;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public class BasicResponse implements AutoCloseable {
    public int status;
    public String message;
    public InputStream content;

    public BasicResponse(int status, String message, InputStream content, boolean isGzip) throws IOException {
        this.status = status;
        this.message = message;
        this.content = new BufferedInputStream(isGzip ? new GZIPInputStream(content) : content);
    }

    @Override
    public void close() throws Exception {
        content.close();
    }
}
