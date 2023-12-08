package priam.datarequest.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import priam.datarequest.entities.DataSubject;

@FeignClient(name = "DATA-SUBJECT-SERVICE")
public interface DataSubjectRestClient {

    @GetMapping(path = "/api/DataSubject/{id}")
    DataSubject getDataSubject(@PathVariable(name = "id") int idDataSubject);

    @GetMapping(path = "/api/DataSubject/ref:{idRef}")
    DataSubject getDataSubjectByRef(@PathVariable (name = "idRef")String idRef);
}
