package com.arom.yeojung.controller;

import com.arom.yeojung.object.LocationType;
import com.arom.yeojung.object.dto.BudgetResponseDTO;
import com.arom.yeojung.object.dto.LocationRequestDTO;
import com.arom.yeojung.object.dto.LocationResponseDTO;
import com.arom.yeojung.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/locations")
public class LocationController {

    private final LocationService locationService;

    // 생성
    @PostMapping
    public ResponseEntity<LocationResponseDTO> createLocation(@RequestBody LocationRequestDTO dto) {
        LocationResponseDTO created = locationService.createLocation(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<LocationResponseDTO> getLocation(@PathVariable Long id) {
        LocationResponseDTO location = locationService.getLocation(id);
        return ResponseEntity.ok(location);
    }

    // 전체 조회
    @GetMapping
    public ResponseEntity<List<LocationResponseDTO>> getAllLocations() {
        List<LocationResponseDTO> locations = locationService.getAllLocations();
        return ResponseEntity.ok(locations);
    }

    //LocationType에 따른 조회
    @GetMapping("/byLocationType")
    public ResponseEntity<List<LocationResponseDTO>> getBudgetsByBudgetType(LocationType locationType) {
        List<LocationResponseDTO> responseList = locationService.getLocationsByType(locationType);
        return ResponseEntity.ok(responseList);
    }

    // 수정
    @PutMapping("/{id}")
    public ResponseEntity<LocationResponseDTO> updateLocation(@PathVariable Long id, @RequestBody LocationRequestDTO dto) {
        LocationResponseDTO updated = locationService.updateLocation(id, dto);
        return ResponseEntity.ok(updated);
    }


    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }
}