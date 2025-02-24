package com.arom.yeojung.controller;

import com.arom.yeojung.object.dto.LocationRequestDTO;
import com.arom.yeojung.object.dto.LocationResponseDTO;
import com.arom.yeojung.service.LocationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService){
        this.locationService = locationService;
    }
    // 생성
    @PostMapping
    public LocationResponseDTO createLocation(@RequestBody LocationRequestDTO dto) {
        return locationService.createLocation(dto);
    }

    // 단건 조회
    @GetMapping("/{id}")
    public LocationResponseDTO getLocation(@PathVariable Long id) {
        return locationService.getLocation(id);
    }

    // 전체 조회
    @GetMapping
    public List<LocationResponseDTO> getAllLocations() {
        return locationService.getAllLocations();
    }

    // 수정
    @PutMapping("/{id}")
    public LocationResponseDTO updateLocation(@PathVariable Long id, @RequestBody LocationRequestDTO dto) {
        return locationService.updateLocation(id, dto);
    }

    // 삭제
    @DeleteMapping("/{id}")
    public void deleteLocation(@PathVariable Long id) {
        locationService.deleteLocation(id);
    }
}