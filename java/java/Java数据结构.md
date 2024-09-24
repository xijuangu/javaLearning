# Java 数据结构

## 数组（Arrays）

数组（Arrays）是一种基本的数据结构，可以存储固定大小的相同类型的元素。

```java
int[] array = new int[5];
```

- 特点： 固定大小，存储相同类型的元素。
- 优点： 随机访问元素效率高。
- 缺点： 大小固定，插入和删除元素相对较慢。

详细可见 `八股` 中 `数组.md`

## 列表（Lists）

Java 提供了多种列表实现，如 ArrayList 和 LinkedList。

```java
List<String> arrayList = new ArrayList<>();
List<Integer> linkedList = new LinkedList<>();
```

### ArrayList

- 特点： 动态数组，可变大小。
- 优点： 高效的随机访问和快速尾部插入。
- 缺点： 中间插入和删除相对较慢。

```java
ArrayList<String> arrayList = new ArrayList<>();
arrayList.add("Hello");
arrayList.add("World");

System.out.println(arrayList);
```

ArrayList 中的元素实际上是对象，而基本类型不是对象，引用类型的对象存储的是基本类型数据所存放的地址。因此`ArrayList<E>` 中的 `<E>` 只能为引用数据类型，这时我们就需要使用到基本类型的包装类（引用类型）。

|基本类型|引用类型|
|---|---|
|boolean|Boolean|
|byte|Byte|
|short|Short|
|int|Integer|
|long|Long|
|float|Float|
|double|Double|
|char|Character|

> 基本类型（Primitive Types）是Java内置的数据类型，存储的是实际的值。这些类型的数据直接存储在栈（stack）中，效率较高，且不需要通过引用来访问数据。
引用类型（Reference Types）是指那些由类定义的类型，包括类、接口、数组和枚举。引用类型的对象存储的是对象在堆（heap）中的引用（地址），而不是对象本身的值。

Arrays数组转化为ArrayList类型：

```java
// 示例数组
String[] array = {"Hello", "World", "Java"};

// 将数组转换为 List
// Arrays.asList(array) 实际上返回的是 Arrays$ArrayList 类型的实例，它是 Arrays 类的一个内部私有静态类，继承自 AbstractList 并实现了 List 接口。它返回的这个对象是一个固定大小的 List，其底层数据结构是传入的数组。因此可以用 List 类型的变量来引用它，并使用 List 接口中的方法，但不能改变其大小。这个内部类与 java.util.ArrayList 是不同的。
List<String> list = Arrays.asList(array);

// 不能直接赋值给 ArrayList 类型变量
// ArrayList<String> arrayList = Arrays.asList(array); // 编译错误

// 正确的方式是通过构造方法创建一个新的 ArrayList
ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(array));
System.out.println("ArrayList elements: " + arrayList);
```

ArrayList示例代码:

```java{.line-numbers}
package org.xijuangu;

import java.util.ArrayList;

public class listPrinter{
    public void printList(ArrayList<?> list) {
        for (Object obj : list) {
            System.out.print(obj + " ");
        }
    }
}
```

