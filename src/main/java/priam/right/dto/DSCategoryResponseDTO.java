package priam.right.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@lombok.Data
public class DSCategoryResponseDTO {
    private int dataSubjectCategoryId;
    private String dataSubjectCategoryName;
    private String locationId;
}
