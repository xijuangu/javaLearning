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
    - `@Autowired`  
        作用：用于自动注入依赖的 Bean。
        原理：Spring 容器会扫描带有 `@Autowired` 注解的字段、构造函数或方法，并自动注入匹配的 Bean。  
        实现：通过反射机制在运行时找到带有 `@Autowired` 注解的成员，然后从容器中获取相应的 Bean 并注入。

        ```java
        @Service
        public class MyService {
            @Autowired
            private MyRepository myRepository;  // 其他类的对象
        }
        ```

3. <u>控制反转 (IoC)</u>  
Spring IoC 容器负责管理对象的生命周期和依赖关系，应用程序中的对象通过容器获取依赖，而不是自己创建和管理。
    - `@Component`  
      - 作用：将类标记为 Spring 管理的 Bean。
      - 属性：`@Component(value = "myBean")`，等同于`<bean id = "myBean" class="com.apps.pojo.MyBean" />`
      - 原理：Spring 容器在启动时会扫描类路径中带有 `@Component` 注解的类，并将其实例化、配置和管理。  
      - 实现：通过类路径扫描（`ClassPathScanningCandidateComponentProvider`）找到所有带有 `@Component` 注解的类，并注册为 Bean。

        ```java
        @Component
        public class MyComponent {
        // ...
        }
        ```

4. 自动配置 (Auto Configuration)  
Spring Boot 的自动配置是通过大量的条件注解实现的，自动配置类根据 <u>类路径 (CLASSPATH)</u> 和环境的具体情况进行相应的配置。
    - `@SpringBootApplication`  
        作用：组合注解，包含 `@Configuration`、`@EnableAutoConfiguration` 和 `@ComponentScan`，用于标记主应用程序类。  
        原理：`@EnableAutoConfiguration` 启用 Spring Boot 的自动配置机制，根据类路径中的依赖和应用程序的配置自动配置 Spring 应用。  
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
    - `@Transactional`  
        作用：用于声明事务管理。  
        原理：Spring AOP 在运行时动态代理带有 `@Transactional` 注解的方法，在方法执行前后开始和提交事务。  
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
    - `@Value`  
        作用：用于注入外部化配置中的属性值。  
        原理：Spring 容器在初始化 Bean 时，会解析 `@Value` 注解并从配置源中获取相应的属性值。  
        实现：通过 `PropertySourcesPlaceholderConfigurer` 或 `@ConfigurationProperties` 实现属性注入。

        ```java
        @Component
        public class MyComponent {
            @Value("${my.property}")
            private String myProperty;
        }
        ```

7. 注解解析和处理  
Spring 容器在启动时会扫描应用程序上下文中的所有类，解析注解并进行相应的处理。Spring 使用反射和代理机制来实现这一点。以下是一些关键组件：

    - `ClassPathScanningCandidateComponentProvider`：扫描类路径，找到所有带有特定注解的类。  
    - `AnnotationConfigApplicationContext`：加载带有 `@Configuration` 注解的类，并注册相应的 Bean。  
    - `AutowiredAnnotationBeanPostProcessor`：处理 `@Autowired` 注解，实现依赖注入。  
    - `ConfigurationClassPostProcessor`：处理 `@Configuration` 注解，实现基于 Java 配置的 Bean 定义和注册。

### 其他注解

