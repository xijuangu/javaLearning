# RabbitMQ

## RabbitMQ 简介

RabbitMQ 是一个开源的消息代理（Message Broker）和队列服务器，用于在分布式系统中实现消息的可靠传递。它基于 **AMQP（Advanced Message Queuing Protocol，高级消息队列协议）**，并支持多种消息协议。RabbitMQ 以其高可靠性、灵活性和易用性而闻名，广泛应用于 **微服务架构**、异步任务处理、事件驱动系统等场景。

---

### RabbitMQ 的核心概念

1. **生产者（Producer）**  
   生产者是发送消息的应用程序或服务。它将消息发送到 RabbitMQ 中的交换机（Exchange）。

2. **消费者（Consumer）**  
   消费者是从队列中接收消息的应用程序或服务。消费者可以订阅一个或多个队列，并处理接收到的消息。

3. **队列（Queue）**  
   队列是存储消息的缓冲区。生产者将消息发送到队列，消费者从队列中读取消息。队列具有持久化特性，能够确保即使 RabbitMQ 重启，消息也不会丢失。

4. **交换机（Exchange）**  
   交换机负责接收生产者发送的消息，并根据绑定规则（Binding Key）将消息路由到一个或多个队列。RabbitMQ 支持以下几种类型的交换机：
   - **Direct Exchange**：精确匹配路由键。
   - **Fanout Exchange**：广播消息到所有绑定的队列。
   - **Topic Exchange**：通过模式匹配路由键。
   - **Headers Exchange**：基于消息头属性进行路由。

5. **绑定（Binding）**  
   绑定是交换机与队列之间的关联关系，定义了消息如何从交换机路由到队列。

6. **消息确认（Acknowledgement）**  
   RabbitMQ 提供了消息确认机制，确保消息被消费者成功处理后才会从队列中移除。如果消费者处理失败，消息可以重新入队或进入死信队列。

7. **死信队列（Dead Letter Queue, DLQ）**  
   当消息无法被正常处理时（如超时、拒绝等），可以将其路由到死信队列，以便后续分析或重试。

8. **持久化（Persistence）**  
   RabbitMQ 支持消息和队列的持久化，确保在服务器崩溃或重启后数据不会丢失。

---

### RabbitMQ 的工作模式

RabbitMQ 提供了多种消息传递模式，适用于不同的业务场景：

1. **简单模式（Simple Mode）**  
   生产者直接将消息发送到队列，消费者从队列中读取消息。这是最基础的消息传递模式。

2. **工作队列模式（Work Queue Mode）**  
   多个消费者共享一个队列，RabbitMQ 会按照轮询的方式将消息分发给消费者，适合 **负载均衡** 的场景。

3. **发布/订阅模式（Publish/Subscribe Mode）**  
   使用 Fanout 交换机将消息广播到所有绑定的队列，适合需要将消息分发给多个消费者的场景。

4. **路由模式（Routing Mode）**  
   使用 Direct 交换机根据路由键将消息发送到特定的队列，适合需要精确控制消息流向的场景。

5. **主题模式（Topic Mode）**  
   使用 Topic 交换机通过模式匹配路由键将消息发送到符合条件的队列，适合复杂的路由规则。

6. **RPC 模式（Remote Procedure Call Mode）**  
   通过 RabbitMQ 实现远程过程调用，客户端发送请求消息到服务器，服务器处理后返回响应消息。

---

### RabbitMQ 的优势

1. **高可靠性**  
   RabbitMQ 提供了消息持久化、镜像队列、消息确认等功能，确保消息不会丢失。

2. **灵活的消息路由**  
   支持多种交换机类型，可以根据业务需求灵活配置消息路由规则。

3. **跨平台支持**  
   RabbitMQ 支持多种编程语言（如 Java、Python、Go、Node.js 等），方便集成到不同的技术栈中。

4. **集群和高可用性**  
   RabbitMQ 支持集群部署，可以通过镜像队列实现高可用性。

5. **丰富的插件生态**  
   RabbitMQ 提供了丰富的插件（如管理界面、延迟队列、消息追踪等），扩展了其功能。

---

### RabbitMQ 的应用场景

1. **异步任务处理**  
   将耗时的任务（如发送邮件、生成报表）放入队列中异步处理，提升系统响应速度。

