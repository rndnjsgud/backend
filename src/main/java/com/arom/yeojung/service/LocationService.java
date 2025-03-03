package com.arom.yeojung.service;

import com.arom.yeojung.object.Location;
import com.arom.yeojung.object.LocationType;
import com.arom.yeojung.object.dto.LocationRequestDTO;
import com.arom.yeojung.object.dto.LocationResponseDTO;
import com.arom.yeojung.repository.CheckListRepository;
import com.arom.yeojung.repository.LocationRepository;
import com.arom.yeojung.repository.TotalPlanRepository;
import com.arom.yeojung.util.exception.CustomException;
import com.arom.yeojung.util.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;
    // 생성
    @Transactional
    public LocationResponseDTO createLocation(LocationRequestDTO dto) {
        Location location = Location.builder()
                .country(dto.getCountry())
                .city(dto.getCity())
                .district(dto.getDistrict())
                .address(dto.getAddress())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .locationType(dto.getLocationType())
                .build();

        Location saved = locationRepository.save(location);
        return mapToResponseDTO(saved);
    }

    // 조회 (단건)
    @Transactional
    public LocationResponseDTO getLocation(Long id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.LOCATION_NOT_FOUND));
        return mapToResponseDTO(location);
    }

    // 전체 조회
    @Transactional
    public List<LocationResponseDTO> getAllLocations() {
        return locationRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // LocationType에 따른 조회
    @Transactional
    public List<LocationResponseDTO> getLocationsByType(LocationType locationType){
        return locationRepository.findByLocationType(locationType).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // 수정
    @Transactional
    public LocationResponseDTO updateLocation(Long id, LocationRequestDTO dto) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.LOCATION_NOT_FOUND));

        location.setCity(dto.getCity());
        location.setDistrict(dto.getDistrict());
        location.setAddress(dto.getAddress());
        location.setLatitude(dto.getLatitude());
        location.setLongitude(dto.getLongitude());
        location.setLocationType(dto.getLocationType());

        Location updated = locationRepository.save(location);
        return mapToResponseDTO(updated);
    }

    // 삭제
    @Transactional
    public void deleteLocation(Long id) {
        if(!locationRepository.existsById(id)){
            throw new CustomException(ErrorCode.LOCATION_NOT_FOUND);
        }
        locationRepository.deleteById(id);
    }

    // Entity -> DTO 매핑
    private LocationResponseDTO mapToResponseDTO(Location location) {
        return LocationResponseDTO.builder()
                .locationId(location.getLocationId())
                .city(location.getCity())
                .district(location.getDistrict())
                .address(location.getAddress())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .locationType(location.getLocationType())
                .build();
    }
}