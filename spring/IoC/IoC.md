# 第一部分  IoC基础容器

## Javaweb开发及其解决方案

### 传统Javaweb开发困惑及解决方案

1. 层与层之间紧密耦合在一起，接口与具体实现紧密耦合在一起。如果我后面想切换某一个接口的实现，就必须修改多处源代码。
   - 解决方案：程序代码中不要手动new对象，第三方根据要求为程序员提供需要的Bean对象
  
    ```java
    UserDao userDao = new UserDaoImpl();    // 如果有多个Impl1、Impl2...用于多种实现方式，则每次切换实现方式都要修改此处的代码
    userDao.updateUserInfo(user);
    ```

2. 通用的事务功能与日志功能耦合在业务代码中
   - 事务功能：事务功能是指在一个需要对数据库进行操作的功能中，要确保一组操作要么全部成功，要么全部失败，以保持数据一致性和完整性。例如实现一个简单的银行转账功能，从账户A转账100元到账户B。这个操作涉及两个数据库操作：从账户A中扣除100元、向账户B中添加100元。这两个操作必须同时成功或者同时失败，否则会导致数据不一致。通过事务管理可以确保这一点。
  
        ```java
        @Service
        public class BankService {

            @Autowired
            private JdbcTemplate jdbcTemplate;

            @Transactional
            public void transfer(String fromAccount, String toAccount, double amount) {
                jdbcTemplate.update("UPDATE accounts SET balance = balance - ? WHERE account_number = ?", amount, fromAccount);
                jdbcTemplate.update("UPDATE accounts SET balance = balance + ? WHERE account_number = ?", amount, toAccount);
            }
        }
        ```

   - 日志功能：日志功能是记录应用程序运行时的信息，包括普通信息、警告、错误等，以便于调试和监控。假设你要记录一个方法调用的开始和结束时间，就可以使用日志功能来记录这些信息：

        ```java
        @Service
        public class LoggingService {
            private static final Logger logger = LoggerFactory.getLogger(LoggingService.class);

            public void performTask() {
                logger.info("Starting task...");
                logger.info("Task completed.");
            }
        }
        ```

   - 解决方案：程序代码中不要手动new对象，第三方根据要求为程序提供需要的 **Bean对象的代理对象**
   - Bean对象的代理对象：
     - 当Spring容器启动时，会扫描所有配置的bean并进行实例化。
     - 在bean实例化之后但在属性注入之前，Spring会通过BeanPostProcessor接口的实现类来检查该bean是否需要代理。
     - 如果需要代理，Spring会创建该bean的代理对象，并将代理对象注册到Spring容器中，替换原始的bean。代理对象可以说是升级版的bean对象，比普通的bean对象功能更强大一些。
