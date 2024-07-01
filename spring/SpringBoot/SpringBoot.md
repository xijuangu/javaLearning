# Spring Boot

> 全篇带下划线的为 Spring IoC 部分中介绍过的内容

## 注解

### 基本概念

在 Spring Boot 中，注解（Annotations）用于简化配置和管理组件，使得开发者可以通过声明性的方式配置和控制应用程序的行为，而无需编写大量的配置文件或样板代码。

1. 元数据：注解提供了一种将元数据（metadata）嵌入到源代码中的方式。元数据是关于程序元素（如类、方法、字段）的信息，可以在编译时或运行时被读取和处理。
2. 声明式编程：注解使得编程更加声明式，通过标注注解，可以清晰地描述组件的角色和行为，而不需要在代码中显式地编写实现逻辑。
3. 配置简化：使用注解可以大大简化 Spring 应用程序的配置。例如，通过注解可以直接声明 Bean、配置依赖注入、定义事务等，而不需要冗长的 XML 配置文件。

### 原理

1. 注解处理器  
Java 的注解处理器在编译时和运行时解析注解。Spring 使用 <u>**反射**</u> 和 <u>**代理**</u> 机制在运行时处理注解，并相应地配置和管理应用程序组件。
2. <u>依赖注入 (DI)</u>  
Spring 的核心特性是依赖注入，通过注解可以声明 Bean 之间的依赖关系，Spring 容器会自动管理这些依赖关系。  
    - @Autowired  
        作用：用于自动注入依赖的 Bean。
        原理：Spring 容器会扫描带有 @Autowired 注解的字段、构造函数或方法，并自动注入匹配的 Bean。  
        实现：通过反射机制在运行时找到带有 @Autowired 注解的成员，然后从容器中获取相应的 Bean 并注入。

        ```java
        @Service
        public class MyService {
            @Autowired
            private MyRepository myRepository;  // 其他类的对象
        }
        ```

3. <u>控制反转 (IoC)</u>  
Spring IoC 容器负责管理对象的生命周期和依赖关系，应用程序中的对象通过容器获取依赖，而不是自己创建和管理。
    - @Component  
        作用：将类标记为 Spring 管理的 Bean。  
        原理：Spring 容器在启动时会扫描类路径中带有 @Component 注解的类，并将其实例化、配置和管理。  
        实现：通过类路径扫描（ClassPathScanningCandidateComponentProvider）找到所有带有 @Component 注解的类，并注册为 Bean。

        ```java
        @Component
        public class MyComponent {
        // ...
        }
        ```

4. 自动配置 (Auto Configuration)  
Spring Boot 的自动配置是通过大量的条件注解实现的，自动配置类根据 <u>类路径 (CLASSPATH)</u> 和环境的具体情况进行相应的配置。
    - @SpringBootApplication  
        作用：组合注解，包含 @Configuration、@EnableAutoConfiguration 和 @ComponentScan，用于标记主应用程序类。  
        原理：@EnableAutoConfiguration 启用 Spring Boot 的自动配置机制，根据类路径中的依赖和应用程序的配置自动配置 Spring 应用。  
        实现：在启动时，Spring Boot 会扫描类路径中的自动配置类（META-INF/spring.factories），并根据条件注解决定是否加载这些配置类。

        ```java
        @SpringBootApplication
        public class MyApplication {
            public static void main(String[] args) {
                SpringApplication.run(MyApplication.class, args);
            }
        }
        ```

5. <u>面向切面编程 (Aspect-Oriented Programming, AOP)</u>  
AOP 允许通过注解声明切面和切点，在方法执行前后添加额外的行为（如事务管理、日志记录）。
    - @Transactional  
        作用：用于声明事务管理。  
        原理：Spring AOP 在运行时动态代理带有 @Transactional 注解的方法，在方法执行前后开始和提交事务。  
        实现：通过代理模式（JDK 动态代理或 CGLIB）在方法调用前后插入事务管理逻辑。

        ```java
        @Service
        public class MyService {
            @Transactional
            public void performTransactionalOperation() {
                // ...
            }
        }
        ```

6. 属性注入（Property Injection）  
Spring 支持将外部化配置（如 properties 文件、环境变量）注入到 Bean 中。
    - @Value  
        作用：用于注入外部化配置中的属性值。  
        原理：Spring 容器在初始化 Bean 时，会解析 @Value 注解并从配置源中获取相应的属性值。  
        实现：通过 PropertySourcesPlaceholderConfigurer 或 @ConfigurationProperties 实现属性注入。

        ```java
        @Component
        public class MyComponent {
            @Value("${my.property}")
            private String myProperty;
        }
        ```

7. 注解解析和处理  
Spring 容器在启动时会扫描应用程序上下文中的所有类，解析注解并进行相应的处理。Spring 使用反射和代理机制来实现这一点。以下是一些关键组件：

    - ClassPathScanningCandidateComponentProvider：扫描类路径，找到所有带有特定注解的类。  
    - AnnotationConfigApplicationContext：加载带有 @Configuration 注解的类，并注册相应的 Bean。  
    - AutowiredAnnotationBeanPostProcessor：处理 @Autowired 注解，实现依赖注入。  
    - ConfigurationClassPostProcessor：处理 @Configuration 注解，实现基于 Java 配置的 Bean 定义和注册。

### 其他注解

1. @Resource  
    - @Resource 注解是 Java EE 提供的注解，用于在 Spring 应用程序中进行依赖注入。它通常用于标记需要注入的依赖，类似于 Spring 提供的 @Autowired 注解。  
    - 使用方式:
        - @Resource 可以用于字段、setter 方法或其他方法上。
        - 默认情况下，@Resource 注解根据名称进行匹配，如果找不到匹配的名称，再根据类型进行匹配。

    ```java
    import javax.annotation.Resource;

    @Service
    public class MyService {
        @Resource(name = "myRepository")
        private MyRepository myRepository;

        // Setter 注入方式
        @Resource
        public void setMyRepository(MyRepository myRepository) {
            this.myRepository = myRepository;
        }

        // 方法注入方式
        @Resource
        public void injectRepository(MyRepository myRepository) {
            this.myRepository = myRepository;
        }
    }
    ```

2. @Controller  
    - @Controller 注解是 Spring MVC 提供的注解，用于标记控制器类。这些控制器类负责处理用户请求，并返回视图或数据。
    - 使用方式:
        - @Controller 注解用于类上，表明该类是一个控制器类。
        - 在控制器类中，通常会结合 @RequestMapping 注解来映射 HTTP 请求。

    ```java
    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.ui.Model;

    @Controller
    @RequestMapping("/greetings")
    public class GreetingController {

        @GetMapping("/hello")
        public String sayHello(Model model) {
            model.addAttribute("message", "Hello, World!");
            return "hello";
        }
    }
    ```

3. @Bean  
    - @Bean 注解用于 Spring 的 Java 配置中，表示一个方法返回一个 Spring 管理的 Bean。该注解通常用于配置类中，用于定义和配置应用程序上下文中的 Bean。
    - 使用方式:
        - @Bean 注解用于方法上，方法的返回值会注册为 Spring 的 Bean。
        - 通常与 @Configuration 注解一起使用，用于配置类中。

    ```java
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;

    @Configuration
    public class AppConfig {

        @Bean
        public MyService myService() {
            return new MyServiceImpl();
        }

        @Bean
        public MyRepository myRepository() {
            return new MyRepositoryImpl();
        }
    }
    ```
