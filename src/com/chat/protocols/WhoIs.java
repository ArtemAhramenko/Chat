package com.chat.protocols;

import org.apache.commons.net.SocketClient;
import org.apache.commons.net.util.Charsets;
import java.io.*;
import java.nio.charset.Charset;

public class WhoIs extends SocketClient {

    private final int CAPACITY = 1024;
    private final int DEFAULT_PORT = 43;

    public void connect(String hostname) throws IOException {
        this.connect(hostname, DEFAULT_PORT);
    }

    public String query(String username) throws IOException {
        StringBuilder result = new StringBuilder(CAPACITY);
        char[] buf = new char[CAPACITY];
        try (BufferedReader input = new BufferedReader(new InputStreamReader(getInputStream(username), Charset.defaultCharset()))) {
            while (true) {
                int read = input.read(buf, 0, CAPACITY);
                if (read <= 0) {
                    return result.toString();
                }
                result.append(buf, 0, read);
            }
        }
    }

    private InputStream getInputStream(String username) throws IOException {
        String buffer = username + "\r\n";
        byte[] encodedQuery = buffer.getBytes(Charsets.toCharset(null).name());
        DataOutputStream output = new DataOutputStream(new BufferedOutputStream(this._output_, CAPACITY));
        output.write(encodedQuery, 0, encodedQuery.length);
        output.flush();
        return this._input_;
    }
}
