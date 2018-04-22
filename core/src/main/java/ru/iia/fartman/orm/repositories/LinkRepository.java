package ru.iia.fartman.orm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.iia.fartman.orm.entity.ExecuteStart;
import ru.iia.fartman.orm.entity.Link;

import java.util.List;

public interface LinkRepository extends JpaRepository<Link, Long> {

	List<Link> findByExecuteStartAndLinkstringAndStarted(ExecuteStart start, String linkstring, boolean started);

}


