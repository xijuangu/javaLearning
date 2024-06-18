# 设计模式

## 第三章

### 依赖倒置原则

#### 定义

- 高层模块不应该依赖底层模块，两者都应该依赖其抽象
- 抽象不应该依赖细节
- 细节应该依赖抽象

> "抽象"即接口或抽象类
>
> 依赖倒置原则在Java中体现为面向接口编程：
>
> 1. 模块间的依赖只通过抽象发生，实现类之间不发生直接的依赖关系，其依赖关系是通过接口或抽象类产生的。  
> 2. 接口或抽象类不依赖实现类  
> 3. 实现类依赖接口或抽象类
>
> 依赖倒置原则提高了代码的可读性和可维护性，降低了**并行开发**引起的风险。

### 使用依赖倒置原则并行开发

#### 项目依赖关系

例如，甲乙分别负责不同的模块，甲负责汽车类的建造，乙负责司机类的建造，在甲没有完成的情况下，乙由于缺少汽车类，无法完全地编写代码，编译器无法通过，更无法进行单元测试。这就是项目依赖关系。

如果不使用依赖倒置原则，在这种项目依赖关系下，各个开发人员只能顺序完成，效率很低，并且每个开发人员都需要了解所有的业务与技术。

但项目不可能一个人完成，那么就要协作、并行开发，要并行开发就必须要解决模块之间的项目依赖关系。由于我们的实现类只依赖于接口，那么我们可以先约定好接口，然后各自实现各自的接口即可。然后甲乙在各自的开发中，就可以先使用接口类作为变量类型，这时这些变量的类型称为**表面类型**。如果甲先完成，那么甲可以先自行进行单元测试而不必等乙完成。下面介绍依赖的代码可以参考。

#### 表面类型与实际类型

一个变量在被定义的时候被赋予的类型是表面类型，它的实际类型是调用时它的对象的类型。

### 依赖的三种写法

1. 构造函数注入

    ```java
    public interface ICar{
        public void run();
    }
    public interface IDriver{
        public void drive();
    }
    public class Driver implements IDriver{
        private ICar car;     // 定义了ICar接口之后只需要用ICar即可，不关心ICar的方法是否实现、如何实现
        public Driver (ICar _car){
            this.car = _car;
        }
        public void drive(){
            this.car.run();
        }
    }
    ```

2. setter依赖注入

    ```java
    public interface IDriver{
        public void setCar(ICar car);
        public void drive();
    }
    public class Driver implements IDriver{
        private ICar car;
        public void setCar (ICar car){
            this.car = car;
        }
        public void drive(){
            this.car.run();
        }
    }
    ```

3. 在接口的方法中声明依赖对象

    ```java
    public interface IDriver{
        public void drive(ICar car);
    }
    ```

### 最佳实践

- 每个类尽量都有接口类或抽象类，这时基本需求，有了抽象才可能依赖倒置
- 变量的表面类型尽量是接口或者抽象类
- 在开发阶段任何类都不应该从具体类派生，维护阶段可以通过继承覆写来修正bug，无需继承最高的基类
- 尽量不要覆写基类的方法
- 结合里氏替换原则使用
    > 一个父类能出现的地方，它的子类都一定能出现，并且功能不受影响

### 抽象类&接口类

#### 区别

1. 定义和实例化  
   - 抽象类：可以包含抽象方法（没有实现）和非抽象方法（有实现）。不能实例化。  
   - 接口：只能包含抽象方法（直到Java 8才引入默认方法和静态方法）。不能实例化。

    ```java
    // 抽象类
    abstract void makeSound();  // 抽象方法，没有实现
    void eat() {                // 非抽象方法(具体方法)，有实现
        System.out.println(name + " is eating.");
    }

    // 接口
    // 接口中的方法默认是public和abstract的
    void move();
    // 接口可以有默认方法
    default void rest() {
        System.out.println("Resting...");
    }
    // 接口可以有静态方法
    static void staticMethod() {
        System.out.println("Static method in interface");
    }
    ```

2. 继承和实现  
抽象类：一个类只能继承一个抽象类（单继承）。  
接口：一个类可以实现多个接口（多继承）。
   - extends为继承，一个类可以继承一个类，一个接口可以继承多个接口。抽象类用extends来实现抽象方法。
   - implements为实现接口，一个类可以通过implements实现一个或多个接口，该类中必须提供这些接口中所有方法的实现。
