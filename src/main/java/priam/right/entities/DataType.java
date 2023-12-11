package priam.right.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

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