```java{.line-numbers}  
package org.xijuangu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;

public class Main {
    public static void main(String[] args) {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Hello");
        arrayList.add("World");

        listPrinter printer = new listPrinter();
        printer.printList(arrayList);
        System.out.println(arrayList);

        System.out.println("arrayList(1): " + arrayList.get(1));
        arrayList.add("Xijuangu");
        // set(int index, Object obj), 将index处的元素修改为obj
        arrayList.set(1,"!");
        System.out.println(arrayList);
        // remove(int index), 将index处的元素移除
        arrayList.remove(1);
        System.out.println(arrayList);

        System.out.println("size: " + arrayList.size());

        for(Object obj: arrayList) {
            System.out.println("round1 with Object obj: " + obj);
        }
        for(String obj: arrayList) {
            System.out.println("round2 with String obj: " + obj);
        }

        System.out.println("--------------------------");

        ArrayList<Integer> myNumbers = new ArrayList<>();
        myNumbers.add(19);
        myNumbers.add(36);
        myNumbers.add(23);
        for(Integer i: myNumbers) {
            System.out.println(i);
        }

        System.out.print("sorted: ");
        // 排序，注意Collection和Collections的区别，一个是集合框架中的基础接口，一个是工具类
        Collections.sort(myNumbers);
        printer.printList(myNumbers);

        System.out.println("\n--------------------------");
        // 二重数组
        ArrayList<ArrayList<Integer>> doubleArrayList = new ArrayList<>();
        doubleArrayList.add(new ArrayList<>());
        doubleArrayList.get(0).add(1);
        doubleArrayList.get(0).add(2);
        doubleArrayList.add(doubleArrayList.get(0));
        printer.printList(doubleArrayList);

        System.out.println("\n--------------------------");

        ArrayList<Integer> myList = new ArrayList<>();
        myList.add(1);
        myList.add(2);
        myList.add(3);
        myList.add(4);
        ArrayList<Integer> myList2 = new ArrayList<>();
        myList2.add(1);
        myList2.add(5);
        Integer[] remove;
        remove = new Integer[]{3};
        // removeAll(Collection c), 从原ArrayList对象中删除c中含有的所有元素，也即求补集
        myList.removeAll(myList2);
        printer.printList(myList);
        System.out.print("removed from Integer[]: ");
        // asList(Arrays arr)具体用法在上文：“Arrays数组转化为ArrayList类型”处
        myList.removeAll(Arrays.asList(remove));
        printer.printList(myList);

        System.out.println("\n--------------------------");

        // isEmpty()为空返回true，非空返回false
        System.out.println(myList.isEmpty());

        // addAll(int index, Collection c), 从index位置把c中的所有元素插入原ArrayList
        myList2.addAll(1, asList(remove));
        System.out.print("\nadded into myList2[1]: ");
        printer.printList(myList2);

        // 若省略index则默认从末尾插入
        myList2.addAll(asList(remove));
        System.out.print("\nadded into the end: ");
        printer.printList(myList2);

        System.out.println("\n--------------------------");

        ArrayList<Integer> myList3 = new ArrayList<>();
        myList3.add(1);
        myList3.add(2);
        myList3.add(3);
        myList3.add(4);
        myList3.add(5);

        // sublist(int fromIndex, int toIndex)返回的是一个子列表视图，包括fromIndex，不包括toIndex
        // 它的具体类型是ArrayList$SubList，这是一个AbstractList的内部类，专门用于表示ArrayList的一个部分视图。
        // 这个视图并不是一个完整的ArrayList对象，因此不能将其强制转换为ArrayList然后赋值
        // 也即，不能：ArrayList<Integer> myList4 = (ArrayList<Integer>) myList3.subList(2,5);
        // 因此如果想获得一个该子列表的ArrayList对象，要使用如下方法
        List<Integer> sublist = myList3.subList(2,5);
        ArrayList<Integer> myList4 = new ArrayList<>(sublist);
        System.out.println(myList4);

        System.out.println("--------------------------");

        // toString()可以将Arrays、ArrayList等对象转换成字符串
        String myString4 = myList4.toString();
        System.out.println(myString4);

        // toArray()可以将ArrayList等对象转换成Array
        Integer[] myArray4 = new Integer[myList4.size()];
        myList4.toArray(myArray4);
        System.out.println(Arrays.toString(myArray4));

        System.out.println("--------------------------");

        // 返回该元素最后一次出现的位置，没有则返回-1
        System.out.println(myList4.lastIndexOf(5));
        System.out.println(myList4.lastIndexOf(6));

        ArrayList<Integer> myList5 = new ArrayList<>(myList4);
        myList5.add(0, 1);
        myList5.add(4, 8);
        System.out.println("myList5: " + myList5);
        // retainAll(Collection c), 仅保留原对象中c中含有的元素，其余的移除
        myList5.retainAll(myList4);
        System.out.println("myList5 retainAll from myList4: " + myList5);

        System.out.println("--------------------------");

        // 检查对象中是否包含指定元素/指定对象中所包含的所有元素
        System.out.println(myList5.contains(5));
        System.out.println(myList5.containsAll(myList4));

        // 用这个方法来删除指定index之间的元素
        myList5.subList(1,3).clear();
        System.out.println(myList5);

        System.out.println("--------------------------");

        ArrayList<String> sites = new ArrayList<>();

        sites.add("Google");
        sites.add("Runoob");
        sites.add("Taobao");

        System.out.println("ArrayList : " + sites);

        // replaceAll(UnaryOperator<E> operator)用于将内容用给定的操作替换掉数组中每一个元素。
        // lambda表达式：(parameters) ->{ statements; }
        // parameters 是参数列表，expression 或 { statements; } 是Lambda 表达式的主体。
        // 如果只有一个参数，可以省略括号；如果没有参数，也需要空括号。
        sites.replaceAll(e -> e.toUpperCase());
        System.out.println("更新后的 ArrayList: " + sites);

        System.out.println("--------------------------");

        ArrayList<Integer> numbers = new ArrayList<>();

        // 往数组中添加元素
        numbers.add(1);
        numbers.add(2);
        numbers.add(3);
        numbers.add(4);
        System.out.println("ArrayList: " + numbers);

        // 所有元素乘以 10
        System.out.print("更新 ArrayList: ");

        // 将 lambda 表达式传递给 forEach
        // forEach(Consumer<E> action)用于遍历动态数组中每一个元素并执行特定操作
        numbers.forEach((e) -> {
            e = e * 10;
            System.out.print(e + " ");
        });
    }
}
```

