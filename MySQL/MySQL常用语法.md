# MySQL常用语法

## SUM 函数

SUM 函数用于计算数值列的总和。它 **忽略 NULL 值**。

```sql
SELECT SUM(column_name) FROM table_name;
```

当使用 SUM() 函数与其他列时，必须确保在 GROUP BY 子句中包含所有非聚合列。不在 GROUP BY 子句中的列名 不能出现在 SELECT 子句的 非聚合函数表达式 中。在下例中，如果 department 列不在 GROUP BY 子句中，就会报错

```sql
SELECT department, SUM(salary) AS total_salary
FROM employees
GROUP BY department;
```

或者与其他聚合函数并列使用：

```sql
SELECT SUM(salary) AS total_salary, 
       COUNT(*) AS employee_count
FROM employees;
```

## CASE 表达式

CASE 表达式用于在查询中实现条件逻辑。

```sql
SELECT CASE 
           WHEN condition1 THEN result1
           WHEN condition2 THEN result2
           ...
           ELSE resultN
       END
FROM table_name;
```

如果表中的元组满足条件，SELECT CASE 会在该元组的新的一列中返回指定的结果，与该元组相对应。使用 AS column_name 可以指定新列名，不指定则默认为默认为 case。

```sql
SELECT 
    employee_id, 
    salary,
    CASE 
        WHEN salary < 3000 THEN 'Low'
        WHEN salary BETWEEN 3000 AND 7000 THEN 'Medium'
        ELSE 'High'
    END AS salary_range
FROM employees;
```

会返回：

employee_id | salary | salary_range
------------|--------|--------------
1           | 2500   | Low
2           | 4500   | Medium
3           | 8000   | High

## COUNT 函数

COUNT 函数用于返回列中的非 NULL 值的数量。

```sql
SELECT COUNT(column_name) FROM table_name;
```

## LIKE 子句

LIKE 子句用于模糊匹配关键字，通常与通配符一起使用，搜索符合某种模式的字符串。
LIKE 子句中使用百分号 `%` 字符来表示任意字符，类似于UNIX或正则表达式中的星号 `*`，使用 `_` 来表示一个字符，类似于UNIX或正则表达式中的问号 `?`。如果没有使用百分号 `%`, LIKE 子句与等号 `=` 的效果是一样的。

```sql
SELECT username, age, phone_number
FROM users 
WHERE username LIKE '%王_';     # 匹配任意前缀的二字姓王的用户
```

## 结合使用SUM、COUNT、CASE、LIKE

实现计算姓“张”和姓“李”的学生数量。SUM 用于姓“张”的统计，COUNT 用于姓“李”的统计。

```sql
SELECT 
    SUM(CASE 
            WHEN name LIKE '张%' THEN 1 
            ELSE 0 
        END) AS zhang_first_name,
    COUNT(CASE 
              WHEN name LIKE '李%' THEN 1 
              ELSE NULL 
          END) AS li_first_name
FROM student_table;
```

## JOIN/LEFT JOIN/RIGHT JOIN

```sql
A JOIN B ON A.colum=B.colum
```

```sql
SELECT Persons.LastName, Persons.FirstName, Orders.OrderNo
FROM Persons
INNER JOIN Orders
ON Persons.Id_P = Orders.Id_P
ORDER BY Persons.LastName
```

## UNION (ALL)

UNION 操作中的列数和数据类型必须相同（或者为 NULL），且会自动去除重复行，UNION ALL 则会保留所有行

```sql
SELECT column1, column2, ...
FROM table1
[WHERE condition1]
UNION [ALL]
SELECT column1, column2, ...
FROM table2
[WHERE condition2]
[ORDER BY column1, column2, ...];
```

列数和数据类型必须相同：

```sql
SELECT first_name, last_name FROM employees
UNION
SELECT department_name, NULL FROM departments
ORDER BY first_name;
```

## 视图

视图是一个虚拟表，它是通过 SQL 查询语句定义的，并可以像表一样进行查询。创建视图的标准语法如下：

