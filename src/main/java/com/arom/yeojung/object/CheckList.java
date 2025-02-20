package com.arom.yeojung.object;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class CheckList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long checkListId;

    //할일 목록
    @Column(nullable = true, length = 100)
    private String task;

    //완료 여부
    @Column(nullable = true)
    private Boolean isChecked;

    //체크리스트 설명
    @Column(nullable = true)
    private String discription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "planId", nullable = false)
    private TotalPlan totalPlan;
}