### LinkedList

- 特点： 双向链表，元素之间通过指针连接。
- 优点： 插入和删除元素高效，迭代器性能好。
- 缺点： 随机访问相对较慢。

## 集合（Sets）

集合（Sets）用于存储不重复的元素，常见的实现有 HashSet 和 TreeSet。

```java
Set<String> hashSet = new HashSet<>();
Set<Integer> treeSet = new TreeSet<>();
```

### HashSet

- 特点： 无序集合，基于HashMap实现。
- 优点： 高效的查找和插入操作。
- 缺点： 不保证顺序。

```java{.line-numbers}
package org.xijuangu;

import java.util.HashSet;

public class HashSetDemo {
    public static void main(String[] args) {
        HashSet<String> sites = new HashSet<>();
        sites.add("google");
        sites.add("facebook");
        sites.add("twitter");
        sites.add("youtube");
        sites.add("google");        // 添加重复的元素无效
        System.out.println(sites);

        System.out.println("sites.contains(\"google\"): " + sites.contains("google"));

        // 删除元素，成功返回true，失败返回false
        System.out.println("sites.remove(\"google\"): " + sites.remove("google"));
        System.out.println("google removed sites: " + sites);

        System.out.println("sites.size(): " + sites.size());

        sites.clear();
        System.out.println("sites.clear(): " + sites);
        System.out.println("sites.isEmpty(): " + sites.isEmpty());

        sites.add("google");
        sites.forEach(e -> {
            if(e.equals("google")){
                sites.add(e.toUpperCase());
                sites.remove(e);
            }
        });
        System.out.println(sites);
    }
}
```

### TreeSet

- 特点：TreeSet 是有序集合，底层基于红黑树实现，不允许重复元素。
- 优点： 提供自动排序功能，适用于需要按顺序存储元素的场景。
- 缺点： 性能相对较差，不允许插入 null 元素。

## 映射（Maps）

映射（Maps）用于存储键值对，常见的实现有 HashMap 和 TreeMap。

```java
Map<String, Integer> hashMap = new HashMap<>();
Map<String, Integer> treeMap = new TreeMap<>();
```

### HashMap

- 特点： 基于哈希表实现的键值对存储结构。
- 优点： 高效的查找、插入和删除操作。
- 缺点： 无序，不保证顺序。

```java{.line-numbers}
HashMap<Integer, String> Sites = new HashMap<>();
```

