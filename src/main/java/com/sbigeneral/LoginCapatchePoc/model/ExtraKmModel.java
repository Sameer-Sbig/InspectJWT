package com.sbigeneral.LoginCapatchePoc.model;
import com.fasterxml.jackson.annotation.JsonProperty;
public class ExtraKmModel {


    @JsonProperty("PINNumber")
    private String pinNumber;

    @JsonProperty("decision")
    private String decision;

    @JsonProperty("remarks")
    private String remarks;

    @JsonProperty("requestedKM")
    private String requestedKm;
    
}
