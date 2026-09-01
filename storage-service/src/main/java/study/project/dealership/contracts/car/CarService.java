package study.project.dealership.contracts.car;

import study.project.dealership.contracts.car.request.*;
import study.project.dealership.domain.car.Car;
import study.project.dealership.domain.car.Configuration;
import study.project.dealership.domain.car.Model;

import java.util.List;

public interface CarService {
    Car createCar(RequestCreateCar request);

    List<Car> createStandardCar(RequestCreateStandardCar request);

    Car findCar(RequestFindCar request);

    List<Car> findFilteredCars(RequestFindFilteredCars request);

    List<Car> getAllCars();

    void removeCar(RequestRemoveCar request);

    Car updateCar(RequestUpdateCar request);

    Configuration createStandardConfiguration(RequestCreateStandardConfiguration request);

    Configuration findStandardConfiguration(RequestFindStandardConfiguration request);

    Configuration findStandardConfigurationByModel(RequestFindStandardConfigurationByModel request);

    List<Configuration> getAllStandardConfigurations();

    void removeStandardConfiguration(RequestRemoveStandardConfiguration request);

    Configuration updateStandardConfiguration(RequestUpdateStandardConfiguration request);

    Model createModel(RequestCreateModel request);

    Model findModel(RequestFindModel request);

    List<Model> getAllModels();

    void removeModel(RequestRemoveModel request);

    Model updateModel(RequestUpdateModel request);

    Car addCarToTestDrive(RequestAddCarToTestDrive request);

    void removeCarFromTestDrive(RequestRemoveCarFromTestDrive request);
}
