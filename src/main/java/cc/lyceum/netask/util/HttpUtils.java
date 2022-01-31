package cc.lyceum.netask.util;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author Lyceum
 */
public class HttpUtils {

    /**
     * maximum number of reconnects
     */
    public static final int DEFAULT_MAXIMUM_RECONNECTS = 2;

    public static Connection connect(String url) {
        return Jsoup.connect(url)
                .ignoreHttpErrors(true)
                .ignoreContentType(true);
    }

    public static Connection.Response exec(String url) {
        return exec(url, null);
    }

    public static Connection.Response exec(String url, @Nullable Consumer<Connection> consumer) {
        Assert.hasText(url, "argument [url] must have length");
        Connection connect = connect(url);
        if (Objects.nonNull(consumer))
            consumer.accept(connect);
        return exec(connect);
    }

    public static Connection.Response exec(Connection connection) {
        return exec(connection, DEFAULT_MAXIMUM_RECONNECTS);
    }

    public static Connection.Response exec(Connection connection, final int maximumReconnects) {
        for (int i = 0; ; i++) {
            try {
                return connection.execute();
            } catch (SocketTimeoutException ex) {
                if (i < maximumReconnects)
                    continue;
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static String execRespBody(String url) {
        return execRespBody(url, null);
    }

    public static String execRespBody(String url, @Nullable Consumer<Connection> consumer) {
        return exec(url, consumer)
                .body();
    }

    public static String getRespBody(String url, String... data) {
        return execRespBody(url, connection -> connection.data(data));
    }

    public static String postRespBody(String url, String... data) {
        return execRespBody(url, connection -> connection
                .method(Connection.Method.POST)
                .data(data));
    }

    public static String postJsonRespBody(String url, String json) {
        return execRespBody(url, connection -> connection
                .method(Connection.Method.POST)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .requestBody(json));
    }
}
