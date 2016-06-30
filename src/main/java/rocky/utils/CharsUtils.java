package rocky.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qxb-810 on 2016/6/29.
 */
public class CharsUtils {
    public static List<String> getUpperChars() {
        List<String> upperChars = new ArrayList<String>();
        for (int i = 65; i < 91; i++) {
            char[] chars = new char[]{(char) i};
            upperChars.add(String.valueOf(chars));
        }
        return upperChars;
    }

    public static List<String> getNumberString() {
        List<String> numbers = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            numbers.add(String.valueOf(i));
        }
        return numbers;
    }

    public static List<String> getUpperCharsAndNumbers() {
        List<String> upperCharsAndNumbers = new ArrayList<String>();
        upperCharsAndNumbers.addAll(getUpperChars());
        upperCharsAndNumbers.addAll(getNumberString());
        return upperCharsAndNumbers;
    }
}