HashMap示例代码：

```java
package org.xijuangu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ForEachDemo {
    public static void main(String[] args) {
        
        HashMap<Integer, String> sites = new HashMap<>();

        sites.put(1, "google");
        sites.put(3, "facebook");
        sites.put(5, "yahoo");
        System.out.println("sites: " + sites);

        HashMap<Integer, String> copy = new HashMap<>(sites);   // sites.clone()是浅拷贝
        System.out.println("copy created: " + copy);

        System.out.println("key 5 in sites: " + sites.get(5));

        sites.remove(5);
        System.out.println("key 5 removed from sites: " + sites);

        sites.clear();
        System.out.println("sites cleared: " + sites);
        System.out.println("sites.isEmpty(): " + sites.isEmpty());

        System.out.println("size of sites: " + sites.size());
        System.out.println("size of copy: " + copy.size());

        copy.forEach((key, value) -> System.out.println(key + " = " + value));

        sites.putAll(copy);
        System.out.println("sites.putAll(copy): " + sites);

        sites.replaceAll((key, value) -> {
            if(key == 3)
                return value.toUpperCase();
            else
                return value;
        });
        System.out.println("sites.replaceAll(key, value) toUpperCase: " + sites);
        // 单个字符想变成大写需要用:
        // char c = 'c';
        // c = Character.toUpperCase(c);

        sites.replace(1, "newBing");
        System.out.println("sites.replace(1, newBing): " + sites);

        System.out.println("sites.containsKey(3): " + sites.containsKey(3));
        System.out.println("sites.containsValve(\"baidu\"): " + sites.containsValue("baidu"));

        //错误：ArrayList<String> myArray = (ArrayList<String>)sites.values();
        System.out.println("sites.values(): " + sites.values());
        ArrayList<String> myArray = new ArrayList<String>(sites.values());
        System.out.println("sites.values() 返回一个 Collection，可以用来创建列表: " + myArray);

        System.out.println("sites.keySet(): " + sites.keySet());
        System.out.println("sites.entrySet() 返回一个键值对的set: " + sites.entrySet());


        // 遍历Map的方法：

        //第一种：普遍使用，由于二次取值,效率会比第二种和第三种慢一倍
        System.out.println("通过Map.keySet遍历key和value：");
        for (Integer key : sites.keySet()) {
            System.out.println("key= "+ key + " and value= " + sites.get(key));
        }

        //第二种
        System.out.println("通过Map.entrySet使用iterator遍历key和value：");
        Iterator<Map.Entry<Integer, String>> it = sites.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, String> entry = it.next();
            System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
        }

        //第三种：无法在for循环时实现remove等操作
        System.out.println("通过Map.entrySet遍历key和value");
        for (Map.Entry<Integer, String> entry : sites.entrySet()) {
            System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
        }

        //第四种：只能获取values,不能获取key
        sites.put(4, "FACEBOOK");
        System.out.println("通过Map.values()遍历所有的value，但不能遍历key");
        for (String v : sites.values()) {
            System.out.println("value = " + v);
        }
    }
}
```

### TreeMap

- 特点： 基于红黑树实现的有序键值对存储结构。
- 优点： 有序，支持按照键的顺序遍历。
- 缺点： 插入和删除相对较慢。

## StringBuilder & StringBuffer

StringBuilder 和 StringBuffer 类用以对字符串进行修改。和 String 类不同的是，StringBuilder 和 StringBuffer 类的对象能够被多次修改，**并且不产生新的未使用对象**。

StringBuilder 和 StringBuffer 之间的最大不同在于 StringBuilder 的方法不是线程安全的（不能同步访问）。但由于 StringBuilder 相较于 StringBuffer 有速度优势，所以多数情况下建议使用 StringBuilder 类。

使用示例：

