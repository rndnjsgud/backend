package com.arom.yeojung.object;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigInteger;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class TotalPlan {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long totalPlanId;

  private String title;

  private LocalDate startDate;

  private LocalDate endDate;

  private BigInteger totalBudget;

  private String totalPlanDescription;

  private int travelDuration;

  //Todo: uer totalplan 중간 엔티티 만들기

}
