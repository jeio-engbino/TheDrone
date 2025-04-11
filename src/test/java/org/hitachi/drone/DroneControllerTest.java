package org.hitachi.drone;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

import org.hitachi.drone.controller.DroneController;
import org.hitachi.drone.service.DroneService;
import org.hitachi.drone.vo.request.MedicationRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {DroneController.class})
@ExtendWith(SpringExtension.class)
class DroneControllerTest {
    @Autowired
    private DroneController droneController;

    @MockBean
    private DroneService droneService;

    /**
     * Method under test: {@link DroneController#loadDroneBySerialNumber(MedicationRequest)}
     */
    @Test
    void testLoadDroneBySerialNumber() throws Exception {
        when(droneService.loadDroneBySerialNumber((MedicationRequest) any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));

        MedicationRequest medicationRequest = new MedicationRequest();
        medicationRequest.setCode("Code");
        medicationRequest.setImage("Image");
        medicationRequest.setName("Name");
        medicationRequest.setSerialNumber("42");
        medicationRequest.setWeight(10.0f);
        String content = (new ObjectMapper()).writeValueAsString(medicationRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/drone/load")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(droneController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(100));
    }

    /**
     * Method under test: {@link DroneController#getAllDronesFreeForLoading()}
     */
    @Test
    void testGetAllDronesFreeForLoading() throws Exception {
        when(droneService.getAvailDroneList()).thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/drone/avail");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(droneController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(100));
    }

    /**
     * Method under test: {@link DroneController#getMedicationInfo(Map)}
     */
    @Test
    void testGetMedicationInfo() throws Exception {
        when(droneService.getMedicationInfo((Map<String, String>) any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.post("/drone/medication")
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(new HashMap<>()));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(droneController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(100));
    }

    /**
     * Method under test: {@link DroneController#getBatteryInformation(Map)}
     */
    @Test
    void testGetBatteryInformation() throws Exception {
        when(droneService.getDroneBatteryBySerialNumber((Map<String, String>) any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.post("/drone/battery")
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(new HashMap<>()));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(droneController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(100));
    }
}

