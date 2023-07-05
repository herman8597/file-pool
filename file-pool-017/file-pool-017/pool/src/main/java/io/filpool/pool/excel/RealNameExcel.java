package io.filpool.pool.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

@Data
public class RealNameExcel {

    @Excel(name = "用户账号", width = 15)
    private String account;

    @Excel(name = "真实姓名", width = 15)
    private String realName;

    @Excel(name = "国家", width = 15)
    private String areaName;

    @Excel(name = "证件号码", width = 15)
    private String cardNumber;

    @Excel(name = "身份证正面", width = 15)
    private String cardFront;

    @Excel(name = "身份证反面", width = 15)
    private String cardSide;

    @Excel(name = "审核状态", width = 15,replace = {"待审核_1","审核失败_2","审核通过_3"})
    private Integer authStatus;

    @Excel(name = "审核备注", width = 15)
    private String remark;

    @Excel(name = "提交时间", width = 15)
    private Date createTime;

    @Excel(name = "审核时间", width = 15)
    private Date authTime;

}
