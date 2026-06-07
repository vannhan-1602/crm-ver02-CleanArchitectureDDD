package com.crm.application.common;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

@Component
public class Mediator {
    private final ApplicationContext context;

    public Mediator(ApplicationContext context) {
        this.context = context;
    }

    @SuppressWarnings("unchecked")
    public <TResponse> TResponse send(IRequest<TResponse> request) {
        Map<String, IRequestHandler> handlers = context.getBeansOfType(IRequestHandler.class);

        for (IRequestHandler handler : handlers.values()) {
            Type[] interfaces = handler.getClass().getGenericInterfaces();
            for (Type iface : interfaces) {
                if (iface instanceof ParameterizedType pt) {
                    Type firstArg = pt.getActualTypeArguments()[0];
                    if (firstArg instanceof Class<?> clazz
                            && clazz.isAssignableFrom(request.getClass())) {
                        return (TResponse) handler.handle(request);
                    }
                }
            }
        }
        throw new RuntimeException("No handler found for " + request.getClass().getName());
    }
}