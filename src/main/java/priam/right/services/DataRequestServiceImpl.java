package priam.right.services;

import org.springframework.stereotype.Service;
import priam.right.dto.DSCategoryResponseDTO;
import priam.right.dto.DataRequestResponseDTO;
import priam.right.dto.RequestDetailDTO;
import priam.right.dto.RequestListDTO;
import priam.right.entities.*;
import priam.right.enums.AnswerType;
import priam.right.enums.TypeDataRequest;
import priam.right.mappers.DataRequestMapper;
import priam.right.openfeign.DataRestClient;
import priam.right.openfeign.ActorRestClient;

import priam.right.openfeign.ProviderRestClient;
import priam.right.repositories.DataRequestDataRepository;
import priam.right.repositories.DataRequestRepository;
import priam.right.repositories.RequestAnswerRepository;

import javax.annotation.Generated;
import javax.transaction.Transactional;
import java.util.*;

@Generated(
        value = "org.mapstruct.ap.MappingProcessor",
        date = "2021-05-23T23:03:41+0530"
)

@Service
@Transactional
public class DataRequestServiceImpl implements DataRequestService {
    private DataRequestRepository dataRequestRepository;
    private DataRequestMapper dataRequestMapper;
    private DataRestClient dataRestClient;
    private ActorRestClient actorRestClient;
    private ProviderRestClient providerRestClient;
    private RequestAnswerRepository requestAnswerRepository;
    private DataRequestDataRepository dataRequestDataRepository;

    public DataRequestServiceImpl(DataRequestRepository dataRequestRepository, DataRequestMapper dataRequestMapper,
                                  ProviderRestClient providerRestClient, DataRestClient dataRestClient, ActorRestClient actorRestClient, RequestAnswerRepository requestAnswerRepository,
                                  DataRequestDataRepository dataRequestDataRepository) {
        this.dataRequestRepository = dataRequestRepository;
        this.dataRequestMapper = dataRequestMapper;
        this.dataRestClient = dataRestClient;
        this.actorRestClient = actorRestClient;
        this.providerRestClient = providerRestClient;
        this.requestAnswerRepository = requestAnswerRepository;
        this.dataRequestDataRepository = dataRequestDataRepository;
    }

    public List<Map<String, String>> DataAccess(int idDS, String dataTypeName, List<String> attributes){
        System.out.println(providerRestClient.getPersonalDataValues(idDS, dataTypeName, attributes));
        return providerRestClient.getPersonalDataValues(idDS, dataTypeName, attributes);
    }
    @Override
    public DataRequestResponseDTO saveRectificationRequest(String idRef,String attribute, String newValue,  String claim, String primaryKeyaValue) {

        DataRequest dataRequest = new DataRequest();

        dataRequest.setNewValue(newValue);

        int idd = dataRestClient.getIdByName(attribute);
        Data data = dataRestClient.getData(idd);

        DataSubject dataSubject = actorRestClient.getDataSubjectByRef(idRef);
        dataRequest.setDataSubject(dataSubject);

        dataRequest.setClaimDate(new Date());
        dataRequest.setClaim(claim);
        dataRequest.setType(TypeDataRequest.Rectification);

        dataRequest.setResponse(false);

        dataRequest.setIsolated(true);
        dataRequest.setDataSubjectId(dataSubject.getId());

        dataRequest.setPrimaryKeyValue(primaryKeyaValue);

        DataRequest result = dataRequestRepository.save(dataRequest);

        // DataRequestData
        DataRequestData drd = new DataRequestData();
        drd.setDataRequestId(result.getId());
        drd.setDataId(data.getId());
        dataRequestDataRepository.save(drd);

        ArrayList<Data> datas = new ArrayList<>();
        datas.add(data);

        // Response DTO
        DataRequestResponseDTO response = new DataRequestResponseDTO(result, datas);
        return response;
    }

    @Override
    public DataRequestResponseDTO saveErasureRequest(String idRef,String attribute, String claim, String primaryKeyaValue) {

        DataRequest dataRequest = new DataRequest();

        int idd = dataRestClient.getIdByName(attribute);
        Data data = dataRestClient.getData(idd);

        DataSubject dataSubject2 = actorRestClient.getDataSubjectByRef(idRef);
        dataRequest.setDataSubject(dataSubject2);

        dataRequest.setClaimDate(new Date());
        dataRequest.setClaim(claim);
        dataRequest.setType(TypeDataRequest.Forgotten);

        dataRequest.setResponse(false);

        dataRequest.setIsolated(true);
        dataRequest.setDataSubjectId(dataSubject2.getId());

        dataRequest.setPrimaryKeyValue(primaryKeyaValue);

        DataRequest result = dataRequestRepository.save(dataRequest);

        // DataRequestData
        DataRequestData drd = new DataRequestData();
        drd.setDataRequestId(result.getId());
        drd.setDataId(data.getId());
        dataRequestDataRepository.save(drd);

        List<Data> datas = new ArrayList<>();
        datas.add(data);

        // Response DTO
        DataRequestResponseDTO response = new DataRequestResponseDTO(result, datas);
        return response;
    }

