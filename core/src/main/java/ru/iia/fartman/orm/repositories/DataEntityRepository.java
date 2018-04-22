package ru.iia.fartman.orm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.iia.fartman.orm.entity.DataEntity;
import ru.iia.fartman.orm.entity.ExecuteStart;
import ru.iia.fartman.orm.entity.Link;

import java.util.List;


public interface DataEntityRepository extends JpaRepository<DataEntity, Long> {

	List<Link> findByExecuteStartAndLink(ExecuteStart start, Link link);

}
