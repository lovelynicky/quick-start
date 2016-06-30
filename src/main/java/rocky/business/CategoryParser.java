package rocky.business;

import org.apache.commons.lang3.StringUtils;
import rocky.domain.CaseCategory;
import rocky.domain.CaseScale;
import rocky.domain.CaseType;
import rocky.utils.PinYinUtils;
import rocky.utils.RandomGenerator;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static rocky.domain.CaseScale.FIRST;
import static rocky.domain.CaseScale.SECOND;
import static rocky.domain.CaseScale.THIRD;
import static rocky.domain.CaseType.ADMINISTRATIVE;
import static rocky.domain.CaseType.CRIMINAL;
import static rocky.utils.RandomGenerator.randomNoDuplicated36String;

/**
 * Created by qxb-810 on 2016/6/28.
 */
public class CategoryParser {

    private static final String CASE_TYPE_JUDGE_MARKER = "200000";
    private static final String FIRST_CASE_SCALE_FEATURE = "0000";
    private static final String SECOND_CASE_SCALE_FEATURE = "00";

    private String criminalCaseCode;
    private String administrativeCaseCode;
    private String caseTypeCode;

    private File file;
    private Map<CaseType, Map<CaseScale, List<CaseCategory>>> caseCategories = new HashMap<CaseType, Map<CaseScale, List<CaseCategory>>>();

    private CategoryParser(String fileLocation) {
        this(new File(fileLocation));
    }

    private CategoryParser(File file) {
        if (file.exists() && file.isFile()) {
            this.file = file;
        }
        for (CaseType caseType : CaseType.values()) {
            Map<CaseScale, List<CaseCategory>> caseScaleListMap = new HashMap<CaseScale, List<CaseCategory>>();
            for (CaseScale caseScale : CaseScale.values()) {
                caseScaleListMap.put(caseScale, new ArrayList<CaseCategory>());
            }
            caseCategories.put(caseType, caseScaleListMap);
        }
        criminalCaseCode = randomNoDuplicated36String();
        administrativeCaseCode = randomNoDuplicated36String();
        caseTypeCode = randomNoDuplicated36String();
    }

    public static CategoryParser loader(String fileLocation) {
        return new CategoryParser(fileLocation);
    }

    public static CategoryParser loader(File file) {
        return new CategoryParser(file);
    }

    public CategoryParser Parse() {
        if (this.file != null) {
            doParse();
            solveParentAndChildrenRelation();
        }
        return this;
    }

