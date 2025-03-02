package com.arom.yeojung.object.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class DailyPlanResponseDTO {
    private Long dailyPlanId;
    private LocalDate dailyPlanDate;
    private int tripDayNumber;
}