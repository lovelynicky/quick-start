package rocky.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by qxb-810 on 2016/6/28.
 */
public class DateUtils {

    /**
     * 格式化时间到秒
     * @return
     */
    public static String formatDateToMilliSecondsWithoutDecoration(Date date) {
        return new SimpleDateFormat("HHmmssSSS").format(date);
    }
}
