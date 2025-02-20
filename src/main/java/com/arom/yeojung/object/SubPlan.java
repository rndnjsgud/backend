package com.arom.yeojung.object;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class SubPlan extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long subPlanId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "dailyPlanId", nullable = false)
  private DailyPlan dailyPlan;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "locationId", nullable = true)
  private Location location;

  private String subPlanTitle;

  private String subPlanDescription;

  private LocalTime subPlanTime;

  @Column(nullable = false)
  private Long subPlanAuthorId;


}
