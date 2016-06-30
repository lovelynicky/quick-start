package rocky.domain;

/**
 * Created by qxb-810 on 2016/6/28.
 */
public enum CaseScale {
    FIRST("1", "第一级"),
    SECOND("2", "第二级"),
    THIRD("3", "第三级");
    /**
     * 案件级别code
     */
    private String code;
    /**
     * 案件级别描述
     */
    private String desc;

    CaseScale(String code, String desc) {
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
