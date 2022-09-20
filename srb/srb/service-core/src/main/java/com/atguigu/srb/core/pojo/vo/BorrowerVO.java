package com.atguigu.srb.core.pojo.vo;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import com.atguigu.srb.core.pojo.entity.BorrowerAttach;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(
        description = "借款人认证信息"
)
public class BorrowerVO {
    @ApiModelProperty("性别（1：男 0：女）")
    private Integer sex;
    @ApiModelProperty("年龄")
    private Integer age;
    @ApiModelProperty("学历")
    private Integer education;
    @ApiModelProperty("是否结婚（1：是 0：否）")
    private Boolean marry;
    @ApiModelProperty("行业")
    private Integer industry;
    @ApiModelProperty("月收入")
    private Integer income;
    @ApiModelProperty("还款来源")
    private Integer returnSource;
    @ApiModelProperty("联系人名称")
    private String contactsName;
    @ApiModelProperty("联系人手机")
    private String contactsMobile;
    @ApiModelProperty("联系人关系")
    private Integer contactsRelation;
    @ApiModelProperty("借款人附件资料")
    private List<BorrowerAttach> borrowerAttachList;

    public BorrowerVO() {
    }

    public Integer getSex() {
        return this.sex;
    }

    public Integer getAge() {
        return this.age;
    }

    public Integer getEducation() {
        return this.education;
    }

    public Boolean getMarry() {
        return this.marry;
    }

    public Integer getIndustry() {
        return this.industry;
    }

    public Integer getIncome() {
        return this.income;
    }

    public Integer getReturnSource() {
        return this.returnSource;
    }

    public String getContactsName() {
        return this.contactsName;
    }

    public String getContactsMobile() {
        return this.contactsMobile;
    }

    public Integer getContactsRelation() {
        return this.contactsRelation;
    }

    public List<BorrowerAttach> getBorrowerAttachList() {
        return this.borrowerAttachList;
    }

    public void setSex(final Integer sex) {
        this.sex = sex;
    }

    public void setAge(final Integer age) {
        this.age = age;
    }

    public void setEducation(final Integer education) {
        this.education = education;
    }

    public void setMarry(final Boolean marry) {
        this.marry = marry;
    }

    public void setIndustry(final Integer industry) {
        this.industry = industry;
    }

    public void setIncome(final Integer income) {
        this.income = income;
    }

    public void setReturnSource(final Integer returnSource) {
        this.returnSource = returnSource;
    }

    public void setContactsName(final String contactsName) {
        this.contactsName = contactsName;
    }

    public void setContactsMobile(final String contactsMobile) {
        this.contactsMobile = contactsMobile;
    }

    public void setContactsRelation(final Integer contactsRelation) {
        this.contactsRelation = contactsRelation;
    }

