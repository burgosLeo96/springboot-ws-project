package com.protalento.protalentodbproject.controller;

import com.protalento.protalentodbproject.entities.CarEntity;
import com.protalento.protalentodbproject.model.CarModel;
import com.protalento.protalentodbproject.repositories.CarRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/protalento")
public class ProtalentoDbController {

    private final CarRepository carRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping(path = "/cars")
    public ResponseEntity<CarModel> createCar(@RequestBody @Valid CarModel carModel) {
        log.info("Starting process to create a new Car");

        CarEntity carEntity = new CarEntity();
        carEntity.setId(UUID.randomUUID().toString());
        carEntity.setMarca(carModel.getMarca());
        carEntity.setModelo(carModel.getModelo());
        carEntity.setPlaca(carModel.getPlaca());

        carRepository.save(carEntity);
        log.info("Car saved successfully.");

        carModel.setId(carEntity.getId());
        carModel.setAction("added");
        this.sendEventToBroker(carModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(carModel);
    }

    @GetMapping(path = "/cars")
    public ResponseEntity<List<CarModel>> retrieveCars() {
        List<CarModel> responseList = new ArrayList<>();
        carRepository
                .findAll()
                .iterator()
                .forEachRemaining(carEntity -> responseList.add(ProtalentoDbController.convertCarEntityToModel(carEntity)));

        return ResponseEntity.ok(responseList);
    }

    @DeleteMapping(path = "/cars/{carId}")
    public ResponseEntity<Void> deleteCar(@PathVariable String carId) {
        log.info("Deleting car from database");
        Optional<CarEntity> carEntityOptional = carRepository.findById(carId);
        if(carEntityOptional.isPresent()){
            log.info("Car founded, deleting");
            CarEntity carEntity = carEntityOptional.get();
            CarModel carModel = convertCarEntityToModel(carEntity);
            carRepository.delete(carEntity);

            log.info("Car deleted successfully");
            carModel.setAction("deleted");
            this.sendEventToBroker(carModel);

            return ResponseEntity.noContent().build();
        }
        else {
            log.error("No record was found with the id specified");
            return ResponseEntity.notFound().build();
        }
    }

    private void sendEventToBroker(CarModel carModel) {
        log.info("Sending event to topic");
        simpMessagingTemplate.convertAndSend("/cars-topic", carModel);
        log.info("Event sent to topic successfully");
    }

    private static CarModel convertCarEntityToModel(CarEntity carEntity) {
        return CarModel
                .builder()
                .id(carEntity.getId())
                .marca(carEntity.getMarca())
                .modelo(carEntity.getModelo())
                .placa(carEntity.getPlaca())
                .build();
    }

}