2. **解耦系统组件**  
   通过消息队列实现系统组件之间的松耦合，提高系统的可维护性和扩展性。

3. **流量削峰**  
   在高并发场景下，使用 RabbitMQ 缓存请求，避免后端服务过载。

4. **事件驱动架构**  
   在微服务架构中，使用 RabbitMQ 实现服务间的事件通知和通信。

5. **日志收集与分析**  
   将分布式系统的日志消息发送到 RabbitMQ，便于集中存储和分析。

---

### RabbitMQ 的安装与使用

#### 安装步骤（以 Linux 为例）

1. 安装 Erlang（RabbitMQ 基于 Erlang 开发）：

   ```bash
   sudo apt-get update
   sudo apt-get install erlang
   ```

2. 安装 RabbitMQ：

   ```bash
   sudo apt-get install rabbitmq-server
   ```

3. 启动 RabbitMQ 服务：

   ```bash
   sudo systemctl start rabbitmq-server
   ```

#### 启用管理插件

```bash
sudo rabbitmq-plugins enable rabbitmq_management
```

然后通过浏览器访问 `http://localhost:15672`，默认用户名和密码为 `guest/guest`。

---

### 在项目中使用（以 Java 为例）

#### 1. 添加依赖

```xml
<dependency>
    <groupId>com.rabbitmq</groupId>
    <artifactId>amqp-client</artifactId>
    <version>5.16.0</version>
</dependency>
```

#### 2. 连接到 RabbitMQ

在 Java 中，连接到 RabbitMQ 需要创建一个 ConnectionFactory 对象，并通过它建立连接（Connection）和通道（Channel）。通道是与 RabbitMQ 交互的主要接口。

Connection 表示客户端与 RabbitMQ 服务器之间的连接。它是通过 AMQP 协议建立的，并且底层依赖于 TCP 连接来实现通信。换句话说，Connection 其实是一个基于 TCP 的长连接，用于传输 AMQP 协议的消息，还包含了对 AMQP 协议的解析、握手、认证等功能。

当你调用 RabbitMQ Java 客户端库中的 ConnectionFactory.newConnection() 方法时，会创建一个 Connection 对象，该对象负责管理与 RabbitMQ 服务器之间的通信。

虽然 Connection 表示与 RabbitMQ 服务器之间的 **物理连接**，但在实际开发中，我们通常不会直接通过 Connection 发送消息或消费消息，而是使用 Channel。Channel 是基于 Connection 创建的轻量级 **逻辑连接**，所有的 AMQP 操作（如发布消息、消费消息、声明队列等）都是通过 Channel 完成的。

在面对同一个客户端需要同时多个连接时，创建多个 Channel 比创建多个 Connection 更高效，因为每个 Connection 都需要单独的 TCP 连接，而 Channel 共享同一个 Connection 的底层 TCP 连接。举个例子，一个项目中既有生产者又有消费者，那么我们可以使用 SpringBoot 的 `@Configuration` 与 `@Bean` 注解，令整个项目共享一个 Connection，用同一个 Connection 创建多个 Channel。

示例代码：连接 RabbitMQ

```java
package com.xijuangu.simpleModeDemo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer {
    // 队列名称
    public static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        // ① 创建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost("121.40.47.122");
        factory.setUsername("xijuangu");
        factory.setPassword("230824WQjp");

        // ② 使用连接工厂创建一个connection
        try(Connection connection = factory.newConnection()) {
            // ③ 使用connection创建一个channel
            Channel channel = connection.createChannel();

            /*
              ④ 使用channel声明一个队列，声明时就会创建队列
              1.队列名称，如果为空字符串（""），RabbitMQ 会自动生成一个唯一队列名，形似乱码
                如果该队列名已经存在，且队列参数完全一致，那么会直接返回现有的队列
                如果队列参数不完全一致，就会抛出异常并关闭channel，也就是说，一个队列的参数一旦被创建就不能被修改，要修改的话只能先删除再创建
              2.队列是否持久化，默认为false，代表队列存储在内存中，重启就没了。注意这里说的是队列而不是消息！
                消息的持久化需在发送消息时设置 MessageProperties.PERSISTENT_TEXT_PLAIN
              3.该队列是否exclusive，即排他队列，仅对声明它的连接可见，且connection关闭后队列自动删除，排他队列不可设为持久化
              4.是否在所有消费者断开连接后自动删除队列
              5.其他参数，以结构化方式传入，一般用map以key-value的形式传入，例如消息的 TTL（生存时间）、队列长度限制、死信交换机等
             */
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            ...
        }
    }
}
```

