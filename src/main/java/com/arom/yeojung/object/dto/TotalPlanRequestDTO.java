package com.arom.yeojung.object.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TotalPlanRequestDTO {

  @NotNull
  private String title;

  @NotNull
  private LocalDate startDate;

  @NotNull
  private LocalDate endDate;

  private Long totalBudget;

  private String totalPlanDescription;

  private int travelDuration;

  private String planThumbnailFileName;
  private String planThumbnailFileUrl;
}
