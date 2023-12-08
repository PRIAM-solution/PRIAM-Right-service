package priam.datarequest.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import priam.datarequest.enums.Source;

import javax.persistence.Column;
import javax.persistence.Id;

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
    private String primary_key_name;
}