    public void setBorrowerAttachList(final List<BorrowerAttach> borrowerAttachList) {
        this.borrowerAttachList = borrowerAttachList;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof BorrowerVO)) {
            return false;
        } else {
            BorrowerVO other = (BorrowerVO)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label143: {
                    Object this$sex = this.getSex();
                    Object other$sex = other.getSex();
                    if (this$sex == null) {
                        if (other$sex == null) {
                            break label143;
                        }
                    } else if (this$sex.equals(other$sex)) {
                        break label143;
                    }

                    return false;
                }

                Object this$age = this.getAge();
                Object other$age = other.getAge();
                if (this$age == null) {
                    if (other$age != null) {
                        return false;
                    }
                } else if (!this$age.equals(other$age)) {
                    return false;
                }

                Object this$education = this.getEducation();
                Object other$education = other.getEducation();
                if (this$education == null) {
                    if (other$education != null) {
                        return false;
                    }
                } else if (!this$education.equals(other$education)) {
                    return false;
                }

                label122: {
                    Object this$marry = this.getMarry();
                    Object other$marry = other.getMarry();
                    if (this$marry == null) {
                        if (other$marry == null) {
                            break label122;
                        }
                    } else if (this$marry.equals(other$marry)) {
                        break label122;
                    }

                    return false;
                }

                label115: {
                    Object this$industry = this.getIndustry();
                    Object other$industry = other.getIndustry();
                    if (this$industry == null) {
                        if (other$industry == null) {
                            break label115;
                        }
                    } else if (this$industry.equals(other$industry)) {
                        break label115;
                    }

                    return false;
                }

                Object this$income = this.getIncome();
                Object other$income = other.getIncome();
                if (this$income == null) {
                    if (other$income != null) {
                        return false;
                    }
                } else if (!this$income.equals(other$income)) {
                    return false;
                }

                Object this$returnSource = this.getReturnSource();
                Object other$returnSource = other.getReturnSource();
                if (this$returnSource == null) {
                    if (other$returnSource != null) {
                        return false;
                    }
                } else if (!this$returnSource.equals(other$returnSource)) {
                    return false;
                }

                label94: {
                    Object this$contactsName = this.getContactsName();
                    Object other$contactsName = other.getContactsName();
                    if (this$contactsName == null) {
                        if (other$contactsName == null) {
                            break label94;
                        }
                    } else if (this$contactsName.equals(other$contactsName)) {
                        break label94;
                    }

                    return false;
                }

                label87: {
                    Object this$contactsMobile = this.getContactsMobile();
                    Object other$contactsMobile = other.getContactsMobile();
                    if (this$contactsMobile == null) {
                        if (other$contactsMobile == null) {
                            break label87;
                        }
                    } else if (this$contactsMobile.equals(other$contactsMobile)) {
                        break label87;
                    }

                    return false;
                }

                Object this$contactsRelation = this.getContactsRelation();
                Object other$contactsRelation = other.getContactsRelation();
                if (this$contactsRelation == null) {
                    if (other$contactsRelation != null) {
                        return false;
                    }
                } else if (!this$contactsRelation.equals(other$contactsRelation)) {
                    return false;
                }

                Object this$borrowerAttachList = this.getBorrowerAttachList();
                Object other$borrowerAttachList = other.getBorrowerAttachList();
                if (this$borrowerAttachList == null) {
                    if (other$borrowerAttachList != null) {
                        return false;
                    }
                } else if (!this$borrowerAttachList.equals(other$borrowerAttachList)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof BorrowerVO;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        Object $sex = this.getSex();
         result = result * 59 + ($sex == null ? 43 : $sex.hashCode());
        Object $age = this.getAge();
        result = result * 59 + ($age == null ? 43 : $age.hashCode());
        Object $education = this.getEducation();
        result = result * 59 + ($education == null ? 43 : $education.hashCode());
        Object $marry = this.getMarry();
        result = result * 59 + ($marry == null ? 43 : $marry.hashCode());
        Object $industry = this.getIndustry();
        result = result * 59 + ($industry == null ? 43 : $industry.hashCode());
        Object $income = this.getIncome();
        result = result * 59 + ($income == null ? 43 : $income.hashCode());
        Object $returnSource = this.getReturnSource();
        result = result * 59 + ($returnSource == null ? 43 : $returnSource.hashCode());
        Object $contactsName = this.getContactsName();
        result = result * 59 + ($contactsName == null ? 43 : $contactsName.hashCode());
        Object $contactsMobile = this.getContactsMobile();
        result = result * 59 + ($contactsMobile == null ? 43 : $contactsMobile.hashCode());
        Object $contactsRelation = this.getContactsRelation();
        result = result * 59 + ($contactsRelation == null ? 43 : $contactsRelation.hashCode());
        Object $borrowerAttachList = this.getBorrowerAttachList();
        result = result * 59 + ($borrowerAttachList == null ? 43 : $borrowerAttachList.hashCode());
        return result;
    }

    public String toString() {
        return "BorrowerVO(sex=" + this.getSex() + ", age=" + this.getAge() + ", education=" + this.getEducation() + ", marry=" + this.getMarry() + ", industry=" + this.getIndustry() + ", income=" + this.getIncome() + ", returnSource=" + this.getReturnSource() + ", contactsName=" + this.getContactsName() + ", contactsMobile=" + this.getContactsMobile() + ", contactsRelation=" + this.getContactsRelation() + ", borrowerAttachList=" + this.getBorrowerAttachList() + ")";
    }
}
