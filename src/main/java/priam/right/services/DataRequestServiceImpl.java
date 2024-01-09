package priam.right.services;

import org.springframework.stereotype.Service;
import priam.right.dto.DataRequestResponseDTO;
import priam.right.entities.*;
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
    public List<DataRequestResponseDTO> getListDataRequest(int dataSubjectId) {
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

            rectificationAnswer.setAnswer(true);
            rectificationAnswer.setClaimAnswer(claimAnswer);
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
            rectificationAnswer.setAnswer(false);
            rectificationAnswer.setClaimAnswer(claimAnswer);
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

            erasureAnswer.setAnswer(true);
            erasureAnswer.setClaimAnswer(claimAnswer);
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
            erasureAnswer.setAnswer(false);
            erasureAnswer.setClaimAnswer(claimAnswer);
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
        return dataRequestDataRepository.isDataAcceptedByDataSubjectIdAndDataId(dataSubjectId, dataId);
    }
}

