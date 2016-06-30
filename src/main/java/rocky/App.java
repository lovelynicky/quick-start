package rocky;

import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import rocky.business.CategoryParser;
import rocky.domain.CaseCategory;
import rocky.domain.CaseScale;
import rocky.domain.CaseType;
import rocky.utils.DateUtils;
import rocky.utils.PinYinUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static rocky.utils.PinYinUtils.getPinyin;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        CategoryParser.loader("src/files/case-categories.txt").Parse().saveResultToSqlFile("dictionary.sql");
//        String str = "有重大火灾隐患，经公安消防机构通知逾期不改正的、无证驾车";
//        System.out.println(PinYinUtils.getPinYinHeadChar(str.replace("，", StringUtils.EMPTY).replace("、",StringUtils.EMPTY)).toUpperCase());
    }
}
