package org.jxch.capital.fx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jxch.capital.fx.util.SpringFXUtil;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

@Component
public class SpringFXApplication extends Application implements CommandLineRunner, ApplicationContextAware, ApplicationListener<ContextClosedEvent> {
    private static Class<?> controllerClazz;
    private ApplicationContext context;

    @Override
    public void start(Stage stage) {
        Scene scene = SpringFXUtil.getControllerScene(controllerClazz);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void run(String... args) {
        launch(args);
    }

    @Override
    public void stop() {
        SpringApplication.exit(context);
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        Platform.exit();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    public static ApplicationContext run(Class<?> springBootConfigClazz, Class<?> controllerClazz, String... args) {
        SpringFXApplication.controllerClazz = controllerClazz;
        return SpringApplication.run(springBootConfigClazz, args);
    }

}