1. `@Resource`  
    - `@Resource` 注解是 Java EE 提供的注解，用于在 Spring 应用程序中进行依赖注入。它通常用于标记需要注入的依赖，类似于 Spring 提供的 `@Autowired` 注解。
    - @Resource 有两个属性，分别为 name 和 type，name 指定根据 Spring 容器中 bean 的名字来进行注入，type 指定根据 bean 的类型来进行注入。
    - 使用方式:
        - `@Resource` 可以用于字段、`setter` 方法或其他方法上。
        - @Resource 注入时的查找规则：
          - 既不指定 name 属性，也不指定 type 属性：
            默认按字段名或属性名作为 bean 名称进行按名字注入。
            如果找不到符合的 bean，则退回到按类型注入。如果找到多个匹配的类型，则抛出异常。

            ```java
            @Resource
            private MyRepository myRepository;
            ```

            如果 Spring 容器中存在一个名字为 myRepository ( bean name = "myRepository" )的 bean，则注入该 bean。如果没有找到，则按 MyRepository 类型查找唯一匹配的 bean。

            ```xml
            <bean name="myRepository" class="org.xijuangu.src.java.MyRepository"></bean> 
            ```

          - 只指定 name 属性：
            按 name 属性值查找匹配的 bean。如果找不到匹配的 bean，则抛出异常。

            ```java
            @Resource(name = "myRepo")
            private MyRepository myRepository;
            ```

            这段代码会查找名为 myRepo 的 bean，并注入到 myRepository 字段中。

          - 只指定 type 属性：
            按 type 属性值查找匹配的 bean。如果找不到匹配的 bean 或者找到多个匹配的 bean，则抛出异常。

            ```java
            @Resource(type = MyRepository.class)
            private MyRepository myRepository;
            ```

            这段代码会按 MyRepository 类型查找唯一匹配的 bean 并进行注入。

          - 既指定了 name 属性又指定了 type 属性：
            按 name 属性值查找匹配的 bean，并且该 bean 必须是 type 属性指定的类型。如果找不到匹配的 bean 或者类型不符，则抛出异常。

            ```java
            @Resource(name = "myRepo", type = MyRepository.class)
            private MyRepository myRepository;
            ```

            这段代码会查找名为 myRepo 且类型为 MyRepository 的 bean 并进行注入。

2. `@Controller`  
    - `@Controller` 注解是 Spring MVC 提供的注解，用于标记控制器类。这些控制器类负责处理用户请求，并返回视图或数据。
    - 使用方式:
        - `@Controller` 注解用于类上，表明该类是一个控制器类。
        - 在控制器类中，通常会结合 `@RequestMapping` 注解来映射 HTTP 请求。

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

3. `@Bean`
    - `@Bean` 注解用于 Spring 的 Java 配置中，***表示一个方法返回一个 Spring 管理的 Bean***。
    - `@Configuration` 用于定义配置类，配置类在 Spring 应用上下文中用于替代传统的 XML 配置文件，以 Java 类的形式提供 Bean 定义和配置元数据。
    - 使用方式:
        - `@Bean` 注解用于方法上，方法的 ***返回值会注册为 Spring 的 Bean。***
        - 通常与 `@Configuration` 注解一起使用，用于配置类中。

    ```java
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;

    @Configuration
    public class AppConfig {

        @Bean
        public DataSource dataSource() {
            DataSource dataSource = new DataSource();
            dataSource.setUrl("jdbc:mysql://localhost:3306/mydb");
            dataSource.setUsername("user");
            dataSource.setPassword("password");
            return dataSource;
        }

        @Bean
        public MyService myService() {
            return new MyServiceImpl(dataSource());
        }
    }
    ```

    对应的xml配置文件如下：

    ```xml
    <!-- src/main/resources/applicationContext.xml -->
    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://www.springframework.org/schema/beans
            http://www.springframework.org/schema/beans/spring-beans.xsd">

        <!-- 定义 DataSource Bean -->
        <bean id="dataSource" class="com.example.DataSource">
            <!-- property标签等价于上面调用setter方法 -->
            <property name="url" value="jdbc:mysql://localhost:3306/mydb"/>
            <property name="username" value="user"/>
            <property name="password" value="password"/>
        </bean>

        <!-- 定义 MyService Bean，注入 DataSource Bean -->
        <bean id="myService" class="com.example.MyServiceImpl">
            <constructor-arg ref="dataSource"/>
        </bean>

    </beans>
    ```

4. `@ComponentScan`
   `@ComponentScan` 注解用于自动扫描指定包及其子包中的所有类，并根据注解（例如 `@Component`, `@Service`, `@Repository`, `@Controller` 等）将它们注册为 Spring 容器中的 bean。一般和 `@Configuration` 一起使用，可以将配置逻辑集中在一个地方，简化了 Spring 应用程序的配置，只需要一个配置类，就可以配置组件扫描和其他 bean 的定义。但在SpringBoot中，`@SpringBootApplication` 包含了 `@ComponentScan`，因此不需要再在其他地方使用。

