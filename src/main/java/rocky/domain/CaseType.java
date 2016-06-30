package rocky.domain;

/**
 * Created by qxb-810 on 2016/6/28.
 */
public enum  CaseType {

    CRIMINAL("0", "刑事案件"),
    ADMINISTRATIVE("1", "行政案件");

    /**
     * 案件类别code
     */
    private String code;
    /**
     * 案件类别描述
     */
    private String desc;

    CaseType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}