```sql
CREATE VIEW view_name AS 
SELECT column1, column2, ... 
FROM table_name 
WHERE condition;
```

示例：创建视图 view_s 显示所有性别为男的同学的信息：

```sql
CREATE VIEW view_s AS
SELECT * FROM STU
WHERE 性别='男';
```

这样，视图 view_s 将包含表 STU 中所有性别为男的记录，可以像查询表一样查询这个视图：

```sql
SELECT * FROM view_s;
```

## 窗口函数

`RANK() OVER`：用于为分组内的每一行生成排名，相同值的行具有相同的排名，后续的排名会跳过这几个相同的排名。例如：`(90, 1), (90, 1), (80, 3), (60, 4), (60, 4), (50, 6)`
`OVER` 子句：用于定义窗口。可以指定分区 `(PARTITION BY)` 和排序 `(ORDER BY)`。

```sql
SELECT shirt_name, shirt_type, shirt_price,
       RANK() OVER (PARTITION BY shirt_type ORDER BY shirt_price) AS ranking
FROM SHIRTABLE;
```

上面的例子中，查询的结果会按照衬衫类型 (shirt_type) 分组，并按衬衫价格 (shirt_price) 排序，然后生成排名，排名的列名为 ranking。

查询结果示例：
shirt_name|shirt_type|shirt_price|ranking
---|---|---|---
Shirt A|Casual|20.00|1
Shirt E|Casual|22.00|2
Shirt B|Casual|25.00|3
Shirt D|Formal|28.00|1
Shirt C|Formal|30.00|2

## 聚合函数

COUNT()：用于返回某个列的行数或符合条件的行数。

```sql
SELECT COUNT(*) FROM employees;
```

SUM()：用于计算某列数值的总和。

```sql
SELECT SUM(salary) FROM employees;
```

AVG()：用于计算某列数值的平均值。

```sql
SELECT AVG(salary) FROM employees;
```

MAX()：用于返回某列中的最大值。

```sql
SELECT MAX(salary) FROM employees;
```

MIN()：用于返回某列中的最小值。

```sql
SELECT MIN(salary) FROM employees;
```

GROUP_CONCAT()：用于将多行数据合并为一个字符串。它通常与 GROUP BY 一起使用。

```sql
SELECT department, GROUP_CONCAT(employee_name) FROM employees GROUP BY department;
```

## IN 子句

IN 子句用于指定多个值进行匹配，在 WHERE 子句中常见。它允许你检查某个表达式是否在给定的列表中，简化了多重 OR 条件的写法。

```sql
SELECT column_name1, column_name2, ...
FROM table_name
WHERE column_name IN (value1, value2, ...);
```

假设有一个学生成绩表 sc：

sno|class|score
---|---|---
1|English|89
1|Math|90
1|Chinese|99
2|English|85
2|Math|95
2|Chinese|80

查询学号为 1 或 2 的学生成绩：

```sql
SELECT *
FROM sc
WHERE sno IN (1, 2);
```

结果
sno|class|score
---|---|---
1|English|89
1|Math|90
1|Chinese|99
2|English|85
2|Math|95
2|Chinese|80

## IF 子句

IF 子句是 MySQL 提供的一个条件函数，用于根据某个条件返回不同的值。与 CASE 表达式功能相同（只有两种 CASE 的情况下），但更简单。IF 函数接受三个参数：条件、条件为真时返回的值、条件为假时返回的值。

```sql
IF(condition, true_value, false_value)
```

示例
从上面的学生成绩表 sc 中，获取每个学生的成绩，如果成绩大于等于 90，标记为 "Pass"，否则标记为 "Fail"：

```sql
SELECT sno, class, score,
       IF(score >= 90, 'Pass', 'Fail') AS result
FROM sc;
```

结果
sno|class|score|result
---|---|---|---
1|English|89|Fail
1|Math|90|Pass
1|Chinese|99|Pass
2|English|85|Fail
2|Math|95|Pass
2|Chinese|80|Fail

