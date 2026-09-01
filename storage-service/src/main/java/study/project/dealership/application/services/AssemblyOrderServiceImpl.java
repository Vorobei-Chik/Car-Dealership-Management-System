package study.project.dealership.application.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.project.dealership.application.exception.NotFoundException;
import study.project.dealership.contracts.assembly.model.AssemblyOrderDTO;
import study.project.dealership.contracts.assembly.request.RequestCreateAssemblyOrder;
import study.project.dealership.contracts.assembly.request.RequestUpdateAssemblyOrder;
import study.project.dealership.domain.assembly.AssemblyOrder;
import study.project.dealership.domain.assembly.AssemblyOrderStatus;
import study.project.dealership.infrastructure.database.repository.AssemblyOrderJpaRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AssemblyOrderServiceImpl {

    private final AssemblyOrderJpaRepository assemblyOrderJpaRepository;

    @Transactional
    public AssemblyOrderDTO create(RequestCreateAssemblyOrder request) {
        AssemblyOrder entity = new AssemblyOrder();
        entity.setSourceOrderId(request.sourceOrderId());
        entity.setSourceOrderType(request.sourceOrderType());
        entity.setCarId(request.carId());
        entity.setModelId(request.modelId());
        entity.setEngineId(request.engineId());
        entity.setGearBoxId(request.gearBoxId());
        entity.setTransmissionId(request.transmissionId());
        entity.setWheelId(request.wheelId());
        entity.setInteriorId(request.interiorId());
        entity.setRudderId(request.rudderId());
        entity.setWarehouseAdminId(request.warehouseAdminId());
        entity.setStatus(AssemblyOrderStatus.valueOf(request.status()));
        return toDto(assemblyOrderJpaRepository.save(entity));
    }

    @Transactional(readOnly = true)
    public List<AssemblyOrderDTO> findAll() {
        return assemblyOrderJpaRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public AssemblyOrderDTO findById(UUID id) {
        return toDto(assemblyOrderJpaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Assembly order not found")));
    }

    @Transactional
    public AssemblyOrderDTO update(RequestUpdateAssemblyOrder request) {
        AssemblyOrder entity = assemblyOrderJpaRepository.findById(request.id())
                .orElseThrow(() -> new NotFoundException("Assembly order not found"));
        if (request.warehouseAdminId() != null) {
            entity.setWarehouseAdminId(request.warehouseAdminId());
        }
        entity.setStatus(AssemblyOrderStatus.valueOf(request.status()));
        return toDto(assemblyOrderJpaRepository.save(entity));
    }

    @Transactional
    public void delete(UUID id) {
        AssemblyOrder entity = assemblyOrderJpaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Assembly order not found"));
        entity.setRemoved(true);
        assemblyOrderJpaRepository.save(entity);
    }

    private AssemblyOrderDTO toDto(AssemblyOrder entity) {
        return new AssemblyOrderDTO(
                entity.getId(),
                entity.getSourceOrderId(),
                entity.getSourceOrderType(),
                entity.getCarId(),
                entity.getModelId(),
                entity.getEngineId(),
                entity.getGearBoxId(),
                entity.getTransmissionId(),
                entity.getWheelId(),
                entity.getInteriorId(),
                entity.getRudderId(),
                entity.getWarehouseAdminId(),
                entity.getStatus().name(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.isRemoved()
        );
    }
}