```java{.line-numbers}
public class StringBuilderDemo {
    public static void main(String[] args){
        StringBuilder sb = new StringBuilder(10);   // 10是初始最大容量，如超出会自动扩大
        System.out.println("capacity: " + sb.capacity());   // 表示当前最大容量，而不是有多少字符
        sb.ensureCapacity(15);              // 确保容量至少为15，如小于15则使用扩容算法，直到大于等于15
        System.out.println("capacity: " + sb.capacity());   // StringBuilder 的新容量 = (当前容量 * 2) + 2。

        sb.append("Xijuangu");
        System.out.println(sb);
        sb.append("!");
        System.out.println(sb);

        System.out.println("length: " + sb.length());   // 表示当前有多少字符

        sb.insert(6, "Java");
        System.out.println("inserted: " + sb);

        sb.delete(5,8);
        System.out.println("deleted: " + sb);

        sb.reverse();
        System.out.println("reversed: " + sb);

        sb.replace(0,1,"?");    //替换包括0不包括1
        System.out.println("replaced: " + sb);
        sb.replace(0,0,"#");    //0,0表示在开头插入，不替换任何部分
        System.out.println("replaced: " + sb);

        System.out.println("charAt: " + sb.charAt(10));

        char[] chars = new char[11];
        sb.getChars(0,11,chars,0);  // 从sb中取出字符到字符数组，包括0不包括11，共11个字符
        System.out.print(chars);

        System.out.println("\nindexOf(\"gava\"): " + sb.indexOf("gava"));   //返回第一次出现指定字符串的位置
        System.out.println("lastIndexOf(\"gava\"): " + sb.lastIndexOf("gava"));   //返回最后一次出现指定字符串的位置

        sb.setCharAt(10,'Q');
        System.out.println("setCharAt(10,'Q'): " + sb); //修改索引为10处的字符，注意不是字符串，字符串要用replace

        sb.setLength(5);
        // setLength调整内容的长度，但capacity不变
        System.out.println("setLength(5): " + sb + "    But capacity = " + sb.capacity());      //调整长度，超出直接扔掉

        String str = sb.substring(3, 5);
        System.out.println("substring(3,5): " + str);   //或者substring(3)，会返回从3到末尾的子串

        str = sb.toString();
        System.out.println("toString: " + str); //将sb转换为String

        for (char c : sb.toString().toCharArray()) {
            system.out.print(c + " ");
        }
        //对sb中的字符遍历
    }
}
```

## Stack 栈

Stack 是 Java 中的一种后进先出（LIFO，Last In First Out）的数据结构，继承自 Vector 类，所以它可以使用 Vector 类中的所有方法。以下是一些常见的 Vector 用法：

- add(E element): 在 Vector 的末尾添加一个元素。
- add(int index, E element): 在指定位置插入元素。
- get(int index): 获取指定位置的元素。
- firstElement(): 获取第一个元素。
- lastElement(): 获取最后一个元素。
- remove(int index): 删除指定位置的元素。
- clear(): 清空整个栈。
- size(): 返回栈的元素数量。
- isEmpty(): 检查栈是否为空。

```java{.line-numbers}
    Stack<Integer> stack = new Stack<>();
    stack.push(1);
    stack.push(2);
    stack.add(1, 3);    //add可以从指定位置加入，push只能从栈顶加入
    System.out.println(stack);

    System.out.println(stack.firstElement());   //firstElement()返回第一个元素
    System.out.println(stack.lastElement());    //lastElement()返回最后一个元素
    System.out.println(stack.get(1));           //返回指定位置的元素。栈的位置编号规则为：栈底为1，向栈顶依次+1
    System.out.println(stack.peek());           //返回栈顶的元素

    System.out.println(stack.remove(1));  //删除元素，并返回被删除的元素
    System.out.println(stack);

    System.out.println(stack.size());

    stack.clear();
    System.out.println(stack);
    System.out.println(stack.isEmpty());

    stack.push(1);
    stack.push(4);
    stack.push(7);
    System.out.println(stack);
    stack.pop();
    System.out.println(stack);
            
    System.out.println(stack.search(2));    //查找目标对象所在的位置，不存在则返回-1
    System.out.println(stack.search(7));
    System.out.println(stack.search(1));
```

