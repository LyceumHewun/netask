package cc.lyceum.netask.config;

import cc.lyceum.netask.common.ResMsg;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author Lyceum
 */
@ControllerAdvice
public class ExceptionHandlerConfiguration {

    @ExceptionHandler(value = Exception.class)
    public ResMsg<Exception> exceptionHandler(Exception e) {
        e.printStackTrace();
        return ResMsg.fail(e.getMessage(), e);
    }
}
