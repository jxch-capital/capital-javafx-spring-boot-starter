package org.jxch.capital.fx.platform;

import javafx.application.Platform;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Role;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicReference;

@Component
@Role(BeanDefinition. ROLE_INFRASTRUCTURE)
public class PlatformRunLaterPointcutAdvisor extends StaticMethodMatcherPointcut implements PointcutAdvisor, MethodInterceptor {

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        return method.isAnnotationPresent(PlatformRunLater.class);
    }

    @Override
    public ClassFilter getClassFilter() {
        return ClassFilter.TRUE;
    }

    @Override
    public Object invoke(MethodInvocation invocation) {
        AtomicReference<Object> result = new AtomicReference<>();
        Platform.runLater(() -> {
            try {
                result.set(invocation.proceed());
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });

        return result;
    }

    @Override
    public Pointcut getPointcut() {
        return this;
    }

    @Override
    public Advice getAdvice() {
        return this;
    }

    @Override
    public boolean isPerInstance() {
        return true;
    }

}