## AOP

注解：

1. @Aspect，不能单独使用，需要加上@Component
2. @Pointcut
3. @Before
4. @After
5. @Around
6. @AfterReturning
7. @AfterThrowing

占位符：

1. `*`（星号）：
    - 返回类型：`*` 表示任意返回类型。例如，`execution(* com.abc.service.*.many*(..))` 中第一个 `*`。
    - 类名：`*` 表示任意方法名。例如，`execution(* com.xyz.service..*.*(..))` 中第二个 `*`。
    - 方法名：`*` 表示任意方法名。例如，`execution(* com.xyz.service..*.*(..))` 中第三个 `*`。
2. `..`（双点）：
    - 包名：表示当前包及其所有子包。例如，`com.xyz.service..` 表示 `com.xyz.service` 包及其所有子包。
    - 参数：表示任意数量和类型的参数。例如，`(..)` 表示任意参数。
3. `()`（圆括号）：
    - 参数列表：用于指定方法的参数列表。例如，`(..)` 表示任意参数。

表达式：

1. execute：用于匹配方法执行。

   ```java
   // 匹配 com.example.service 包及其子包中的所有方法
    @Pointcut("execution(* com.example.service..*.*(..))")
    public void allServiceMethods() {}
    ```

2. within：用于匹配特定类型中的所有方法 `@Pointcut("within(com.example.service..*)")`
3. this：用于匹配当前 AOP 代理对象的类型，只有代理对象的类型和声明的类型相同才会执行。
   - 目标对象：这是我们在应用程序中实际定义的对象，例如某个业务逻辑类的实例。
   - 代理对象：这是 AOP 框架生成的对象，它包装了目标对象，并在调用目标对象的方法时插入额外的逻辑（例如日志记录、事务管理等）。
   当我们应用 AOP 切面到某个目标对象时，AOP 框架会创建一个代理对象。这个代理对象在调用目标对象的方法之前和之后执行切面逻辑。
   - this 表达式的匹配逻辑
       this(type) 表达式检查当前 AOP 代理对象是否是指定的类型。如果代理对象的类型与指定的类型匹配，那么切入点会被激活。
   - 假设我们有一个接口 MyService 和它的实现类 MyServiceImpl：

        ```java
        public interface MyService {
            void performTask();
        }

        public class MyServiceImpl implements MyService {
            @Override
            public void performTask() {
                System.out.println("Performing task");
            }
        }
        ```

        我们定义一个切面来拦截 MyService 接口的所有方法调用：

        ```java
        @Aspect
        @Component
        public class MyAspect {

            @Pointcut("this(com.example.MyService)")
            public void proxyImplementsMyService() {}

            @Before("proxyImplementsMyService()")
            public void beforeProxyMethods() {
                System.out.println("Before method in proxy implementing MyService");
            }
        }
        ```

        当 Spring AOP 创建代理对象时，有两种主要的代理方式：
        - JDK 动态代理：如果目标对象实现了一个或多个接口，Spring AOP 会默认使用 JDK 动态代理。这种代理方式生成的代理对象实现了与目标对象相同的接口。
        - CGLIB 代理：如果目标对象没有实现任何接口，Spring AOP 会使用 CGLIB 创建一个子类代理。这种代理方式生成的代理对象是目标对象类的子类。

        ```java
        MyService myService = new MyServiceImpl();  // 目标对象
        MyService proxy = (MyService) ProxyFactory.getProxy(myService);  // 代理对象
        //MyService myService = new MyServiceImpl(); 表示目标对象 myService 是 MyService 接口的实现类的对象。
        //ProxyFactory.getProxy(myService) 返回了一个 Object 类型的实现了 MyService 接口的代理对象，并可以被强制转换为MyService类型（被视为实现类），也即代理对象是 JDK 动态代理方式创建的。

        MyServiceImpl myService = new MyServiceImpl();  // 目标对象
        MyServiceImpl proxy = (MyServiceImpl) ProxyFactory.getProxy(myService);  // 代理对象
        //MyServiceImpl myService = new MyServiceImpl(); 表示目标对象 myService 是一个具体类的对象，而不是实现了 MyService 接口的实现类的对象。
        //ProxyFactory.getProxy(myService) 返回了一个 Object 类型的 MyServiceImpl 的子类代理对象，并可以被强制转换为 MyServiceImpl 类型，但不能转换为 MyService 类型（被视为普通类），也即代理对象是 CDLIB 代理方式创建的。
        ```

