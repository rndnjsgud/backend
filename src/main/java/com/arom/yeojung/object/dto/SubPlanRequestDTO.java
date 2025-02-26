package com.arom.yeojung.object.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubPlanRequestDTO {
  @NotBlank
  private String subPlanTitle;

  private String subPlanDescription;

  private LocalTime subPlanTime;

}
