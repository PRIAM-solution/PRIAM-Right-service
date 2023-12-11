package priam.right.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

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
