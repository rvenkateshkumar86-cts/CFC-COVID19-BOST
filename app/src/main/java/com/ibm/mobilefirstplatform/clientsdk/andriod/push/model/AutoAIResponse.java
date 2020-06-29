package com.ibm.mobilefirstplatform.clientsdk.andriod.push.model;

import java.util.List;

public class AutoAIResponse {

    private List<Predictation> predictions;

    public List<Predictation> getPredictions() {
        return predictions;
    }

    public void setPredictions(List<Predictation> predictions) {
        this.predictions = predictions;
    }
}
