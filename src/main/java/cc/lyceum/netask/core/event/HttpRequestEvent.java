package cc.lyceum.netask.core.event;

import cc.lyceum.netask.util.HttpUtils;
import org.jsoup.Connection;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Lyceum
 */
public class HttpRequestEvent extends Event {

    public static final String COOKIES_VARIABLE = "cookies-variable";

    private String url;
    /**
     * Method default value is GET
     */
    private Connection.Method method = Connection.Method.GET;
    private Map<String, String> data = new HashMap<>();
    private String body;
    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> cookies = new HashMap<>();
    /**
     * default 3000 ms timeout
     */
    private int timeout = 3000;

    private boolean useContextCookies = true;

    public HttpRequestEvent(String id) {
        super(id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void action() {
        // choose cookies
        Map<String, String> cookies = Optional.ofNullable(useContextCookies ? getVariable(COOKIES_VARIABLE) : null)
                .map(obj -> (Map<String, String>) obj)
                .orElse(new HashMap<>());
        cookies.putAll(this.cookies);

        // exec http request
        Connection.Response response = HttpUtils.exec(this.url,
                connection -> connection.method(this.method)
                        .data(this.data)
                        .requestBody(this.body)
                        .headers(this.headers)
                        .cookies(cookies)
                        .timeout(this.timeout));

        // set variable
        setVariable(response);
        setVariable(COOKIES_VARIABLE, response.cookies());
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Connection.Method getMethod() {
        return method;
    }

    public void setMethod(Connection.Method method) {
        this.method = method;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public boolean isUseContextCookies() {
        return useContextCookies;
    }

    public void setUseContextCookies(boolean useContextCookies) {
        this.useContextCookies = useContextCookies;
    }
}
