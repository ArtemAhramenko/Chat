package com.chat.protocols;

import org.apache.commons.net.SocketClient;
import org.apache.commons.net.util.Charsets;
import java.io.*;

public class WhoIs extends SocketClient {

    private final int CAPACITY = 1024;
    private final int DEFAULT_PORT = 43;
    private transient char[] buf = new char[CAPACITY];

    public void connect(String hostname) throws IOException {
        this.connect(hostname, DEFAULT_PORT);
    }

    public String query(boolean longOutput, String username) throws IOException {
        StringBuilder result = new StringBuilder(CAPACITY);

        try (BufferedReader input = new BufferedReader(new InputStreamReader(this.getInputStream(longOutput, username), this.getCharset()))) {
            while (true) {
                int read = input.read(buf, 0, CAPACITY);
                if (read <= 0) {
                    return result.toString();
                }
                result.append(buf, 0, read);
            }
        }
    }

    private InputStream getInputStream(boolean longOutput, String username) throws IOException {
        String buffer = username + "\r\n";
        byte[] encodedQuery = buffer.getBytes(Charsets.toCharset(null).name());
        DataOutputStream output = new DataOutputStream(new BufferedOutputStream(_output_, CAPACITY));
        output.write(encodedQuery, 0, encodedQuery.length);
        output.flush();
        return _input_;
    }
}
