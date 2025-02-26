package com.arom.yeojung.object.dto;

import com.arom.yeojung.object.BudgetCategory;
import com.arom.yeojung.object.BudgetType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BudgetResponseDTO {
    @NotNull
    private Long budgetId;
    private BudgetCategory budgetCategory;
    private String budgetName;
    private Long budgetAmount;
    private BudgetType budgetType;
    private Long subPlanId;
}