综合示例
在一个查询中结合使用 IN 子句和 IF 函数：

```sql
SELECT sno,
       SUM(IF(class = 'English', score, 0)) AS english_score,
       SUM(IF(class = 'Math', score, 0)) AS math_score
FROM sc
WHERE class IN ('English', 'Math')
GROUP BY sno;
```

## DDL

DDL（Data Definition Language）语句：数据定义语言，主要是进行定义/改变表的结构、数据类型、表之间的链接等操作。常用的语句关键字有 CREATE、DROP、ALTER 等。

```sql
CREATE DATABASE 数据库名;

CREATE TABLE 表名(
列名1 数据类型,
列名2 数据类型,
列名3 数据类型,
...
)

ALTER TABLE 表名;
ALTER TABLE 表名 ADD 列名 数据类型;
ALTER TABLE 表名 CHANGE 列名 新列名 新数据类型;
ALTER TABLE 表名 DROP 列名;

DROP TABLE 表名;

DROP DATABASE 数据库名;
```

## DML

DML（Data Manipulation Language）语句: 数据操纵语言，主要是对数据进行增加、删除、修改操作。常用的语句关键字有 INSERT、UPDATE、DELETE 等。

```sql
INSERT INTO 表名 (字段1,字段2,...) values (某值,某值,...),(某值,某值,...), ...;

UPDATE 表名 SET 列名=新值 WHERE 限定条件;

DELETE FROM 表名 WHERE 限定条件;
```

## DQL

DQL（Data Query Language）语句：数据查询语言，主要是对数据进行查询操作。

```sql
# 指定要查询的列，可以是表中的列名，也可以是聚合函数（如SUM()、COUNT()等）
# 如果是聚合函数与列名并列查询，那么这个列名必须出现在GROUP BY中。
SELECT 列名1, 列名2, ...    
# 指定要查询的表
FROM 表名   
# 用于过滤记录，只有符合条件的记录才会被返回。可以使用逻辑运算符（如AND、OR）组合多个条件
# 但不能使用聚合函数，如果要用聚合函数则需要用 HAVING。
WHERE 条件  
# 对结果集进行分组，通常与聚合函数一起使用，以便对每组计算聚合值。
GROUP BY 列名   
# 对分组后的记录进行过滤，类似于WHERE，但用于聚合函数的结果，且只能过滤GROUP BY中的列名。
# 因此有HAVING必有GROUP BY。
HAVING 条件     
ORDER BY 列名 [ASC|DESC]
# 限制查询返回的记录数量，常用于分页或获取前N条记录，OFFSET可选。
LIMIT 数量 OFFSET 数量;     
```

## DCL

DCL（Data Control Language）语句： 数据控制语言，主要是用来设置/更改数据库用户权限。常用关键字有 GRANT、REVOKE 等。一般人员很少用到DCL语句。

```sql
GRANT （授权）

REVOKE （取消权限）
```

## 索引失效的常见情况

1. 对索引列使用**函数**或表达式：
例如：`WHERE YEAR(create_time) = 2023`。
索引失效原因：数据库无法直接使用索引列的值，而是需要对每一行数据应用函数或表达式。
2. 对索引列进行**隐式类型转换**：
例如：`WHERE user_id = '123'`，而 `user_id` 是整数类型。
索引失效原因：数据库需要将 `user_id` 转换为字符串类型，导致无法使用索引。
3. 使用 `OR` **连接的条件中只有部分字段有索引**：
例如：`WHERE a = 1 OR b = 2`，如果 `a` 和 `b` 中只有一个有索引，索引可能失效。
索引失效原因：`OR` 条件可能导致数据库无法有效使用索引。
4. 使用 `LIKE` **以通配符开头**：
例如：`WHERE name LIKE '%abc'`。
索引失效原因：通配符在开头时，数据库无法利用索引进行前缀匹配。
5. 复合索引**未遵循最左前缀原则**：
例如：复合索引是 `(a, b, c)`，但查询条件是 `WHERE b = 2 AND c = 3`。
索引失效原因：复合索引必须从最左边的列开始使用。
6. **数据分布不均匀**：
例如：某个列的值大部分相同（如性别列），即使有索引，数据库也可能选择全表扫描。
索引失效原因：索引的选择性太低，数据库认为全表扫描更高效。
7. 使用 `NOT IN` 或 `!=`：
例如：`WHERE status != 'active'`。
索引失效原因：`NOT IN` 或 `!=` 条件通常无法有效利用索引，而 `NOT EXISTS` 则优化得更好，更可能使用索引。
8. 查询**返回大量数据**：
例如：`SELECT * FROM users`。
索引失效原因：当查询返回的数据量很大时，数据库可能选择全表扫描而不是使用索引。

