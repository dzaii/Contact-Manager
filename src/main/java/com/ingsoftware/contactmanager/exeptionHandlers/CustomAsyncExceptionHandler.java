package com.ingsoftware.contactmanager.exeptionHandlers;

import lombok.extern.log4j.Log4j2;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

@Log4j2
public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(
            Throwable throwable, Method method, Object... obj) {

        log.error("Exception message - " + throwable.getMessage());
        log.error("Method name - " + method.getName());
        for (Object param : obj) {
            log.error("Parameter value - " + param);
        }
    }
}
