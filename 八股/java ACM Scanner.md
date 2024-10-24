# ACM 标准输入输出方法

1. nextInt()：直至读取到空格或回车之后结束本次的int值；
2. next()：直至读取到空格或回车之后结束本次的String值，不可读取回车，下次读取时会跳过该回车；
3. nextLine()：直至读取到换行符（回车）之后结束本次读取的String，可读取回车（空值）

```java
import java.util.Scanner;

public class Main{
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while(in.hasNext()){
            int a = in.nextInt();
            int b = in.nextInt();
            System.out.println(a + b);
        }
    }
}
```
