package org.jxch.capital.fx.register;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.XmlUtil;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.jxch.capital.fx.config.FXConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import java.util.Objects;

@Component
public class FXMLLoaderRegister implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {
    public final static String FXML_LOADER_BEAN_NAME_SUFFIX = "FXMLLoader";
    private final ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    private Environment environment;

    @Override
    @SneakyThrows
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        String fxmlResources = Objects.requireNonNull(environment.getProperty(FXConfig.FXML_SCAN, FXConfig.FXML_SCAN_DEFAULT));
        for (Resource resource : resolver.getResources(fxmlResources)) {
            GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
            beanDefinition.setBeanClass(FXMLLoaderFactoryBean.class);
            beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(resource.getURL());
            registry.registerBeanDefinition(getBeanName(resource), beanDefinition);
        }
    }

    @SneakyThrows
    private String getBeanName(@NonNull Resource resource) {
        Document document = XmlUtil.parseXml(FileUtil.readUtf8String(resource.getFile()));
        String controllerClassName = document.getDocumentElement().getAttributes().getNamedItem("fx:controller").getNodeValue();
        return Class.forName(controllerClassName).getSimpleName() + FXML_LOADER_BEAN_NAME_SUFFIX;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

}
