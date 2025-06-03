package com.sure.helper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Investigator2 {

    public static void main(String[] args) {
        int[] nums = {5, 8, 90, 100, 106, 105, 1000};
        int target = 1106;
        int[] result = twoSum(nums, target);
        System.out.println("Result " + Arrays.toString(result));


        int[] height = {1, 8, 7, 5, 4, 3, 9, 6, 8};
        int Max_Area = containerMaxWater(height);
        System.out.println("Max Area" + Max_Area);

        int[] numbers = {3, 0, 1};
        int missing = findMissingNumInArray(numbers);
        System.out.println("Missing number is :" + missing);
    }

    public static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement)) {
                return new int[]{map.get(complement), i};
            }
            map.put(nums[i], i);
        }
        return new int[]{};
    }

    public static int containerMaxWater(int[] height) {
        int left = 0;
        int right = height.length - 1;
        int max_area = 0;
        while (left < right) {
            int width = right - left;
            int current_area = Math.min(height[left], height[right]) * width;
            max_area = Math.max(current_area, max_area);

            if (height[left] < height[right]) {
                left++;
            } else right--;
        }
        return max_area;
    }

    public static int findMissingNumInArray(int[] numbers) {
        int actualSum = 0;
        int length = numbers.length;
        int expectedSum = length * (length + 1) / 2; // 6
        for (int num : numbers) {
            actualSum += num; //4
        }
        return expectedSum - actualSum;
    }

}


