package org.jxch.capital.fx;

import cn.hutool.extra.spring.SpringUtil;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jxch.capital.fx.processor.StagePostProcessor;
import org.jxch.capital.fx.util.SpringFXUtil;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;

@Component
public class SpringFXApplication extends Application implements CommandLineRunner, ApplicationContextAware, ApplicationListener<ContextClosedEvent> {
    private static Class<?> controllerClazz;
    private static ApplicationContext context;

    @Override
    public void start(Stage stage) {
        Scene scene = SpringFXUtil.getControllerScene(controllerClazz);
        stage.setScene(scene);
        stage.setTitle(SpringUtil.getApplicationName());

        context.getBeansOfType(StagePostProcessor.class).values().stream()
                .sorted(new AnnotationAwareOrderComparator())
                .forEach(processor -> processor.postProcess(stage));

        stage.show();
    }

    @Override
    public void run(String... args) {
        launch(args);
    }

    @Override
    public void stop() {
        if (Objects.nonNull(context)) {
            SpringApplication.exit(context);
        }
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        Platform.exit();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static ApplicationContext run(Class<?> springBootConfigClazz, Class<?> controllerClazz, String... args) {
        SpringFXApplication.controllerClazz = controllerClazz;
        SpringApplication springApplication = new SpringApplication(springBootConfigClazz);
        springApplication.setSources(Set.of(SpringFXApplication.class.getPackageName(), springBootConfigClazz.getPackageName(), controllerClazz.getPackageName()));
        return springApplication.run(args);
    }

}
