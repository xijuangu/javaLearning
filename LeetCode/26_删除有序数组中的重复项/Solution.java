class Solution {
    public int removeDuplicates(int[] nums) {
        int left = 0;
        int count = 1;
        for(int right = 1; right < nums.length; right++){
            if(nums[left] != nums[right]){
                left++;
                nums[left] = nums[right];
                count++;
            }
        }
        return count;
    }
}