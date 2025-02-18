package com.arom.yeojung.object;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "Location")
@Getter
@Setter
@RequiredArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long locationId;

    //시
    @Column(nullable = false, length = 50)
    private String locationCity;

    //군, 구
    @Column(nullable = false, length = 50)
    private String locationDistrict;

    //위도
    @Column(nullable = false, precision = 9, scale = 6)
    private BigDecimal latitude;

    //경도
    @Column(nullable = false, precision = 9, scale = 6)
    private BigDecimal longitude;
}
