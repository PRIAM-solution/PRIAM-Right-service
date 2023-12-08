package priam.datarequest.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;
@AllArgsConstructor
@NoArgsConstructor
@lombok.Data
public class DataType {

    private int dataTypeId;
    private String dataTypeName;
    private String primaryKeyName;
    private Collection<Data> dataList;
}
