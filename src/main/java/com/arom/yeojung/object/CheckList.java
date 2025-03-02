package com.arom.yeojung.object;

import com.arom.yeojung.object.dto.CheckListRequestDTO;
import com.arom.yeojung.object.dto.CheckListResponseDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class CheckList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long checkListId;

    //할일 목록
    @Column(nullable = false, length = 100)
    private String task;

    //완료 여부
    @Column(nullable = true)
    private Boolean isChecked;

    //체크리스트 설명
    @Column(nullable = true)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "planId", nullable = false)
    private TotalPlan totalPlan;
}


