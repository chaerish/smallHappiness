package smu.likelion.smallHappiness.user.service;

import lombok.RequiredArgsConstructor;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smu.likelion.smallHappiness.common.error.BusinessException;
import smu.likelion.smallHappiness.common.error.ErrorCode;
import smu.likelion.smallHappiness.user.dto.ClientSignUpRequestDTO;
import smu.likelion.smallHappiness.user.entity.Client;
import smu.likelion.smallHappiness.user.repository.ClientRepository;
import smu.likelion.smallHappiness.user.repository.CustomerRepository;

import java.io.IOException;
import java.util.Collections;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService{

    private final PasswordEncoder passwordEncoder;
    private final ClientRepository clientRepository;
    private final CustomerRepository customerRepository;
    @Value("${openapi.mykey}")
    private String mykey;
    private final String uri = "http://api.odcloud.kr/api/nts-businessman/v1/status?serviceKey=";

    @Override
    @Transactional
    public Long signUp(ClientSignUpRequestDTO clientSignUpRequestDTO) throws IOException {
        String userId = clientSignUpRequestDTO.getUserId();
        if(clientRepository.existsByUserId(userId) || customerRepository.existsByUserId(userId)){
            throw new BusinessException(ErrorCode.USER_DUPLICATED);
        }
        if(clientRepository.existsByRegNum(clientSignUpRequestDTO.getRegNum())){
            throw new BusinessException(ErrorCode.REGNUM_DUPLICATED);
        }

        //사업자 여부 진위확인
        if(!getStatus(clientSignUpRequestDTO.getRegNum())){
            throw new BusinessException(ErrorCode.INVALID_REGNUM);
        }

        clientSignUpRequestDTO.setUserPw(passwordEncoder.encode(clientSignUpRequestDTO.getUserPw()));

        Client client = Client.builder()
                .userId(clientSignUpRequestDTO.getUserId())
                .userPw(clientSignUpRequestDTO.getUserPw())
                .name(clientSignUpRequestDTO.getName())
                .storeName(clientSignUpRequestDTO.getStoreName())
                .storeAddr(clientSignUpRequestDTO.getStoreAddr())
                .regNum(clientSignUpRequestDTO.getRegNum())
                .roles(Collections.singletonList("USER"))
                .build();
        clientRepository.save(client);
        return client.getId();
    }

    @Override
    public boolean getStatus(String regNum) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(uri+mykey);
        try{
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            //create post json data
            jsonArray.put(regNum);
            jsonObject.put("b_no", jsonArray);
            httpPost.setEntity(new StringEntity(jsonObject.toString()));
            //set header
            httpPost.setHeader(HttpHeaders.ACCEPT, "application/json");
            httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            //get response
            CloseableHttpResponse response = httpClient.execute(httpPost);
            if(response.getStatusLine().getStatusCode() != 200){
                response.close();
                return false;
            }
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");
            if(responseString.contains("match_cnt")){
                response.close();
                return true;
            }
            else{
                response.close();
                return false;
            }
        } catch (JSONException e){
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
