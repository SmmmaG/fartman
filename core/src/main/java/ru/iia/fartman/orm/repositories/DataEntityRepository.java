package ru.iia.fartman.orm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.iia.fartman.orm.entity.DataEntity;


public interface DataEntityRepository extends JpaRepository<DataEntity, Long> {
}
