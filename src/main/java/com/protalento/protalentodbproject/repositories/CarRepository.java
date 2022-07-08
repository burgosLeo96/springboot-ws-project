package com.protalento.protalentodbproject.repositories;

import com.protalento.protalentodbproject.entities.CarEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends CrudRepository<CarEntity, String> {
}
