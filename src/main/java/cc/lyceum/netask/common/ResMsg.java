package cc.lyceum.netask.common;

/**
 * @author Lyceum
 */
public class ResMsg<T> {

    public static final int SUCCESS_CODE = 200;
    public static final int FAIL_CODE = 0;

    private final int code;
    private final String message;
    private final T data;

    public ResMsg(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public static <T> ResMsg<T> success(String message, T data) {
        return new ResMsg<>(SUCCESS_CODE, message, data);
    }

    public static <T> ResMsg<T> success(String message) {
        return new ResMsg<>(SUCCESS_CODE, message, null);
    }

    public static <T> ResMsg<T> success(T data) {
        return new ResMsg<>(SUCCESS_CODE, null, data);
    }

    public static <T> ResMsg<T> success() {
        return new ResMsg<>(SUCCESS_CODE, null, null);
    }

    public static <T> ResMsg<T> fail(String message, T data) {
        return new ResMsg<>(FAIL_CODE, message, data);
    }

    public static <T> ResMsg<T> fail(String message) {
        return new ResMsg<>(FAIL_CODE, message, null);
    }

    public static <T> ResMsg<T> fail(T data) {
        return new ResMsg<>(FAIL_CODE, null, data);
    }

    public static <T> ResMsg<T> fail() {
        return new ResMsg<>(FAIL_CODE, null, null);
    }

    public static <T> ResMsg<T> judge(boolean cond) {
        return cond ? success() : fail();
    }

    public static <T> ResMsg<T> create(int code, String message, T data) {
        return new ResMsg<>(code, message, data);
    }

    public static <T> ResMsg<T> create(int code, String message) {
        return new ResMsg<>(code, message, null);
    }

    public static <T> ResMsg<T> create(int code, T data) {
        return new ResMsg<>(code, null, data);
    }
}
