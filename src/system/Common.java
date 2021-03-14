package system;

import java.util.Arrays;
import java.util.List;

public class Common {

    public static List<String> splitedList(String ary){
        String rsv = ary;
        List<String> instruct = Arrays.asList(rsv.split("/"));
        return instruct;
    }
}