    @Override
    public DataRequestResponseDTO getDataRequest(int id) {
        DataRequest dataRequest = dataRequestRepository.getById(id);
        ArrayList<Data> datas = new ArrayList<>();

        List<Integer> dataIds = new ArrayList<>();
        dataIds = dataRequestDataRepository.findDataIdsByDataRequestId(id);
        dataIds.forEach(dataId -> {
            Data data = dataRestClient.getData(dataId);
            datas.add(data);
        });

        DataSubject dataSubject = actorRestClient.getDataSubject(dataRequest.getDataSubjectId());
        dataRequest.setDataSubject(dataSubject);

        // Response DTO
        DataRequestResponseDTO response = new DataRequestResponseDTO(dataRequest, datas);
        return response;
    }

    @Override
    public List<DataRequestResponseDTO> getListDataRequestByDataSubjectId(int dataSubjectId) {
        List<DataRequestResponseDTO> response = new ArrayList<>();
        List<DataRequest> dataRequestList = dataRequestRepository.findByDataSubjectId(dataSubjectId);

        for (DataRequest dataRequest : dataRequestList)
        {
            // Get all Data object
            ArrayList<Data> datas = new ArrayList<>();
            List<Integer> dataIds = new ArrayList<>();
            dataIds = dataRequestDataRepository.findDataIdsByDataRequestId(dataRequest.getId());
            dataIds.forEach(dataId -> {
                Data data = dataRestClient.getData(dataId);
                datas.add(data);
            });

            DataSubject dataSubject = actorRestClient.getDataSubject(dataRequest.getDataSubjectId());
            dataRequest.setDataSubject(dataSubject);
            response.add(new DataRequestResponseDTO(dataRequest, datas));
        }

        return response;
    }

    @Override
    public List<DataRequestResponseDTO> getListRectificationRequests() {
        List<DataRequestResponseDTO> response = new ArrayList<>();
        List<DataRequest> dataRequestList = dataRequestRepository.findByType(TypeDataRequest.Rectification);

        for (DataRequest dataRequest : dataRequestList)
        {

            // Get all Data object
            ArrayList<Data> datas = new ArrayList<>();
            List<Integer> dataIds = new ArrayList<>();
            dataIds = dataRequestDataRepository.findDataIdsByDataRequestId(dataRequest.getId());
            dataIds.forEach(dataId -> {
                Data data = dataRestClient.getData(dataId);
                datas.add(data);
            });

            DataSubject dataSubject = actorRestClient.getDataSubject(dataRequest.getDataSubjectId());
            dataRequest.setDataSubject(dataSubject);
            response.add(new DataRequestResponseDTO(dataRequest, datas));

        }
        return response;
    }

    @Override
    public List<DataRequestResponseDTO> getListErasureRequests() {
        List<DataRequestResponseDTO> response = new ArrayList<>();
        List<DataRequest> dataRequestList = dataRequestRepository.findByType(TypeDataRequest.Forgotten);

        for (DataRequest dataRequest : dataRequestList)
        {

            // Get all Data object
            ArrayList<Data> datas = new ArrayList<>();
            List<Integer> dataIds = new ArrayList<>();
            dataIds = dataRequestDataRepository.findDataIdsByDataRequestId(dataRequest.getId());
            dataIds.forEach(dataId -> {
                Data data = dataRestClient.getData(dataId);
                datas.add(data);
            });

            DataSubject dataSubject1 = actorRestClient.getDataSubject(dataRequest.getDataSubjectId());
            dataRequest.setDataSubject(dataSubject1);
            response.add(new DataRequestResponseDTO(dataRequest, datas));

        }
        return response;
    }