    private void doParse() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(this.file));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] array = line.split(StringUtils.SPACE);
                if (array != null && array.length > 1) {
                    String caseCode = array[0];
                    String name = array[1];
                    //刑事案件类型
                    if (caseCode.endsWith(FIRST_CASE_SCALE_FEATURE)) {
                        if (caseCode.compareTo(CASE_TYPE_JUDGE_MARKER) < 0) {
                            caseCategories.get(CRIMINAL).get(FIRST).add(
                                    new CaseCategory(randomNoDuplicated36String(), criminalCaseCode, name, caseCode, FIRST, CRIMINAL));
                        } else {
                            caseCategories.get(ADMINISTRATIVE).get(FIRST).add(
                                    new CaseCategory(randomNoDuplicated36String(), administrativeCaseCode, name, caseCode, FIRST, ADMINISTRATIVE));
                        }
                        continue;
                    }
                    if (caseCode.endsWith(SECOND_CASE_SCALE_FEATURE)) {
                        if (caseCode.compareTo(CASE_TYPE_JUDGE_MARKER) < 0) {
                            caseCategories.get(CRIMINAL).get(SECOND).add(
                                    new CaseCategory(randomNoDuplicated36String(), null, name, caseCode, CaseScale.SECOND, CRIMINAL));
                        } else {
                            caseCategories.get(ADMINISTRATIVE).get(SECOND).add(
                                    new CaseCategory(randomNoDuplicated36String(), null, name, caseCode, CaseScale.SECOND, ADMINISTRATIVE));
                        }
                        continue;
                    }
                    if (caseCode.compareTo(CASE_TYPE_JUDGE_MARKER) < 0) {
                        caseCategories.get(CRIMINAL).get(THIRD).add(
                                new CaseCategory(randomNoDuplicated36String(), null, name, caseCode, CaseScale.THIRD, CRIMINAL));
                    } else {
                        caseCategories.get(ADMINISTRATIVE).get(THIRD).add(
                                new CaseCategory(randomNoDuplicated36String(), null, name, caseCode, CaseScale.THIRD, ADMINISTRATIVE));
                    }

                } else {
                    //// TODO: 2016/6/28 error handling
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存结果到sql文件
     *
     * @param fileName 文件名（相对路径或是绝对路径）
     */
    public void saveResultToSqlFile(String fileName) {
        try {
            if (this.caseCategories != null && this.caseCategories.size() > 0) {
                File sqlFile;
                if (StringUtils.isNotEmpty(fileName)) {
                    if (fileName.contains(".sql")) {
                        sqlFile = new File(fileName);
                    } else {
                        sqlFile = new File(String.format("%s%s", fileName, ".sql"));
                    }

                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(sqlFile));
                    //add 案件类别 其中parentID是-1，增加刑事案件和行政案件其parentID是案件类别的IID，后面的一级分类的parentID是criminalCaseCode，administrativeCaseCode
                    bufferedWriter.append(String.format("insert into dictionary (IID,DICTIONARY_NO,NAME,PYJP,DICTIONARY_TYPE,PARENT_ID,CREATE_TIME,UPDATE_TIME,DELETEFLAG) " +
                                    "values ('%s','%s','%s','%s','AJLB','%s',sysdate,sysdate,'0');", caseTypeCode, "000000", "案件类别",
                            PinYinUtils.getPinYinHeadChar("案件类别").toUpperCase(), "-1"));
                    bufferedWriter.newLine();
                    bufferedWriter.append(String.format("insert into dictionary (IID,DICTIONARY_NO,NAME,PYJP,DICTIONARY_TYPE,PARENT_ID,CREATE_TIME,UPDATE_TIME,DELETEFLAG) " +
                                    "values ('%s','%s','%s','%s','AJLB','%s',sysdate,sysdate,'0');", criminalCaseCode, "000001", CRIMINAL.getDesc(),
                            PinYinUtils.getPinYinHeadChar(CRIMINAL.getDesc()).toUpperCase(), caseTypeCode));
                    bufferedWriter.newLine();
                    bufferedWriter.append(String.format("insert into dictionary (IID,DICTIONARY_NO,NAME,PYJP,DICTIONARY_TYPE,PARENT_ID,CREATE_TIME,UPDATE_TIME,DELETEFLAG) " +
                                    "values ('%s','%s','%s','%s','AJLB','%s',sysdate,sysdate,'0');", administrativeCaseCode, "000002", ADMINISTRATIVE.getDesc(),
                            PinYinUtils.getPinYinHeadChar(ADMINISTRATIVE.getDesc()).toUpperCase(), caseTypeCode));
                    bufferedWriter.newLine();
                    for (CaseType caseType : CaseType.values()) {
                        for (CaseScale caseScale : CaseScale.values()) {
                            for (CaseCategory caseCategory : caseCategories.get(caseType).get(caseScale)) {
                                bufferedWriter.append(String.format("insert into dictionary (IID,DICTIONARY_NO,NAME,PYJP,DICTIONARY_TYPE,PARENT_ID,CREATE_TIME,UPDATE_TIME,DELETEFLAG) " +
                                                "values ('%s','%s','%s','%s','AJLB','%s',sysdate,sysdate,'0');",
                                        caseCategory.getCode(), caseCategory.getCaseCode(), caseCategory.getName(), caseCategory.getPinYinShortName(), caseCategory.getParentCode()));
                                bufferedWriter.newLine();
                            }
                        }
                    }
                    bufferedWriter.flush();
                    bufferedWriter.close();
                } else {
                    //// TODO: 2016/6/28 empty file name handling
                }
            } else {
                System.out.println("please parse one dictionary file of cases");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void solveParentAndChildrenRelation() {
//        sort();
        if (this.caseCategories.size() > 0) {
            for (CaseType caseType : CaseType.values()) {
                List<CaseCategory> firstScaleCategories = caseCategories.get(caseType).get(FIRST);
                List<CaseCategory> secondScaleCategories = caseCategories.get(caseType).get(SECOND);
                List<CaseCategory> thirdScaleCategories = caseCategories.get(caseType).get(THIRD);
                for (CaseCategory firstCaseCategory : firstScaleCategories) {
                    String subCaseCode = firstCaseCategory.getCaseCode().substring(0, 2);
                    secondScaleCategories.forEach((caseCategory -> {
                        if (subCaseCode.equals(caseCategory.getCaseCode().substring(0, 2))) {
                            caseCategory.setParentCode(firstCaseCategory.getCode());
                        }
                    }));
                }

                for (CaseCategory secondCaseCategory : secondScaleCategories) {
                    String firstScaleSubCaseCode = secondCaseCategory.getCaseCode().substring(0, 2);
                    String secondScaleSubCaseCode = secondCaseCategory.getCaseCode().substring(2, 4);
                    thirdScaleCategories.forEach((caseCategory -> {
                        if (caseCategory.getCaseCode().substring(0, 2).equals(firstScaleSubCaseCode) && caseCategory.getCaseCode().substring(2, 4).equals(secondScaleSubCaseCode)) {
                            caseCategory.setParentCode(secondCaseCategory.getCode());
                        }
                    }));
                }
            }
        } else {
            //// TODO: 2016/6/29  else for empty scenario
        }
    }

    //对数据进行一个排序
    private void sort() {
        for (CaseType caseType : CaseType.values()) {
            for (CaseScale caseScale : CaseScale.values()) {
                caseCategories.get(caseType).get(caseScale).sort((before, after) -> {
                    if (before.getCode().compareTo(after.getCode()) < 0) {
                        return -1;
                    } else if (before.getCode().compareTo(after.getCode()) == 0) {
                        return 0;
                    }
                    return 1;
                });
            }
        }
    }

    public Map<CaseType, Map<CaseScale, List<CaseCategory>>> getCaseCategories() {
        return caseCategories;
    }
}
