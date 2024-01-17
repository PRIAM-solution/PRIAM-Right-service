package priam.right.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class DataSubject {

    @lombok.Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class DataSubjectCategory {
        private int dataSubjectCategoryId;
        private String dataSubjectCategoryName;
        private String locationId;
    }
    private int dataSubjectId;
    private String referenceId;
    private int age;
    private DataSubjectCategory dataSubjectCategory;

    public int getDataSubjectCategoryId() {
        return dataSubjectCategory.dataSubjectCategoryId;
    }
    public String getDataSubjectCategoryName() {
        return dataSubjectCategory.dataSubjectCategoryName;
    }
}