## 概念题

1. 下面有关sql 语句中 delete、truncate的说法正确的是？
   - **A. 论清理表数据的速度，TRUNCATE 一般比 DELETE 更快**
   正确。TRUNCATE 是一种DDL（数据定义语言）操作，不会逐行删除数据，而是直接重新分配数据页，不能回滚。DELETE 是 DML 操作，逐行删除，可以回滚，更慢。
   - **B. TRUNCATE 命令可以用来删除部分数据**
   错误。TRUNCATE 是一种DDL操作，用于删除整个表中的所有数据，能用于删除部分数据。要删除部分数据，需要使用 DELETE 语句合 WHERE 子句。
   - **C. TRUNCATE 只删除表的数据不删除表的结构**
   正确。TRUNCATE 删除表中的所有数据，但表的结构（如列、索引约束等）保留不变。
   - **D. DELETE 能够回收高水位（自增ID值）**
   错误。DELETE 操作不会重置表的自增ID。即使删除了表中的所行，自增ID也不会重置，新的插入行仍会使用删除前的最大自增ID下一个值。TRUNCATE 操作会重置自增ID。

    处理效率：drop > trustcate > delete
    删除范围：drop 删除整个表（结构和数据一起删除）；trustcate 删除全部记录，但不删除表结构；delete 只删除数据
    高水位线：delete 不影响自增ID值，高水线保持原位置不动；trustcate 会将高水线复位，自增ID变为1。
2. 某IT公司人事管理采用专门的人事管理系统来实现。后台数据库名为LF。新来的人事部张经理新官上任，第一件事是要对公司的员工做全面的了解。可是他在访问员工信息表EMPL里的工资和奖金字段的时被拒绝，只能查看该表其他字段。作为LF的开发者你将如何解决这一问题：
    A. 废除张经理的数据库用户帐户对表EMPL里的工资列和奖金列的SELECT权限
    B. 添加张经理到db_datareader角色
    C. 添加张经理到db_accessadmin角色
    D. 授予张经理的数据库用户帐户对表EMPL里的工资列和奖金列的SELECT权限。

    - A是废除，明显错误；
    - B C权限太大，没必要；
    - D是正确操作，给他相应字段查询权限即可。

## JDBC

JDBC（Java DataBase Connectivity）是一种用于执行 SQL 语句的 Java API，在 java.sql 下，是 Java 和数据库之间的一个桥梁，是一个规范而不是一个实现，能够交给数据库执行 SQL 语句。具体讲就是通过 Java 连接各种数据库，并对表中数据执行增、删、改、查等操作的技术。

本质上，JDBC 的作用和图形化客户端的作用相同，都是发送 SQL 操作数据库。差别在图形化界面的操作是图形化、傻瓜化的，而 JDBC 则需要通过编码完成图形操作时的效果。

也就是说，JDBC 本质上也是一种发送 SQL 操作数据库的 client 技术，只不过需要通过 Java 编码完成。

jdbc 操作数据库的步骤如下：

1. DriverManager 加载数据库驱动
2. Connection 获得数据库连接
3. Statement 获得执行SQL语句的 PreparedStatement 或者 Statement
4. 处理 ResultSet 结果集
