package com.arom.yeojung.object;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long locationId;

    // 국가명 추가 (예: "대한민국", "USA")
    @Column(nullable = false, length = 50)
    private String country;

    //시
    @Column(nullable = false, length = 50)
    private String city;

    //군, 구
    @Column(nullable = false, length = 50)
    private String district;

    //주소
    @Column(nullable = false)
    private String address;

    //위도
    @Column(nullable = false, precision = 9, scale = 6)
    private BigDecimal latitude;

    //경도
    @Column(nullable = false, precision = 9, scale = 6)
    private BigDecimal longitude;

    //위치 정보 사용 계획 구분   => TOTAL 일 시, totalPlan에서 사용하는 위치정보, SUB일 시 subPlan에서 사용하는 위치정보
    @Enumerated(EnumType.STRING)
    private LocationType locationType;
}
