package com.arom.yeojung.object;

import jakarta.persistence.*;
import jdk.jfr.Unsigned;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Budget")
@Getter
@Setter
@RequiredArgsConstructor
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long budgetId;

    //예산 카테고리
    @Enumerated(EnumType.STRING)
    private BudgetCategory budgetCategory;

    //예산 이름
    @Column(nullable = false, length = 50)
    private String budgetName;

    //예산 금액
    @Column(nullable = false)
    private Long budgetAmount;

    //예산 유형(전체 예산인지, 당일 예산인지)
    @Enumerated(EnumType.STRING)
    private BudgetType budgetType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subPlanId", nullable = false)
    private SubPlan subPlan;
}
