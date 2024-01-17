package priam.right.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessRequestRequestDTO {
    private String referenceId;
    private String claim;
    private ArrayList<Integer> listOfSelectedDataId;
}
