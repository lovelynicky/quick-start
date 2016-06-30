package rocky.utils;

import java.util.*;

/**
 * Created by qxb-810 on 2016/6/28.
 */
public class RandomGenerator {

    private static final int PREFIX_LENGTH = 23;
    private static Random random = new Random();


    /**
     * 随机的36个包含大写字母和数字的不重复字符串
     *
     * @return 一个不重复字符串
     */
    public static String randomNoDuplicated36String() {
        StringBuilder stringBuilder = new StringBuilder();
        List<String> upperCharsAndNumbers = CharsUtils.getUpperCharsAndNumbers();
        for (int i = 0; i < PREFIX_LENGTH; i++) {
            stringBuilder.append(upperCharsAndNumbers.get(getRandom(36)));
        }
        return stringBuilder.append(DateUtils.formatDateToMilliSecondsWithoutDecoration(new Date())).toString();
    }

    private static int getRandom(int range) {
        return random.nextInt(range);
    }
}