3. 字段  
抽象类：可以包含实例变量（字段）。  
接口：只能包含常量（默认是public static final的）。
4. 构造器  
抽象类：可以有构造器。  
接口：不能有构造器。
5. 访问修饰符  
抽象类：方法可以使用任何访问修饰符（public, protected, private）。  
接口：方法默认是public的，不能使用其他访问修饰符。

## 第四章

### 隔离原则

#### 接口

- 实例接口  
  例如``Person zhangSan = new Person()``,产生了一个实例，这个实例要遵从的标准就是Person这个类，这个角度来讲，Person就是zhangSan的接口
- 类接口  
  interface所定义的接口

#### 隔离原则的定义

1. 客户端不应该依赖它不需要的接口
2. 类之间的依赖关系应该建立在最小的接口上

> => 接口尽量细化，同时接口中的方法尽量少。尽量使用多个“专门的接口”，即，提供给几个模块就应该有几个接口。

- 尽量小，但要有限度，不能违反*单一职责原则*
- 高内聚(尽量少公布public方法)
- 定制服务(为每个模块定制接口)
- 有限度(细化要适中)

## 第五章

### 迪米特法则

> 也称为最少知识原则

一个类应该对 **自己需要耦合或调用的类** 知道的最少，即，只调用 **自己需要耦合或调用的类** 提供的public方法，不调用自己没有耦合或调用的类与方法。

上面的定义也可以解释为：只与**直接的**朋友通信。  
朋友类：出现在 **成员变量中** 或 **方法的输入输出参数中** 的类，而出现在方法体内部的类不属于朋友类。

#### 例子1

老师给体育委员发布命令，清点女生数量

```java
public class Teacher{
    public void command(GroupLeader groupLeader){   // GroupLeader是Teacher的朋友类
        // 创建女生队列
        List<Girl> listGirls = new ArrayList();     // Girl类出现在command方法体内，不属于Teacher的朋友类
                                                    // 也就是说，command方法与陌生的类Girl有了交流，破坏了Teacher的健壮性
                                                    // 方法是类的一个行为，类竟然不知道自己的行为与其他类产生了依赖关系，这是不允许的，严重违反了迪米特法则
        for (int i = 0; i < 20; i ++){
            listGirls.add(new Girl());
        }
        // 告诉体育委员执行清点任务
        groupLeader.countGirls(listGirls);
    }
}

public class GroupLeader{
    // 清点女生数量
    public void countGirls(List<Girl> listGirls){
        System.out.println("女生总人数为：" + listGirls.size());
    }
}

public class Girl{}

public class Client{
    public static void main(String[] args){
        Teacher teacher = new Teacher();
        teacher.command(new GroupLeader());
    }
}
```

修改后的Teacher类与GroupLeader类：

```java
public class Teacher{
    public void command(GroupLeader groupLeader){   // GroupLeader是Teacher的朋友类
        // 告诉体育委员执行清点任务
        groupLeader.countGirls(listGirls);
    }
}

public class GroupLeader{
    private List<Girl> listGirls;
    // 传递全班女生进来
    public void setListGirls(List<Girl> _listGirls){
        this.listGirls = _listGirls;
    }
    // 清点女生数量
    public void countGirls(){
        System.out.println("女生总人数为：" + this.listGirls.size());
    }
}
```

### 朋友类之间不应过于高度耦合

#### 例子2

```java
public class VSCode{
    private Random rand = new Random(System.currentTimeMillis());

    public int first(){
        System.out.println("执行第一个方法");
        return rand.nextInt(100);
    }

    public int second(){
        System.out.println("执行第二个方法");
        return rand.nextInt(100);
    }

    public int third(){
        System.out.println("执行第三个方法");
        return rand.nextInt(100);
    }
}

public class InstallSoftware{
    public void installVSCode(VSCode vscode){
        int first = vscode.first();             // 如果要把first、second、third的返回值改成boolean，就需要两个类一起改，应当避免这种设计
        if(first > 50){                         // 这种情况就是耦合关系过于牢固的负面效果
            int second = vscode.second();       
            if(second > 50){
                int third = vscode.third();
                if(third > 50){
                    vscode.first();
                }
            }
        }
    }
}

public class Client{
    public static void main(String[] args){
        InstallSoftware invoker = new InstallSoftware();
        invoker.installVSCode(new VSCode());
    }
}
```
