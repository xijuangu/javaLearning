# I/O流

![alt text](image-1.png)

## 字节流

### FileOutputStream

实现步骤：

- 创建字节输出流对象`FileOutputStream fos = new FileOutputStream("file.txt")`
  - 参数可以是字符串表示的路径或File对象`new File("file.txt")`。在底层代码中，如果构造方法的传参是路径字符串，在构造方法内部调用的也是参数为File对象的构造方法。
  - 如果文件不存在则会创建一个新的文件，但是父级路径必须是存在的。
  - 如果文件已经存在，则会清空文件
- 写数据`fos.write(97)`
  - `write()`方法的参数是整数，但是实际上写到本地文件中的是整数在ASCII上对应的字符
- 释放资源`fos.close();`
  - 如果不释放资源，java会一直占用该文件直到程序终止。
  - 对于正在被占用的文件，如果要删除则会被操作系统提示并阻止，如果要修改则必须“另存为”，可以覆盖原文件。

|||
|---|---|
|`void write(int b)`|一次写一个字节数据|
|`void write(byte[] b)`|一次写一个字节数组数据|
|`void write(byte[] b, int off, int len)`|一次写一个字节数组的从`off`索引开始的`len`个数据|

> `String`类中的`getBytes()`方法可以将字符串对象转换成`byte[]`数组并返回
> Windows中换行符是`"\r\n"`，不同操作系统换行符不同，在Windows中，java会将`"\r"`或`"\n"`处理转换成`"\r\n"`，具体可自行百度。
> `public FileOutputStream(File file, boolean append)`是最底层的构造方法，`append`默认为`false`，表示是否要在原文件的基础上续写

### FileInputStream

基本步骤同上

```java{.line-numbers}
package org.xijuangu;

import java.io.FileInputStream;
import java.io.IOException;

public class FileInputDemo {
    public static void main(String[] args) throws IOException {
        // 读文件，file.txt中是"abc"，如果文件不存在则抛出错误
        FileInputStream fis = new FileInputStream("file.txt");

        int b1 = fis.read();      // 一次读一个字节，读出来的是该字节在ASCII上对应的整数
        System.out.println(b1);   // 97
        int b2 = fis.read();
        System.out.println(b2);   // 98
        int b3 = fis.read();
        System.out.println(b3);   // 99

        int b4 = fis.read();
        System.out.println(b4);   // -1，表示没有读到字符

        fis.close();
    }
}
```

或者循环读取：

```java
int b1;
while ((b1 = fis.read()) != -1) {
  System.out.println((char) b1);    // 强转类型为char
}
```

注意不能写成：

```java
while (fis.read() != -1) {    // 每次调用fis.read()都会读取一个字节并移动指针到下一个字节，这样写的话就会一个隔一个地读
  int b1 = fis.read();
  System.out.println((char) b1);
}
```

### 例子：复制文件

小文件复制：

```java{.line-numbers}
package org.xijuangu;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileTransportDemo {
    public static void main(String[] args) throws IOException {

        FileInputStream fis = new FileInputStream("file.txt");
        FileOutputStream fos = new FileOutputStream("copy.txt");

        int b;
        while((b = fis.read()) != -1){
            fos.write(b);
        }

        fos.close();
        fis.close();
    }
}
```

大文件复制：`public int read(byte[] buffer)`
不同于上面使用的`read()`方法，这个重载的方法一次会读取一个字节数组的数据，根据数组的长度，每次读取尽可能地把这个数组装满。如果读到最后不够读了，只会覆盖前n个元素(n为本次读取到的元素数)，剩下的元素仍然保持为上一次读取到的内容。该方法的返回值为本次读取到多少个数据。

```java{.line-numbers}
package org.xijuangu;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileTransportDemo {
    public static void main(String[] args) throws IOException {

      long start = System.currentTimeMillis();    // 标记开始时间

      FileInputStream fis = new FileInputStream("file.txt");
      FileOutputStream fos = new FileOutputStream("copytxt");

      byte[] bytes = new byte[2];    // 数组大小为2
      int readNums;                  // 标记本次读到了几个字节
      while((readNums = fis.read(bytes)) != -1){
          fos.write(bytes, 0, readNums);    // 从bytes数组的0号位置开始，写入readNums个字节（前readNums个数组元素）
          System.out.println(new String(bytes, 0, readNums));   // 从bytes数组的0号位置开始，将前readNums个数组元素转换成字符串并输出
      }

      fos.close();
      fis.close();

      long end = System.currentTimeMillis();    // 标记结束时间

      System.out.println("time cost: " + (end - start) + " ms");
    }
}
```

> 计算机中的文件是通过字节序列来表示的。每个文件，无论其类型（文本、图片、视频等），在存储时都是由一系列字节（8位二进制数）组成的。这些字节是数据的最小单位。
一个字节由 8 位（bit）组成。每一位可以是 0 或 1，因此一个字节可以表示的值范围是 0 到 255。
>
> 文件的字节序列：文件在存储时是按字节序列存储的。文件中的每一个字节都表示某种特定的数据，具体含义取决于文件的类型和格式。具体可参考JVM篇的字节码。
>
> 文件类型和字节表示
文本文件：
文本文件通常以 ASCII 或 Unicode 编码存储，每个字符对应一个或多个字节。
例如，ASCII 编码中的每个字符用一个字节表示，Unicode 编码的 UTF-8 可以用 1 到 4 个字节表示一个字符。
二进制文件：
包括图像、音频、视频等文件，直接以二进制数据形式存储。
文件格式定义了字节序列如何解释，例如 BMP 图像文件头部包含文件大小、宽度、高度等信息。

## 字符集

### ASCII编码

![alt text](image-2.png)