    @Override
    public DataRequestResponseDTO RectificationAnswer(int idDataRequest, boolean answer, String claimAnswer){
        //Code redondant, de la fonction getDataRequest (pas même type de retour
        DataRequest dataRequest = dataRequestRepository.getById(idDataRequest);
        ArrayList<Data> datas = new ArrayList<>();

        DataSubject dataSubject = actorRestClient.getDataSubject(dataRequest.getDataSubjectId());
        dataRequest.setDataSubject(dataSubject);
        dataRequest.setResponse(true);

        RequestAnswer rectificationAnswer = new RequestAnswer();
        if(answer == true){

            rectificationAnswer.setAnswer(AnswerType.VALIDATED);
            rectificationAnswer.setClaim(claimAnswer);
            rectificationAnswer.setDataRequest(dataRequest);
            rectificationAnswer.setClaimDate(new Date());
            requestAnswerRepository.save(rectificationAnswer);

            // Get all Data object
            List<Integer> dataIds = new ArrayList<>();
            dataIds = dataRequestDataRepository.findDataIdsByDataRequestId(dataRequest.getId());
            dataIds.forEach(dataId -> {
                Data data = dataRestClient.getData(dataId);
                datas.add(data);

                String dataTypeName= data.getData_type_name();
                String primaryKeyName= data.getPrimary_key_name();
                String primaryKeyValue= dataRequest.getPrimaryKeyValue();
                String attribute = data.getAttribute();
                String newValue= dataRequest.getNewValue();

                String dsId = dataRequest.getDataSubject().getIdRef();

                List<Map<String, String>> parameters = new ArrayList<>();

                Map<String, String> parameter = new HashMap<>();
                parameter.put("attribute", attribute);
                parameter.put("newValue", newValue);
                parameter.put("dsId", dsId);
                parameter.put("dataTypeName", dataTypeName);
                parameter.put("primaryKeyName", primaryKeyName);
                parameter.put("primaryKeyValue", primaryKeyValue);

                parameters.add(parameter);

                providerRestClient.rectification(parameters);
            });

        }else{
            rectificationAnswer.setAnswer(AnswerType.REFUSED);
            rectificationAnswer.setClaim(claimAnswer);
            rectificationAnswer.setDataRequest(dataRequest);
            rectificationAnswer.setClaimDate(new Date());
            requestAnswerRepository.save(rectificationAnswer);
        }
        dataRequest.setIsolated(false);

        // Response DTO
        DataRequestResponseDTO response = new DataRequestResponseDTO(dataRequest, datas);
        return response;
    }

    @Override
    public DataRequestResponseDTO ErasureAnswer(int idDataRequest, boolean answer, String claimAnswer){
        DataRequest dataRequest = dataRequestRepository.getById(idDataRequest);
        ArrayList<Data> datas = new ArrayList<>();

        DataSubject dataSubject = actorRestClient.getDataSubject(dataRequest.getDataSubjectId());
        dataRequest.setDataSubject(dataSubject);
        dataRequest.setResponse(true);

        RequestAnswer erasureAnswer = new RequestAnswer();
        if(answer == true){

            erasureAnswer.setAnswer(AnswerType.VALIDATED);
            erasureAnswer.setClaim(claimAnswer);
            erasureAnswer.setDataRequest(dataRequest);
            erasureAnswer.setClaimDate(new Date());
            requestAnswerRepository.save(erasureAnswer);

            // Get all Data object
            List<Integer> dataIds = new ArrayList<>();
            dataIds = dataRequestDataRepository.findDataIdsByDataRequestId(dataRequest.getId());
            dataIds.forEach(dataId -> {
                Data data = dataRestClient.getData(dataId);
                datas.add(data);

                String dataTypeName= data.getData_type_name();
                String primaryKeyName= data.getPrimary_key_name();
                String primaryKeyValue= dataRequest.getPrimaryKeyValue();
                String attribute = data.getAttribute();


                String dsId = dataRequest.getDataSubject().getIdRef()/*getId()*/;

                List<Map<String, String>> parameters = new ArrayList<>();

                Map<String, String> parameter = new HashMap<>();
                parameter.put("attribute", attribute);
                parameter.put("dsId", dsId);
                parameter.put("dataTypeName", dataTypeName);
                parameter.put("primaryKeyName", primaryKeyName);
                parameter.put("primaryKeyValue", primaryKeyValue);

                parameters.add(parameter);

                providerRestClient.forgotten(parameters);
            });

        }else{
            erasureAnswer.setAnswer(AnswerType.REFUSED);
            erasureAnswer.setClaim(claimAnswer);
            erasureAnswer.setDataRequest(dataRequest);
            erasureAnswer.setClaimDate(new Date());
            requestAnswerRepository.save(erasureAnswer);
        }
        dataRequest.setIsolated(false);

        // Response DTO
        DataRequestResponseDTO response = new DataRequestResponseDTO(dataRequest, datas);
        return response;
    }

    @Override
    public void saveAccessRequest(String idRef, String claim, ArrayList<Integer> listOfSelectedDataId) {
        DataRequest dataRequest = new DataRequest();
        DataSubject dataSubject = actorRestClient.getDataSubjectByRef(idRef);

        dataRequest.setDataSubject(dataSubject);
        dataRequest.setClaimDate(new Date());
        dataRequest.setClaim(claim);
        dataRequest.setType(TypeDataRequest.Knowledge);

        dataRequest.setResponse(false);
        dataRequest.setIsolated(true);
        dataRequest.setNewValue(null);
        dataRequest.setPrimaryKeyValue(null);

        DataRequest result = dataRequestRepository.save(dataRequest);

        // Save all DataRequestData
        listOfSelectedDataId.forEach(dataId -> {
            DataRequestData drd = new DataRequestData();
            drd.setDataRequestId(result.getId());
            drd.setDataId(dataId);
            dataRequestDataRepository.save(drd);
        });
    }

