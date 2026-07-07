package com.avendum.midsautomate.validation;

import java.util.*;
import java.util.stream.Collectors;

public class CodingTest {
    public static void main(String[] args) {
//       List<Integer> list = Arrays.asList(1,2,3,4,5,6,7);
//       List<List<Integer>> lists = Arrays.asList(Arrays.asList(1,2,3,7),Arrays.asList(4, 10,13),Arrays.asList(11,7,6));
//
////       list.stream().filter(i -> i%2==0).forEach(System.out::println);
//        System.out.println(list.stream().map(i -> i*10).reduce((i,j) -> i+=j).get());
//
//        list = lists.stream().flatMap(integers -> integers.stream().map(i -> i)).sorted().collect(Collectors.toList());
//        System.out.println("List = "+list);

        String a = "kar";
        String b = a;
        
        a = a.concat(" til");
        System.out.println(a);
        System.out.println(b);
    }

}
