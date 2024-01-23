package priam.right.services;


import priam.right.dto.*;
import priam.right.entities.DataRequestAnswer;
import priam.right.enums.DataRequestType;

import java.util.List;
import java.util.Map;

public interface DataRequestService {

    DataRequestResponseDTO getDataRequest(int dataRequestId);

    List<DataRequestResponseDTO> getListDataRequestByDataSubjectId(int dataSubjectId);

    DataRequestResponseDTO saveDataRequest(DataRequestRequestDTO dataRequestRequestDTO, DataRequestType dataRequestType);

    DataRequestResponseDTO RectificationAnswer(int dataRequestId, boolean answer, String dataRequestClaim);

    DataRequestResponseDTO ErasureAnswer(int dataRequestId, boolean answer, String claimAnswer);
    List<DataRequestResponseDTO> getListRectificationRequests();

    List<DataRequestResponseDTO> getListErasureRequests();
    List<Map<String, String>> DataAccess(int dataSubjectId, String dataTypeName, List<String> attributes);

    DataRequestResponseDTO saveAccessRequest(AccessRequestRequestDTO accessRequestRequestDTO);

    boolean isAccepted(int dataSubjectId, int dataId);

    List<RequestListDTO> getDataRequestByFilters(List<String> listOfSelectedTypeDataRequests, List<String> listOfSelectedStatus, List<String> listOfSelectedDataSubjectCategories);

    RequestDetailDTO getRequestDataDetail(int dataRequestId);
    DataRequestAnswer getRequestAnswerByDataRequestId(int dataRequestId);
}
