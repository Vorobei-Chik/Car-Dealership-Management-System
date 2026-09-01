package study.project.dealership.presentation.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import study.project.dealership.contracts.mapping.PartMapper;
import study.project.dealership.contracts.part.PartService;
import study.project.dealership.contracts.part.model.*;
import study.project.dealership.contracts.part.request.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/parts")
@Validated
@RequiredArgsConstructor
public class PartController {

    private final PartService partService;

    @PreAuthorize("hasRole('WAREHOUSE_ADMIN')")
    @PostMapping("/link")
    public ResponseEntity<Void> linkPartToModel(@Valid @RequestBody RequestLinkPartToModel request) {
        partService.linkPartToModel(request);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('WAREHOUSE_ADMIN')")
    @PostMapping("/unlink")
    public ResponseEntity<Void> unlinkPartFromModel(@Valid @RequestBody RequestUnlinkPartFromModel request) {
        partService.unlinkPartFromModel(request);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('WAREHOUSE_ADMIN')")
    @PostMapping("/engines")
    public ResponseEntity<EngineDTO> createEngine(@Valid @RequestBody RequestCreateEngine request) {
        return ResponseEntity.ok(PartMapper.toDto(partService.createEngine(request)));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/engines/for-model")
    public ResponseEntity<List<EngineDTO>> getEnginesForModel(@RequestParam @NotNull UUID modelId) {
        List<EngineDTO> dtos = partService.getEnginesForModel(new RequestGetEnginesForModel(modelId)).stream()
                .map(PartMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @PreAuthorize("hasRole('WAREHOUSE_ADMIN')")
    @PutMapping("/engines")
    public ResponseEntity<EngineDTO> updateEngine(@Valid @RequestBody RequestUpdateEngine request) {
        return ResponseEntity.ok(PartMapper.toDto(partService.updateEngine(request)));
    }

    @PreAuthorize("hasRole('WAREHOUSE_ADMIN')")
    @PostMapping("/gearboxes")
    public ResponseEntity<GearBoxDTO> createGearBox(@Valid @RequestBody RequestCreateGearBox request) {
        return ResponseEntity.ok(PartMapper.toDto(partService.createGearBox(request)));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/gearboxes/for-model")
    public ResponseEntity<List<GearBoxDTO>> getGearBoxesForModel(@RequestParam @NotNull UUID modelId) {
        List<GearBoxDTO> dtos = partService.getGearBoxes(new RequestGetGearBoxesForModel(modelId)).stream()
                .map(PartMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @PreAuthorize("hasRole('WAREHOUSE_ADMIN')")
    @PutMapping("/gearboxes")
    public ResponseEntity<GearBoxDTO> updateGearBox(@Valid @RequestBody RequestUpdateGearBox request) {
        return ResponseEntity.ok(PartMapper.toDto(partService.updateGearBox(request)));
    }

    @PreAuthorize("hasRole('WAREHOUSE_ADMIN')")
    @PostMapping("/interiors")
    public ResponseEntity<InteriorDTO> createInterior(@Valid @RequestBody RequestCreateInterior request) {
        return ResponseEntity.ok(PartMapper.toDto(partService.createInterior(request)));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/interiors/for-model")
    public ResponseEntity<List<InteriorDTO>> getInteriorsForModel(@RequestParam @NotNull UUID modelId) {
        List<InteriorDTO> dtos = partService.getInteriors(new RequestGetInteriorsForModel(modelId)).stream()
                .map(PartMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @PreAuthorize("hasRole('WAREHOUSE_ADMIN')")
    @PutMapping("/interiors")
    public ResponseEntity<InteriorDTO> updateInterior(@Valid @RequestBody RequestUpdateInterior request) {
        return ResponseEntity.ok(PartMapper.toDto(partService.updateInterior(request)));
    }

    @PreAuthorize("hasRole('WAREHOUSE_ADMIN')")
    @PostMapping("/rudders")
    public ResponseEntity<RudderDTO> createRudder(@Valid @RequestBody RequestCreateRudder request) {
        return ResponseEntity.ok(PartMapper.toDto(partService.createRudder(request)));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/rudders/for-model")
    public ResponseEntity<List<RudderDTO>> getRuddersForModel(@RequestParam @NotNull UUID modelId) {
        List<RudderDTO> dtos = partService.getRudders(new RequestGetRuddersForModel(modelId)).stream()
                .map(PartMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @PreAuthorize("hasRole('WAREHOUSE_ADMIN')")
    @PutMapping("/rudders")
    public ResponseEntity<RudderDTO> updateRudder(@Valid @RequestBody RequestUpdateRudder request) {
        return ResponseEntity.ok(PartMapper.toDto(partService.updateRudder(request)));
    }

    @PreAuthorize("hasRole('WAREHOUSE_ADMIN')")
    @PostMapping("/transmissions")
    public ResponseEntity<TransmissionDTO> createTransmission(@Valid @RequestBody RequestCreateTransmission request) {
        return ResponseEntity.ok(PartMapper.toDto(partService.createTransmission(request)));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/transmissions/for-model")
    public ResponseEntity<List<TransmissionDTO>> getTransmissionsForModel(@RequestParam @NotNull UUID modelId) {
        List<TransmissionDTO> dtos = partService.getTransmissions(new RequestGetTransmissionsForModel(modelId)).stream()
                .map(PartMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @PreAuthorize("hasRole('WAREHOUSE_ADMIN')")
    @PutMapping("/transmissions")
    public ResponseEntity<TransmissionDTO> updateTransmission(@Valid @RequestBody RequestUpdateTransmission request) {
        return ResponseEntity.ok(PartMapper.toDto(partService.updateTransmission(request)));
    }

    @PreAuthorize("hasRole('WAREHOUSE_ADMIN')")
    @PostMapping("/wheels")
    public ResponseEntity<WheelDTO> createWheel(@Valid @RequestBody RequestCreateWheel request) {
        return ResponseEntity.ok(PartMapper.toDto(partService.createWheel(request)));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/wheels/for-model")
    public ResponseEntity<List<WheelDTO>> getWheelsForModel(@RequestParam @NotNull UUID modelId) {
        List<WheelDTO> dtos = partService.getWheels(new RequestGetWheelsForModel(modelId)).stream()
                .map(PartMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @PreAuthorize("hasRole('WAREHOUSE_ADMIN')")
    @PutMapping("/wheels")
    public ResponseEntity<WheelDTO> updateWheel(@Valid @RequestBody RequestUpdateWheel request) {
        return ResponseEntity.ok(PartMapper.toDto(partService.updateWheel(request)));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{partId}")
    public ResponseEntity<PartDTO> findPart(@PathVariable @NotNull UUID partId) {
        return ResponseEntity.ok(PartMapper.toBaseDto(partService.findPart(new RequestFindPart(partId))));
    }

    @PreAuthorize("hasRole('WAREHOUSE_ADMIN')")
    @DeleteMapping("/{partId}")
    public ResponseEntity<Void> removePart(@PathVariable @NotNull UUID partId) {
        partService.removePart(new RequestRemovePart(partId));
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('WAREHOUSE_ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<PartDTO> updatePart(@Valid @RequestBody RequestUpdatePart request) {
        return ResponseEntity.ok(PartMapper.toBaseDto(partService.updatePart(request)));
    }
}