#### 3. 消息生产者

消息生产者负责将消息发送到 RabbitMQ 的队列中。可以通过 basicPublish 方法将消息发布到指定的交换机和队列。

承接上例：

```java

/*
  ⑤ 使用channel发送一个消息
  1.要发送的交换机名称
  2.路由键
  3.一个含有消息的属性的对象，可以是null
  4.消息的实际内容，必须是字节数组
*/
String message = "hello";
// 这里简单模式和后面的工作队列模式都是将交换机名称设为空字符串，此时消息会通过默认交换机进行路由
// 而默认的交换机类型正是direct交换机，它的特点是，路由键就是要发送的队列的队列名，并且必须精确匹配队列名，否则消息会被丢弃
// 还有其他类型的交换机，它们的路由键需要符合各自的绑定规则
// 消费者的路由键可能会含有通配符，用以匹配生产者生产信息时提供的完整路由键
// 例如，规定路由键格式：order.[国家].[事件]，生产者可能给出路由键"order.us.created"，而消费者则绑定"order.us.*"
channel.basicPublish("", QUEUE_NAME, null, message.getBytes());

System.out.println("消息发送完毕");
```

#### 4. 消息消费者

消息消费者负责从队列中接收并处理消息。可以通过 basicConsume 方法订阅队列，并定义回调函数来处理接收到的消息。

代码示例，可接上面第三步的示例，也可接第二步的代码示例

```java
// 一个Connection（物理TCP连接）可以创建多个Channel
// 每个Channel是逻辑上的独立会话，避免频繁创建/销毁TCP连接的开销
// Channel 是线程不安全的，多线程共享同一个 Channel 会导致数据错乱
channel = connection.createChannel();

/*
  消费者消费消息
  1.消费的队列名称
  2.消费成功之后是否要自动应答(true代表自动应答,false代表手动应答)
  a. 自动应答：
    - 消费者在接收到消息后，立即自动向 RabbitMQ 确认消息已处理完成。
    - RabbitMQ 收到确认后，立即将消息从队列中删除。
    - 无论消费者是否实际处理成功，消息都会被标记为已消费。
    - 简单易用、低延迟，但有消息丢失风险，适合允许消息丢失的非关键任务，或是开发时的快速验证
  b. 手动应答：
    - 消费者在处理消息后，必须显式发送确认信号（ACK） 给 RabbitMQ。
    - RabbitMQ 收到 ACK 后，才将消息从队列中删除。
    - 若消费者未发送 ACK 或连接中断，RabbitMQ 会重新投递消息（给其他消费者或原消费者）。
  c. 最佳实践：
    - 手动应答+持久化消息，确保消息在服务重启或处理失败时仍可靠
    - 通过 basicQos 设置预取计数，避免消费者过载；
    - 处理失败时，根据业务场景选择重新入队或进入死信队列
  3.消费者消费消息的回调(函数式接口)
  4.消费者取消消费的回调(函数式接口)
*/

// 消费消息的回调
DeliverCallback deliverCallback = (consumerTag, message) -> {
    System.out.println("成功消费消息,内容为:" + new String(message.getBody()));
};
// 取消消费的回调
CancelCallback cancelCallback = (consumerTag) -> {
    System.out.println("消息消费被中断");
};

channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);

Thread.sleep(1000);
```

##### 消息确认机制

消费者可以通过手动确认消息的方式确保消息被正确处理。如果消息处理失败，RabbitMQ 会重新投递消息。

关闭自动确认：

```java
channel.basicConsume(QUEUE_NAME, false, deliverCallback, cancelCallback);
```

手动确认消息：

```java
DeliverCallback deliverCallback = (consumerTag, delivery) -> {
    String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
    System.out.println(" [x] Received '" + message + "'");
    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false); // 手动确认
};
```

#### 其他广播模式、工作队列模式、Fanout 模式的示例代码在项目中可看

[rabbitmqDemo](https://github.com/xijuangu/rabbitmqDemo)
