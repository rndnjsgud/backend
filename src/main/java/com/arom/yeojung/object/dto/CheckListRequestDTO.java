package com.arom.yeojung.object.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CheckListRequestDTO {
    @NotNull
    private String task;
    private Boolean isChecked;
    private String description;
    private Long totalPlanId;
}
