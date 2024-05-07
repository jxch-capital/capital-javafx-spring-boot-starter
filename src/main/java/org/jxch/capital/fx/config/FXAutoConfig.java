package org.jxch.capital.fx.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ComponentScan("org.jxch.capital.fx")
public class FXAutoConfig {
    public final static String FXML_SCAN = "capital.fx.fxml-scan";
    public final static String FXML_SCAN_DEFAULT = "/fx/**/*.fxml";
    @Value("${" + FXML_SCAN + ":" + FXML_SCAN_DEFAULT + "}")
    private String fxmlPath;
    @Value("${capital.fx.css-scan:/fx/**/*.css}")
    private String cssPath;

}
