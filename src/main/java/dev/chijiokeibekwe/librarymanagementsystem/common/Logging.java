package dev.chijiokeibekwe.librarymanagementsystem.common;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

@Slf4j
@Aspect
@Component
public class Logging {

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    private void restControllerMethods() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    private void postControllerMethods() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    private void putControllerMethods() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.ExceptionHandler)")
    private void exceptionHandlerMethods() {
    }

    @Around(value = "postControllerMethods() || putControllerMethods()")
    public Object trackExecutionTimeOfApiWriteOperations(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Instant start = Instant.now();
        Object result = joinPoint.proceed();
        Instant end = Instant.now();
        log.info("API to {} took {} ms", methodName, ChronoUnit.MILLIS.between(start, end));
        return result;
    }

    @Before(value = "restControllerMethods()")
    public void logRestControllerMethodCall(JoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        log.info("Executing REST controller method: " + methodName);
    }

    @Before(value = "exceptionHandlerMethods()")
    public void logException(JoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();

        if(args[0] instanceof MethodArgumentNotValidException e) {
            log.error(e.getBindingResult().getAllErrors().get(0).getDefaultMessage(), e);
        } else if (args[0] instanceof ConstraintViolationException e) {
            log.error((new ArrayList<>(e.getConstraintViolations())).get(0).getMessage(), e);
        } else {
            log.error(((Exception)args[0]).getMessage(), args[0]);
        }
    }
}
