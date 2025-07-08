# ✅ Java `record` 全面整理（适用于 Java 14+）

## 一、什么是 `record`？

Java `record` 是一种专门为 **不可变数据载体类** 设计的简洁语法结构。它自动生成构造器、getter、`equals()`、`hashCode()`、`toString()` 等常规方法。

```java
public record Person(String name, int age) {
    // 不能定义实例字段（除非是静态字段）
    // private String address;  (×)
    // public static final String address = "On the Earth";	 (√)
}
```

等价于传统 Java 类中冗长的样板代码：

```java
// record 默认是 final，不能被继承
public final class Person {
    
    // 字段都是 private final，不能修改
    private final String name;
    private final int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String name() {
        return name;
    }

    public int age() {
        return age;
    }

    @Override
    public boolean equals(Object o) { ... }

    @Override
    public int hashCode() { ... }

    @Override
    public String toString() { ... }
}
```



------

## 二、`record` 的语法与结构

### ✅ 基本语法

```java
public record 类名(字段列表) {
    // 可选方法、构造器
}
```

示例：

```java
public record Book(String title, double price) {}
```

这将自动生成：

- 所有字段的 `private final` 属性
- 构造器：`public Book(String title, double price)`，另外，`record` 有专属的紧凑型构造器
- Getter：`title()` 和 `price()`，注意不是“`getTitle()`”
- `equals()`、`hashCode()`、`toString()` 方法

------

## 三、使用规则与限制

| 项目                | 是否允许 | 说明                                                   |
| ------------------- | -------- | ------------------------------------------------------ |
| 自定义构造器        | ✅        | 支持多种形式                                           |
| 空构造器            | ❌        | 不支持无参构造器（因为字段必须赋值）                   |
| 可变非 `final` 字段 | ❌        | 所有字段默认 `private final`，不可修改                 |
| 成员变量            | ❌        | 不允许添加实例字段（非  `static`）                     |
| 实现接口            | ✅        | 支持接口继承                                           |
| 继承类              | ❌        | `record` 是隐式继承 `java.lang.Record`，不能继承其他类 |

------

## 四、构造器的三种形式

### ✅ 1. 紧凑构造器（Compact Constructor）

常用于字段校验，紧凑构造器会给字段自动赋值，不需要 `this.name = name`：

```java
public record Person(String name, int age) {
    public Person {
        if (age < 0) {
            throw new IllegalArgumentException("Age must be positive");
        }
    }
}
```

------

### ✅ 2. 显式完整构造器（Canonical Constructor）

手动列出参数和赋值：

```java
public record Person(String name, int age) {
    public Person(String name, int age) {
        this.name = name == null ? "Unknown" : name;
        this.age = age;
    }
}
```

------

### ✅ 3. 额外构造器（Custom Constructor）

重载构造方法，允许不同的构造方式：

```java
public record Person(String name, int age) {
    public Person(String name, int age) {
        this.name = name == null ? "Unknown" : name;
        this.age = age;
    }
    
    public Person(String name) {
        this(name, 0);
    }
}
```

------

## 五、定义方法

你可以定义实例方法（不能修改字段）、静态方法：

```java
public record Person(String name, int age) {
    public String greeting() {
        return "Hello, " + name;
    }

    public static String species() {
        return "Homo sapiens";
    }
}
```

------

## 六、与框架的兼容性（如 Spring）

### ✅ 使用 `record` 作为 `@RequestBody` 参数

```java
@PostMapping("/save")
public String save(@RequestBody Person person) {
    return person.name();
}
```

要求：

- JSON 的字段名要与 `record` 的字段一致
- Spring Boot 2.6+、Jackson 2.12+ 已良好支持 `record`

------

## 七、典型使用场景

- DTO（数据传输对象）
- VO（视图对象）
- 配置载体类（结合 `@ConfigurationProperties`）
- JSON 映射类
- 测试数据模型

------

## 八、注意事项汇总

| 注意点           | 描述                                   |
| ---------------- | -------------------------------------- |
| 不可变性         | 字段不可更改，自动 `final`             |
| 没有 setter      | 只读字段，不提供 `setXxx()` 方法       |
| 不能定义实例字段 | 禁止 `private int x;` 形式的字段       |
| 可重载方法       | 允许自定义方法逻辑                     |
| 支持序列化       | Jackson、Gson、Lombok 等现代工具已支持 |

------

## 九、示例：一个完整的 `record` 使用

```java
public record Product(String name, double price) {

    public Product {
        if (price < 0) {
            throw new IllegalArgumentException("价格不能为负数");
        }
    }

    public String label() {
        return name + " - ￥" + price;
    }

    public static String category() {
        return "商品";
    }
}
```

------

## 🔚 总结

| 优点                     | 缺点                             |
| ------------------------ | -------------------------------- |
| 简洁、自动生成样板代码   | 不支持字段修改、不能添加实例字段 |
| 强不可变性               | 只能继承 `Record`，不能继承类    |
| 适用于数据模型、接口传输 | 不适合复杂业务逻辑类             |