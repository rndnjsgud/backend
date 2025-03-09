package com.arom.yeojung.object.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TotalPlanResponseDTO {
  private Long totalPlanId;
  private String title;
  private LocalDate startDate;
  private LocalDate endDate;
  private Long totalBudget;
  private String totalPlanDescription;
  private int travelDuration;

  private String planThumbnailFileName;
  private String planThumbnailFileUrl;
}
