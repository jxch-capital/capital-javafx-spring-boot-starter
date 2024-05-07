package org.jxch.capital.fx.register;

import javafx.fxml.FXMLLoader;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.net.URL;

public class FXMLLoaderFactoryBean implements FactoryBean<FXMLLoader> {
    private final URL fxml;
    @Autowired
    private ApplicationContext context;

    public FXMLLoaderFactoryBean(URL fxml) {
        this.fxml = fxml;
    }

    @Override
    public FXMLLoader getObject() {
        FXMLLoader fxmlLoader = new FXMLLoader(fxml);
        fxmlLoader.setControllerFactory(clazz -> AopUtils.isAopProxy(context.getBean(clazz)) ?
                AopProxyUtils.getSingletonTarget(context.getBean(clazz)) : context.getBean(clazz));
        return fxmlLoader;
    }

    @Override
    public Class<?> getObjectType() {
        return FXMLLoader.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

}
