package ru.iia.fartman.orm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.iia.fartman.orm.entity.Site;

import java.util.List;

public interface SiteRepository extends JpaRepository<Site, Long> {

	List<Site> findByName(String name);

}
