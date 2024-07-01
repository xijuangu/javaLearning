# 题目

给定一个整数数组 nums，将数组中的元素向右轮转 k 个位置，其中 k 是非负数。

示例 1:
输入: nums = [1,2,3,4,5,6,7], k = 3  
输出: [5,6,7,1,2,3,4]  
解释:  
向右轮转 1 步: [7,1,2,3,4,5,6]  
向右轮转 2 步: [6,7,1,2,3,4,5]  
向右轮转 3 步: [5,6,7,1,2,3,4]  

## 解答

方法一：使用额外的数组
我们可以使用额外的数组来将每个元素放至正确的位置。用 n 表示数组的长度，我们遍历原数组，将原数组下标为 i 的元素放至新数组下标为 (i+k)modn 的位置，最后将新数组拷贝至原数组即可。

```java
class Solution {
    public void rotate(int[] nums, int k) {
        int[] copy = new int[k];
        int i = k % nums.length;
        if(nums.length != 1){
            System.arraycopy(nums, nums.length-i, copy, 0, i);
            System.arraycopy(nums, 0, nums, i, nums.length-i);
            System.arraycopy(copy, 0, nums, 0, i);
        }
    }
}
```
