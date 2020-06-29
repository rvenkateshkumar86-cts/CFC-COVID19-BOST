package com.ibm.mobilefirstplatform.clientsdk.android.push;

import com.ibm.mobilefirstplatform.clientsdk.andriod.push.model.AutoAIResponse;
import com.ibm.mobilefirstplatform.clientsdk.andriod.push.model.PatientDTO;
import com.ibm.mobilefirstplatform.clientsdk.andriod.push.services.AutoAIConnector;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {

    private AutoAIConnector connector = AutoAIConnector.getInstance();

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testAutoAI() throws Exception {
        PatientDTO dto = new PatientDTO();
        dto.setAge(34);
        dto.setJob("IT");
        dto.setMarital("Married");
        AutoAIResponse response = connector.submitData(dto);
        assertTrue(response.getPredictions() != null);
        assertTrue(!response.getPredictions().get(0).getValues().isEmpty());
    }
}