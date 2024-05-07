# capital-javafx-spring-boot-starter
javafx与springboot的自动集成


## 代码示例
0. 导入依赖，仓库地址：https://central.sonatype.com/artifact/io.github.jxch/capital-javafx-spring-boot-starter
```xml
<dependency>
    <groupId>io.github.jxch</groupId>
    <artifactId>capital-javafx-spring-boot-starter</artifactId>
    <version>${capital-javafx.version}</version>
</dependency>
```
1. 定义主界面的FXML文件，并放置于resources文件夹下（同时支持自动加载css文件，默认资源路径：`/fx`）
```xml
<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.control.*?>
<?import javafx.scene.layout.*?>

<VBox xmlns="http://javafx.com/javafx"
      xmlns:fx="http://javafx.com/fxml"
      fx:controller="org.jxch.capital.fx.test.controller.ControllerTest"
      prefHeight="1200.0" prefWidth="1600.0">

    <Label>test</Label>
</VBox>
```
2. 定义控制类（由Spring管理）
```java
@Component
public class ControllerTest {
}
```
3. 启动JAVA FX程序（传入SpringBoot启动类和主界面控制类）
```java
@SpringBootApplication
public class FXAppTest {
    public static void main(String[] args) {
        SpringFXApplication.run(FXAppTest.class, ControllerTest.class, args);
    }
}
```

---
## 功能点
* `SpringFXUtil.getControllerScene(Class<?> controllerClass)` 方法获取携带CSS设置的Scene
* `SpringFXUtil.getFXMLLoader(Class<T> controllerClass)` 方法获取 `FXMLLoader`
  * `FXMLLoader::load` 方法获取 `Parent` 节点
  * `FXMLLoader::getController` 方法获取 `controller` 控制类对象（Controller类必须由Spring管理）
* `@PlatformRunLater` 标注的方法将自动在 `Platform.runLater(() -> {})` 中执行

---
## 注意事项
* `Controller` 类建议设为单例，因为FXML的属性注入操作的是原始对象，`Controller`的原型Bean会自动生成一个新的原始对象，缺失了FXML属性注入的生命周期
* `FXMLLoader` 是原型的，这意味着如果`Controller`是单例的，那么它和fxml文件的关系将是一对多的，可以在一个`Controller`中管理所有与之对应的fxml页面
* 如果确实需要 `Controller` 的原型模式，请不要直接使用Spring默认的方式获取Bean，因为它缺失了FXML属性注入的步骤，请采用以下方式：
  * 使用 `SpringFXUtil.loadAndGet(Class<T> controllerClass)` 方法加载并获取获取 `PCBindHolder` 对象
  * 通过 `PCBindHolder::parent` 获取新页面的 `Parent` 对象
  * 通过 `PCBindHolder::controller` 方法获取新页面与之对应的 `Controller` 对象

