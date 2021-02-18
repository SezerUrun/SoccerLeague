package com.example.soccerleague.API;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Season {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("startDate")
    @Expose
    private String startDate;
    @SerializedName("endDate")
    @Expose
    private String endDate;
    @SerializedName("currentMatchday")
    @Expose
    private Object currentMatchday;
    @SerializedName("availableStages")
    @Expose
    private List<String> availableStages = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Object getCurrentMatchday() {
        return currentMatchday;
    }

    public void setCurrentMatchday(Object currentMatchday) {
        this.currentMatchday = currentMatchday;
    }

    public List<String> getAvailableStages() {
        return availableStages;
    }

    public void setAvailableStages(List<String> availableStages) {
        this.availableStages = availableStages;
    }

}
