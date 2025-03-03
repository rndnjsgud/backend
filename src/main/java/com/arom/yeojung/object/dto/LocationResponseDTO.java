package com.arom.yeojung.object.dto;

import com.arom.yeojung.object.LocationType;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class LocationResponseDTO {
    private Long locationId;

    // 국가명
    private String country;

    //시
    private String city;

    //군, 구
    private String district;

    //주소
    private String address;

    //위도
    private BigDecimal latitude;

    //경도
    private BigDecimal longitude;

    //위치 정보 사용 계획 구분
    private LocationType locationType;
}
