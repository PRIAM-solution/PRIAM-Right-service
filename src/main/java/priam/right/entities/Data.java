package priam.right.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import priam.right.enums.Source;

@AllArgsConstructor
@NoArgsConstructor
@lombok.Data
public class Data {
    private int id;
    private String attribute;
    private boolean isPersonal;
    private String Category;
    private Source source;
    private int data_type_id;
    private String data_type_name;
}
