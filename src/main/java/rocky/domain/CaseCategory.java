package rocky.domain;

import org.apache.commons.lang3.StringUtils;
import rocky.utils.PinYinUtils;

/**
 * Created by qxb-810 on 2016/6/28.
 */
public class CaseCategory {
    /**
     * 编码
     */
    private String code;
    /**
     * 父编码
     */
    private String parentCode;
    /**
     * 名称
     */
    private String name;
    /**
     * 级别
     */
    private CaseScale scale;
    /**
     * 分类
     */
    private CaseType type;

    /**
     * 案件编码
     */
    private String caseCode;

    private String pinYinShortName;

    public CaseCategory() {
    }

    public CaseCategory(String code, String parentCode, String name, String caseCode, CaseScale scale, CaseType type) {
        this.code = code;
        this.parentCode = parentCode;
        this.name = name;
        this.scale = scale;
        this.type = type;
        this.caseCode = caseCode;
        this.pinYinShortName= PinYinUtils.getPinYinHeadChar(this.name.replace("，", StringUtils.EMPTY).replace("、",StringUtils.EMPTY)).toUpperCase();
    }

    public String getCode() {
        return code;
    }

    public String getParentCode() {
        return parentCode;
    }

    public String getName() {
        return name;
    }

    public CaseScale getScale() {
        return scale;
    }

    public CaseType getType() {
        return type;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getPinYinShortName() {
        return pinYinShortName;
    }

    public String getCaseCode() {
        return caseCode;
    }
}
