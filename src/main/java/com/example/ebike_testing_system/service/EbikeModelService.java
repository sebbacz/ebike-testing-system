package com.example.ebike_testing_system.service;

import com.example.ebike_testing_system.dto.EbikeModelDto;
import com.example.ebike_testing_system.dto.EbikeModelProjection;
import com.example.ebike_testing_system.exception.NotFoundException;
import com.example.ebike_testing_system.model.EbikeModel;
import com.example.ebike_testing_system.repository.EbikeModelRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EbikeModelService {

    private final EbikeModelRepository eModelRepository;

    public EbikeModelService(EbikeModelRepository eModelRepository) {
        this.eModelRepository = eModelRepository;
    }

    public List<EbikeModel> findAll() {
        return eModelRepository.findAll();
    }

    public List<EbikeModelProjection> findAllProjected() {
        return eModelRepository.findAllProjected();
    }

    public EbikeModelProjection getProjectedModelById(int modelId) {
        return eModelRepository.findProjectedById(modelId)
                .orElseThrow(() -> new NotFoundException("Model not found"));
    }


    public EbikeModel findById(int modelId) {
        return eModelRepository.findById(modelId).orElseThrow(() -> new NotFoundException("eBikeModel not found"));
    }

    public List<EbikeModel> findByName(String name) {
        return this.eModelRepository.findByNameContainingIgnoreCase(name);
    }

    public EbikeModel addModel(EbikeModel model) {
        return eModelRepository.save(model);
    }

    public EbikeModel addModel(String name, int batteryCapacity, int maxSupport, int enginePowerMax,
                               int enginePowerNominal, int engineTorque) {

        final EbikeModel newModel = new EbikeModel();
        newModel.setName(name);
        newModel.setBatteryCapacity(batteryCapacity);
        newModel.setMaxSupport(maxSupport);
        newModel.setEnginePowerMax(enginePowerMax);
        newModel.setEnginePowerNominal(enginePowerNominal);
        newModel.setEngineTorque(engineTorque);
        eModelRepository.save(newModel);

        return newModel;
    }

    public EbikeModel updateModel(int id, EbikeModelDto dto) {
        EbikeModel model = findById(id); // throws if not found

        model.setName(dto.getName());
        model.setBatteryCapacity(dto.getBatteryCapacity());
        model.setMaxSupport(dto.getMaxSupport());
        model.setEnginePowerMax(dto.getEnginePowerMax());
        model.setEnginePowerNominal(dto.getEnginePowerNominal());
        model.setEngineTorque(dto.getEngineTorque());

        return eModelRepository.save(model);
    }


    public EbikeModel patchModelName(int id, String name) {
        final EbikeModel model = findById(id);
        model.setName(name);
        return eModelRepository.save(model);
    }

    @Transactional
    public void deleteModel(int modelId) {
        try {
            final EbikeModel model = eModelRepository.findById(modelId)
                    .orElseThrow(() -> new NotFoundException("Model not found!"));
            eModelRepository.deleteById(modelId);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalStateException("Cannot delete model because it is referenced by existing ebikes!");
        }
    }


}