    @Override
    public boolean isAccepted(int dataSubjectId, int dataId) {
        Optional<Boolean> isAccepted = dataRequestDataRepository.isDataAcceptedByDataSubjectIdAndDataId(dataSubjectId, dataId);
        if(isAccepted.isPresent())
            return isAccepted.get();
        else
            return false;
    }

    @Override
    public List<RequestListDTO> getDataRequestByFilters(List<String> listOfSelectedTypeDataRequests, List<String> listOfSelectedStatus, List<String> listOfSelectedDataSubjectCategories) {
        List<RequestListDTO> response = new ArrayList<>();
        // Filter with Type of DataRequest
        List<DataRequest> filteredTypeList;
        if(listOfSelectedTypeDataRequests.isEmpty()) {
            filteredTypeList = dataRequestRepository.findAll();
        }
        else {
            filteredTypeList = new ArrayList<>();
            filteredTypeList = dataRequestRepository.findByTypes(listOfSelectedTypeDataRequests);
        }

        // Filter with status of DataRequest
        List<DataRequest> filteredStatusList;
        System.out.println(listOfSelectedStatus);
        if(listOfSelectedStatus.isEmpty()) {
            filteredStatusList = filteredTypeList;
        }
        else {
            filteredStatusList = new ArrayList<>();
            filteredTypeList.forEach(dataRequest -> {
                Optional<RequestAnswer> answer = requestAnswerRepository.findRequestAnswerByRequestId((long) dataRequest.getId());
                System.out.println(answer.isPresent());
                System.out.println(listOfSelectedStatus.contains(AnswerType.IN_PROGRESS.toString()));
                // First case : If looking for validated or refused requests
                if(answer.isPresent()) {
                    AnswerType answerType = answer.get().getAnswer();
                    if(listOfSelectedStatus.contains(answerType.toString())) {
                        filteredStatusList.add(dataRequest);
                    }
                }
                // Second case : looking for in progess requests (so no answer yet)
                else if(listOfSelectedStatus.contains(AnswerType.IN_PROGRESS.toString())) {
                    filteredStatusList.add(dataRequest);
                }
            });
        }

        // Filter with DataSubjectCategory
        filteredStatusList.forEach(dataRequest -> {
            DSCategoryResponseDTO category = actorRestClient.getDSCategoryById(dataRequest.getDataSubjectId());
            RequestListDTO r = new RequestListDTO();
            r.setRequestId(dataRequest.getId());
            r.setResponse(dataRequest.isResponse());
            r.setTypeRequest(dataRequest.getType());
            r.setIssuedAt(dataRequest.getClaimDate());
            r.setDataSubjectCategory(category.getDscName());
            
            if(listOfSelectedDataSubjectCategories.isEmpty()) {
                response.add(r);
            }
            else if(listOfSelectedDataSubjectCategories.contains(category.getDscName())) {
                response.add(r);
            }
        });

        return response;
    }

    @Override
    public RequestDetailDTO getRequestDataDetail(int requestId) {
        RequestDetailDTO response = new RequestDetailDTO();

        // DataRequest information
        DataRequest dataRequest = dataRequestRepository.getById(requestId);
        response.setRequestId(dataRequest.getId());
        response.setResponse(dataRequest.isResponse());
        response.setIsolated(dataRequest.isIsolated());
        response.setIssuedAt(dataRequest.getClaimDate());
        response.setNewValue(dataRequest.getNewValue());
        response.setTypeRequest(dataRequest.getType());

        // DataSubject information
        DataSubject dataSubject = actorRestClient.getDataSubject(dataRequest.getDataSubjectId());
        DSCategoryResponseDTO category = actorRestClient.getDSCategoryById(dataRequest.getDataSubjectId());
        response.setDataSubject(dataSubject.getId(), dataSubject.getIdRef(), category.getDscName());

        // Data information
        List<DataRequestData> dataRequestDatas = dataRequestDataRepository.findDataRequestDataByDataRequestId(requestId);
        dataRequestDatas.forEach(drd -> {
            Data data = dataRestClient.getData(drd.getDataId());
            String dataTypeName = dataRestClient.getDataTypeNameByDataTypeId(data.getData_type_id());
            response.addData(dataTypeName, data.getId(), data.getAttribute(), drd.isAnswer());
        });

        return response;
    }

    @Override
    public RequestAnswer getRequestAnswerByDataRequestId(int requestId) {
        Optional<RequestAnswer> res = requestAnswerRepository.findRequestAnswerByRequestId((long) requestId);
        if(res.isPresent())
            return res.get();
        else
            return null;
    }
}

