package com.arom.yeojung.object;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "CheckList")
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

    @ManyToOne
    @JoinColumn(name = "planId", nullable = false)
    private TotalPlan totalPlan;
}
