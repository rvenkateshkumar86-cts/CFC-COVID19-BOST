package com.ibm.mobilefirstplatform.clientsdk.andriod.push.model;

import java.util.List;

public class Predictation {
    private List<String> fields;

    private List<Object> values;

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public List<Object> getValues() {
        return values;
    }

    public void setValues(List<Object> values) {
        this.values = values;
    }
}
