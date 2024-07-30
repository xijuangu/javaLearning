# MySQL常用语法

## SUM 函数

SUM 函数用于计算数值列的总和。它忽略 NULL 值。

```sql
SELECT SUM(column_name) FROM table_name;
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

如果表中的元组满足条件，SELECT CASE 会在该元组的新的一列中返回指定的结果，与该元组相对应。如不使用 AS column_name 指定新列名，新列名将默认为 case。例如：

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

RANK() OVER：用于为分组内的每一行生成排名，相同值的行具有相同的排名，后续的排名会跳过这几个相同的排名。例如(90, 1), (90, 1), (80, 3), (60, 4), (60, 4), (50, 6)
OVER 子句：用于定义窗口。可以指定分区 (PARTITION BY) 和排序 (ORDER BY)。

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

## IN 子句

IN 子句用于指定多个值进行匹配，在 WHERE 子句中常见。它允许你检查某个表达式是否在给定的列表中，简化了多重 OR 条件的写法。

```sql
SELECT column_name(s)
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

IF 子句是 MySQL 提供的一个条件函数，用于根据某个条件返回不同的值。与 CASE 表达式类似，但更简单。IF 函数接受三个参数：条件、条件为真时返回的值、条件为假时返回的值。

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
