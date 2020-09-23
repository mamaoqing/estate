package com.estate.sdzy.report.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FinanceMonthsReport {
    //本月总收费额、本月应收额、本月欠费额、本月收本月金额、本月收往期金额、往期欠费金额
    private String commName;
    private Long comm_id;
    private Long rule_id;
    private String ruleName;
    private BigDecimal totalPay;
    private BigDecimal receivable;
    private BigDecimal owed;
    private BigDecimal received;
    private BigDecimal previousPeriodReceived;
    private BigDecimal previousOwed;
}
