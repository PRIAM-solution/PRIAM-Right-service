package priam.datarequest.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.ManyToOne;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class DataSubject {
    private int id;
    private String idRef;
    private int age;
    //@ManyToOne
    //private DSCategory category;
}
