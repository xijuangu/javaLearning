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

如果表中的元组满足条件，CASE 会在该元组的新的一列中返回指定的结果，与该元组相对应。如不使用 AS column_name 指定新列名，新列名将默认为 case。例如：

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
