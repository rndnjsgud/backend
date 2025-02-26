package com.arom.yeojung.object.dto;

import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SubPlanResponseDTO {
  private Long subPlanId;
  private String subPlanTitle;
  private String subPlanDescription;
  private LocalTime subPlanTime;
}
