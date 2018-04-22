package ru.iia.fartman.orm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.iia.fartman.orm.entity.ExecuteStart;
import ru.iia.fartman.orm.entity.Link;
import ru.iia.fartman.orm.entity.Site;

import java.util.List;

public interface ExecuteStartRepository extends JpaRepository<ExecuteStart, Long> {

	List<Link> findBySiteAndStartedAndFinished(Site site, boolean started, boolean finished);

}
