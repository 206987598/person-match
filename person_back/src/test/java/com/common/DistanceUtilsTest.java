package com.common;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
class DistanceUtilsTest {
    @Test
    void minDistance() {
        List<String> list1 = Arrays.asList("大一", "男", "Java");
        List<String> list2 = Arrays.asList("大二", "男", "python", "实习生");
        List<String> list3 = Arrays.asList("女", "Java");
        DistanceUtils distanceUtils = new DistanceUtils();
        int i = distanceUtils.minDistance(list1, list2);
        int i1 = distanceUtils.minDistance(list2, list3);
        System.out.println(i);
        System.out.println(i1);


    }

}