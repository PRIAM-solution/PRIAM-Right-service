package priam.right.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import priam.right.entities.Data;

import java.util.List;

@FeignClient(name ="DATA-SERVICE")
public interface DataRestClient {

    @GetMapping(path = "/api/dataId/{attribute}")
    public int getIdByName(@PathVariable String attribute);

    @GetMapping(path = "/api//datatype/data/{dataTypeId}")
    public String getDataTypeNameByDataTypeId(@PathVariable int dataTypeId);

    @GetMapping(path = "/api/personalData/{id}")
    Data getData(@PathVariable(name = "id") int id);

    @GetMapping(path = "/api/personalDataList")
    List<Data> getListPersonalData();

   /* @GetMapping(path = "/dataType/{id}")
    DataType getDataType(@PathVariable int id);*/

}
