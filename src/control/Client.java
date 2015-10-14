package control;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class Client {
	private SimpleServer server = new SimpleServer();

    public Client(String url) {
        String charset = "UTF-8";

        try {
            // Initial connection - send the server the client IP and port
            String query = String.format("ip=%s&port=%s",
                    URLEncoder.encode("<my IP>", charset),
                    URLEncoder.encode("<my port>", charset));

            URLConnection connection = new URL(url + "?" + query).openConnection();
            connection.setRequestProperty("Accept-Charset", charset);
            InputStream response = connection.getInputStream();

            //TODO read from input stream
        }
        catch (IOException e) {
            //TODO
        }
    }
}
