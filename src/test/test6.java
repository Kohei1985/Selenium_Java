package test;

import java.util.Arrays;
import java.util.List;

import persons.Person;

public class test6 {

    public static void main(String[] args) {
        Person psn =  new Person("雉子谷","sheet1","新琴似小/1/2/3","手稲山口小/4/5/6","新陵中/7/8/9","");

        String rsv1 = psn.getRsv_info01();
        List<String> instracts01 = Arrays.asList(rsv1.split("/"));
        System.out.println(instracts01.get(0));


    }

}
