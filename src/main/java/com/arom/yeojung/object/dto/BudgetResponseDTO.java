package com.arom.yeojung.object.dto;

import com.arom.yeojung.object.BudgetCategory;
import com.arom.yeojung.object.BudgetType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BudgetResponseDTO {
    @NotNull
    private Long budgetId;
    private BudgetCategory budgetCategory;
    private String budgetName;
    private Long BudgetAmount;
    private BudgetType budgetType;
    private Long subPlanId;
}
