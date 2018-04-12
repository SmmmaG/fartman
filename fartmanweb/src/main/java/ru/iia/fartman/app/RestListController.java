package ru.iia.fartman.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.iia.fartman.orm.entity.DataEntity;
import ru.iia.fartman.orm.entity.ExecuteStart;
import ru.iia.fartman.orm.repositories.DataEntityRepository;
import ru.iia.fartman.orm.repositories.ExecuteStartRepository;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RestListController {
    @Autowired
    ApplicationContext context;
    @Autowired
    DataEntityRepository dataEntityRepository;
    @Autowired
    ExecuteStartRepository executeStartRepository;

    @RequestMapping("/")
    List<DataEntity> getLastList() {
        List<ExecuteStart> starts = executeStartRepository.findAll(Sort.by("dateTime"));
        if (starts.isEmpty()) return null;
        Long laststartId = ((ExecuteStart) starts.get(0)).getUuid();
        List<DataEntity> allentityes = dataEntityRepository.findAll();
        List<DataEntity> result = new ArrayList<>();
        allentityes.forEach(dataEntity -> {
            if (dataEntity.getStart().getUuid().equals(laststartId))
                result.add(dataEntity);
        });
        return result;
    }

}