4. target：用于匹配目标对象为指定的类型。如`target(com.xyz.service.AccountService)`，目标对象为`AccountService`类型的会被代理
5. args：用于匹配方法参数的类型。

    ```java
    // 匹配参数类型为 String 的所有方法
    @Pointcut("args(java.lang.String)")
    public void methodsWithStringArgument() {}
    ```

6. @target：用于匹配目标对象的类具有指定注解。

   ```java
   // 匹配目标对象的类上有 @Repository 注解的所有方法
   @Pointcut("@target(org.springframework.stereotype.Repository)")
   public void targetWithRepositoryAnnotation() {}
   ```

7. @within：用于匹配具有指定注解的类中的所有方法。

    ```java
    // 匹配所有标有 @Service 注解的类中的所有方法
    @Pointcut("@within(org.springframework.stereotype.Service)")
    public void withinServiceAnnotatedClass() {}
    ```

8. @annotation：用于匹配具有指定注解的方法。

    ```java
    // 匹配所有标有 @Transactional 注解的方法
    @Pointcut("@annotation(org.springframework.transaction.annotation.Transactional)")
    public void transactionalMethods() {}
    ```

9. @args：用于匹配方法参数的运行时类型具有指定注解的方法。

    ```java
    // 匹配参数运行时类型具有 @Validated 注解的方法
    @Pointcut("@args(org.springframework.validation.annotation.Validated)")
    public void methodsWithValidatedArgs() {}
    ```

以下为@Around、@Before等注解的使用样例：

```java
@Aspect
public class AdviceTest {
    @Around("execution(* com.abc.service.*.many*(..))")
    public Object process(ProceedingJoinPoint point) throws Throwable {
        System.out.println("@Around：执行目标方法之前...");
        //访问目标方法的参数：
        Object[] args = point.getArgs();
        if (args != null && args.length > 0 && args[0].getClass() == String.class) {
            args[0] = "改变后的参数1";
        }
        //用改变后的参数执行目标方法
        Object returnValue = point.proceed(args);
        System.out.println("@Around：执行目标方法之后...");
        System.out.println("@Around：被织入的目标对象为：" + point.getTarget());
        return "原返回值：" + returnValue + "，这是返回结果的后缀";
    }
    
    @Before("execution(* com.abc.service.*.many*(..))")
    public void permissionCheck(JoinPoint point) {
        System.out.println("@Before：模拟权限检查...");
        System.out.println("@Before：目标方法为：" + 
                point.getSignature().getDeclaringTypeName() + 
                "." + point.getSignature().getName());
        System.out.println("@Before：参数为：" + Arrays.toString(point.getArgs()));
        System.out.println("@Before：被织入的目标对象为：" + point.getTarget());
    }
    
    @AfterReturning(pointcut="execution(* com.abc.service.*.many*(..))", 
        returning="returnValue")    // returning属性指定一个参数名以保存目标方法的返回值
    public void log(JoinPoint point, Object returnValue) {
        System.out.println("@AfterReturning：模拟日志记录功能...");
        System.out.println("@AfterReturning：目标方法为：" + 
                point.getSignature().getDeclaringTypeName() + 
                "." + point.getSignature().getName());
        System.out.println("@AfterReturning：参数为：" + 
                Arrays.toString(point.getArgs()));
        System.out.println("@AfterReturning：返回值为：" + returnValue);
        System.out.println("@AfterReturning：被织入的目标对象为：" + point.getTarget());
        
    }
    
    @After("execution(* com.abc.service.*.many*(..))")
    public void releaseResource(JoinPoint point) {
        System.out.println("@After：模拟释放资源...");
        System.out.println("@After：目标方法为：" + 
                point.getSignature().getDeclaringTypeName() + 
                "." + point.getSignature().getName());
        System.out.println("@After：参数为：" + Arrays.toString(point.getArgs()));
        System.out.println("@After：被织入的目标对象为：" + point.getTarget());
    }

    @AfterThrowing(
        pointcut = "execution(* com.example.service.MyService.*(..))",
        throwing = "error"      // throwing属性指定一个参数名以保存目标方法抛出的异常
    )
    public void afterThrowingAdvice(JoinPoint joinPoint, Throwable error) {
        System.out.println("After throwing advice. Method: " + joinPoint.getSignature());
        System.out.println("Exception: " + error);
    }
}
```

