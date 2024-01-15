package priam.right.web;

import org.springframework.web.bind.annotation.*;
import priam.right.dto.*;
import priam.right.entities.RequestAnswer;
import priam.right.enums.DataRequestType;
import priam.right.services.DataRequestService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(path = "/api", produces = "application/json")
public class DataRequestRestController {
    private final DataRequestService dataRequestService;

    public DataRequestRestController(DataRequestService dataRequestService) {
        this.dataRequestService = dataRequestService;
    }

    @GetMapping(path = "/requestsRectification")
    public List<DataRequestResponseDTO> getListRequests() {
        return dataRequestService.getListRectificationRequests();
    }

    @GetMapping(path = "/requestsErasure")
    public List<DataRequestResponseDTO> getListErasureRequests() {
        return dataRequestService.getListErasureRequests();
    }

    @GetMapping(path = "right/dataRequest/{id}")
    public DataRequestResponseDTO getDataRequest(@PathVariable int id) {
        return dataRequestService.getDataRequest(id);
    }

    @PostMapping(path = "/right/rectificationRequest")
    public DataRequestResponseDTO RectificationRequest(@RequestBody DataRequestRequestDTO dataRequestDTO) {
        return dataRequestService.saveDataRequest(dataRequestDTO, DataRequestType.Rectification);
    }

    @PostMapping(path = "/right/erasureRequest")
    public DataRequestResponseDTO ErasureRequest(@RequestBody DataRequestRequestDTO dataRequestDTO) {
        return dataRequestService.saveDataRequest(dataRequestDTO, DataRequestType.Erasure);
    }

    @PostMapping(path = "/answerRectification")
    public DataRequestResponseDTO saveRectificationAnswer(@RequestBody RequestAnswerRequestDTO requestAnswer) {
        return dataRequestService.RectificationAnswer(requestAnswer.getIdDataRequest(),requestAnswer.isAnswer(),requestAnswer.getClaimAnswer());
    }

    @PostMapping(path = "/answerErasure")
    public DataRequestResponseDTO saveErasureAnswer(@RequestBody RequestAnswerRequestDTO requestAnswer) {
        return dataRequestService.ErasureAnswer(requestAnswer.getIdDataRequest(),requestAnswer.isAnswer(),requestAnswer.getClaimAnswer());
    }

    @GetMapping(path = "/requestsRectification/{id}")
    public List<DataRequestResponseDTO> getListDataRequestByDataSubject(@PathVariable(name = "id") int dataSubjectId) {
        return dataRequestService.getListDataRequestByDataSubjectId(dataSubjectId);
    }

    @GetMapping(path = "/personalDataValues/accessRight")
    public List<Map<String, String>> DataAccess(@RequestParam int idDS, @RequestParam String dataTypeName, @RequestParam List<String> attributes) {
        System.out.println("recupération c bn "+idDS+dataTypeName+attributes);
        return dataRequestService.DataAccess(idDS,dataTypeName,attributes);
    }
    @GetMapping(path = "/isAccepted")
    public boolean isDataRequestAcceptedForDataId(@RequestParam int dataSubjectId, @RequestParam int dataId) {
        return dataRequestService.isAccepted(dataSubjectId, dataId);
    }

    @PostMapping(path = "/right/recordAccessRequest")
    public void recordAccessRequest(@RequestBody AccessRequestRequestDTO accessRequestRequestDTO) {
        dataRequestService.saveAccessRequest(accessRequestRequestDTO.getIdRef(), accessRequestRequestDTO.getClaim(), accessRequestRequestDTO.getListOfSelectedDataId());
    }

    @GetMapping(path = "right/requestList")
    public List<RequestListDTO> getRequestListByFilters(@RequestParam Optional<List<String>> listOfSelectedTypeDataRequests, @RequestParam Optional<List<String>> listOfSelectedStatus, @RequestParam Optional<List<String>> listOfSelectedDataSubjectCategories) {
        return dataRequestService.getDataRequestByFilters(listOfSelectedTypeDataRequests.orElse(new ArrayList<>()), listOfSelectedStatus.orElse(new ArrayList<>()), listOfSelectedDataSubjectCategories.orElse(new ArrayList<>()));
    }

    @GetMapping(path = "right/requestDetail/{requestId}")
    public RequestDetailDTO getRequestDetail(@PathVariable int requestId) {
        return dataRequestService.getRequestDataDetail(requestId);
    }

    @GetMapping(path = "right/answerOfRequest/{requestId}")
    public RequestAnswer getRequestAnswer(@PathVariable int requestId) {
        return dataRequestService.getRequestAnswerByDataRequestId(requestId);
    }
}
