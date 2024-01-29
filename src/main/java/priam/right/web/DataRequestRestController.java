package priam.right.web;

import org.bouncycastle.cert.ocsp.Req;
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

    @PostMapping(path = "/right/accessRequest")
    public DataRequestResponseDTO saveAccessRequest(@RequestBody AccessRequestRequestDTO accessRequestRequestDTO) {
        return dataRequestService.saveAccessRequest(accessRequestRequestDTO);
    }

    @PostMapping(path = "/right/rectificationRequest")
    public DataRequestResponseDTO saveRectificationRequest(@RequestBody DataRequestRequestDTO dataRequestDTO) {
        return dataRequestService.saveDataRequest(dataRequestDTO, DataRequestType.Rectification);
    }

    @PostMapping(path = "/right/erasureRequest")
    public DataRequestResponseDTO saveErasureRequest(@RequestBody DataRequestRequestDTO dataRequestDTO) {
        return dataRequestService.saveDataRequest(dataRequestDTO, DataRequestType.Erasure);
    }

    @GetMapping(path = "/right/answer/{requestId}")
    public RequestAnswer getRequestAnswer(@PathVariable long requestId) {
        return dataRequestService.getRequestAnswer(requestId);
    }
    @PostMapping(path = "/right/answer")
    public RequestAnswer saveRequestAnswer(@RequestBody RequestAnswerRequestDTO requestAnswer) {
        return dataRequestService.saveRequestAnswer(requestAnswer);
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
