package com.synergics.ishom.trysearchapps.Model.RetrofitResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseCategory {

    @SerializedName("status") public String status;
    @SerializedName("categories") public List<Category> categories;

    public class Category {

        @SerializedName("id") public int id;
        @SerializedName("name") public String name;
        @SerializedName("url") public int url;
        @SerializedName("revamped") public boolean revamped;
        @SerializedName("children") public List<Category> children;

    }

}
