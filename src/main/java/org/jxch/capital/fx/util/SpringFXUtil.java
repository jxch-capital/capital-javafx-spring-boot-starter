package org.jxch.capital.fx.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import lombok.SneakyThrows;
import org.jxch.capital.fx.bind.PCBindHolder;
import org.jxch.capital.fx.config.FXAutoConfig;
import org.jxch.capital.fx.register.FXMLLoaderRegister;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class SpringFXUtil implements ApplicationContextAware, InitializingBean {
    private static ApplicationContext context;
    private static List<String> stylesheets;

    public static <T> FXMLLoader getFXMLLoader(Class<T> controllerClass) {
        return context.getBean(controllerClass.getSimpleName() + FXMLLoaderRegister.FXML_LOADER_BEAN_NAME_SUFFIX, FXMLLoader.class);
    }

    @SneakyThrows
    public static Scene getControllerScene(Class<?> controllerClass) {
        Scene scene = new Scene(getFXMLLoader(controllerClass).load());
        stylesheets.forEach(css -> scene.getStylesheets().add(css));
        return scene;
    }

    @SneakyThrows
    public static <T> PCBindHolder<T> loadAndGet(Class<T> controllerClass) {
        FXMLLoader fxmlLoader = getFXMLLoader(controllerClass);
        return new PCBindHolder<T>().setParent(fxmlLoader.load()).setController(fxmlLoader.getController());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    @Override
    @SneakyThrows
    public void afterPropertiesSet() {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        FXAutoConfig config = context.getBean(FXAutoConfig.class);
        stylesheets = Arrays.stream(resolver.getResources(config.getCssPath())).map(SpringFXUtil::readStylesheet).toList();
    }

    @SneakyThrows
    public static String readStylesheet(Resource resource) {
        return resource.getURL().toExternalForm();
    }

}
