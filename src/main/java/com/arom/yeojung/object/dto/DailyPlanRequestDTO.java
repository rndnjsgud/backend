package com.arom.yeojung.object.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Builder
@Getter
@Setter
public class DailyPlanRequestDTO {
    private LocalDate dailyPlanDate;
    private int tripDayNumber;
}