## Spring Boot起步依赖原理

首先明确，起步依赖中引用的是项目的坐标，坐标中定义了版本信息。对项目的依赖可以继承。  

`<parent>`，其中是父工程  
`spring-boot-starter-parent`，其中定义了各种技术的版本信息，组合了一套最优搭配的技术版本。在各种starter中，定义了完成该功能需要的坐标合集，其中大部分信息来自父工程，其余的父工程中没有的就要自己写版本号，需要自己注意版本冲突。  
`spring-boot-dependencies`是`spring-boot-starter-parent`的parent，它没有再上一级的parent。  
`<dependencyManagement>`，版本锁定，在父工程内定义的带版本信息的坐标，子工程同项目不需要再写版本信息  
`<properties>`，属性信息，是一堆技术版本信息  

`sping-boot-stater-web`，它没有版本锁定，每一个引用的坐标内都有版本信息  
`<dependencies>`，其中是该项目依赖的坐标  

## 配置文件分类

SpringBoot是基于约定的，所以很多配置都有默认值。如果想使用自己的配置替换默认配置的话，就需要使用`application.properties`或`application.yml(或.yaml)`进行配置，文件名必须是application，位置位于resources目录下，三种文件可以同时生效，优先级从高到底为`.properties` > `.yml` > `.yaml`。  

### properties

```properties
server.port=8081
```

### yml

- 最简洁，以数据为核心
- 大小写敏感
- 使用缩进表示层级关系，缩进不允许使用Tab，只允许使用空格，避免不同系统对应的空格数不同的问题。缩进的空格数目不重要，只要同层级元素左侧对齐即可
- `#`表示注释

```yml
# 对象（map），键值对的集合
server:
  port: 8081    # 注意8081前面有个空格

# 行内写法（少），也要注意两处空格
server: {port: 8081}


# 数组
address: 
    - 北京市
    - 上海市

# 行内写法（少）
address: [北京市,上海市]


# 纯量，单个不可再分的值
msg1: 'hello \n world'    # 单引号不识别转义字符
msg2: "hello \n world"    # 双引号识别转义字符

# 参数引用
msg3: ${msg1}
```

## 读取配置内容

```java
// 读取对象
@Value("${server.port}")
private String port;

// 读取数组
@Value("${address[0]}")
private String address1;
@Value("${address[1]}")
private String address2;

// 读取纯量
@Value("${msg1}")
private String msg1;


// Enviroment对象方式
@Autowired
private Enviroment env;

System.out.println(env.getProperty("server.port"));

```

`@ConfiurationProperties`用于将配置内容和对象绑定  

```yml
server:
  port1: 8081
  port2: 8082
  address: 
    - 北京市
    - 上海市
```

```java
@Component      // 注册为Bean被spring管理
@ConfigurationProperties(prefix = "server")     // prefix表示配置文件中的前缀
public class ServerConfig {
    priavte String port1;       // 属性名和配置文件中的key一致，会自动映射
    private String port2;
    priavte String[] address;   // 数组也一样获取
    // ...
    // 省略getter和setter
}