## Queue 队列

Queue 是一种先进先出（FIFO，First In First Out）的数据结构。它继承自 Collection 接口，所以可以使用 Collection 中的方法。又因为它是接口，所以必须使用它的实现类。

队列的实现类有很多，可以以有界与无界为区分，也可以以使用情景为区分，例如普通队列、阻塞队列、双端队列、优先级队列、并发队列、同步队列、阻塞队列等。

`LinkedList` 普通队列中的常用方法：

- `add(E e)` 和 `offer(E e)`：添加元素到队列
- `remove()` 和 `poll()`：移除并返回队列头部的元素。其中在添加失败时（如内存满），`add` 和 `remove` 方法会直接抛出异常，`offer` 和 `poll` 方法返回false。
- `element()` 和 `peek()`：获取队列头部的元素，但不移除。其中在队列为空时，`element()` 方法会直接抛出异常、`peek()` 会返回 `null`。
- `isEmpty()`：检查队列是否为空
- `size()`：返回队列中元素的个数
- `clear()`：清空队列中的所有元素

`ArraryDeque` 双端队列中的常用方法：

- `addFirst()` 与 `offerFirst()`: 在双端队列的开头添加指定的元素。如果双端队列已满，则 add 引发异常、offer 返回 false。
- `addLast()` 与 `offerLast()`: 在双端队列的末尾添加指定的元素。如果双端队列已满，则 add 引发异常、offer 返回 false。
- `getFirst()` 与 `peekFirst()`: 返回双端队列的第一个元素。如果双端队列为空，则 get 引发异常，peek 返回 null。
- `getLast()` 与 `peekLast()`: 返回双端队列的最后一个元素。如果双端队列为空，则 get 引发异常，peek 返回 null。
- `removeFirst()` 与 `pollFirst()`: 返回并删除双端队列的第一个元素。如果双端队列为空，则 remove 引发异常，poll 返回 null。
- `removeLast()` 与 `pollLast()`: 返回并删除双端队列的最后一个元素。如果双端队列为空，则 remove 引发异常，poll 返回 null。

## 实例

1. String 类型的数字转换为 int 类型或 Integer 类型，以及 int 类型或 Integer 类型的数字转换为 String 类型。

    ```java{.line-numbers}
    class Solution150 {
        public int evalRPN(String[] tokens) {
            Stack<String> stack = new Stack<>();
            for(String s : tokens){
                stack.push(s);
                if("+".equals(s)){
                    stack.pop();
                    int temp = Integer.parseInt(stack.pop()) + Integer.parseInt(stack.pop());   // 如果是integer类型的temp就可以用Integer.valueOf()
                    stack.push(Integer.toString(temp)); // int类型本身没有toString，但是Integer有静态方法toString(int)与非静态方法toString，因此此处既可以用Integer.toString(temp)，也可以直接将temp定义为Integer类型然后temp.toString
                }
                if("*".equals(s)) {
                    stack.pop();
                    int temp = Integer.parseInt(stack.pop()) * Integer.parseInt(stack.pop());
                    stack.push(Integer.toString(temp));
                }
                if("-".equals(s)) {
                    stack.pop();
                    int temp = Integer.parseInt(stack.pop()) - Integer.parseInt(stack.pop());
                    temp = -temp;
                    stack.push(Integer.toString(temp));
                }
                if("/".equals(s)) {
                    stack.pop();
                    int temp1 = Integer.parseInt(stack.pop());
                    int temp2 = Integer.parseInt(stack.pop());
                    int temp = temp2 / temp1;
                    stack.push(Integer.toString(temp));
                }
            }
            return Integer.valueOf(stack.peek());
        }
    }
    ```

2. String的replaceAll()方法与上面的Map和List的replaceAll()的方法不同，后者可以用Lambda表达式直接操作原对象中的数据内容，而String的replaceAll()方法需要传入两个参数，第一个参数是正则表达式，第二个参数是需要替换成的字符串，并且会返回一个替换后的字符串，不操作原字符串对象。
