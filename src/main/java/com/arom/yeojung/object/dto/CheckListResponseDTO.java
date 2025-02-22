package com.arom.yeojung.object.dto;

import com.arom.yeojung.object.TotalPlan;
import lombok.Data;

@Data
public class CheckListResponseDTO {
    private Long checkListId;
    private String task;
    private Boolean isChecked;
    private String description;
    private Long totalPlanId;
}
