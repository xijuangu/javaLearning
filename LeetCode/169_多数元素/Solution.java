import java.util.Arrays;

class Solution1 {
    public int majorityElement(int[] nums) {
        Arrays.sort(nums);
        int flag = 0;
        for(int i = 0; i < nums.length; i++){
            if(nums[i] != nums[flag]){
                if(i - flag > nums.length/2){
                    return nums[flag];
                }
                flag = i;
            }
        }
        return nums[flag];
    }
}


class Solution2 {
    public int majorityElement(int[] nums) {
        Arrays.sort(nums);
        return (nums[nums.length / 2]);
    }
}