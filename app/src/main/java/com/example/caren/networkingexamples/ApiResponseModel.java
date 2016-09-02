package com.example.caren.networkingexamples;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiResponseModel {

    @SerializedName("url") @Expose
    public String url;

    @SerializedName("login") @Expose
    public String login;
}
