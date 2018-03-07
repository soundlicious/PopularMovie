package com.example.pablo.popularmovie1.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

/**
 * Created by pablo on 03/03/2018.
 */


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "page",
        "results",
        "total_results",
        "total_pages"
})
public class Page implements Parcelable {

    @JsonProperty("page")
    private Integer page;
    @JsonProperty("results")
    private List<MovieDetail> results = null;
    @JsonProperty("total_results")
    private Integer totalMovieDetails;
    @JsonProperty("total_pages")
    private Integer totalPages;

    @JsonProperty("page")
    public Integer getPage() {
        return page;
    }

    @JsonProperty("page")
    public void setPage(Integer page) {
        this.page = page;
    }

    @JsonProperty("results")
    public List<MovieDetail> getMovieDetails() {
        return results;
    }

    @JsonProperty("results")
    public void setMovieDetails(List<MovieDetail> results) {
        this.results = results;
    }

    @JsonProperty("total_results")
    public Integer getTotalMovieDetails() {
        return totalMovieDetails;
    }

    @JsonProperty("total_results")
    public void setTotalMovieDetails(Integer totalMovieDetails) {
        this.totalMovieDetails = totalMovieDetails;
    }

    @JsonProperty("total_pages")
    public Integer getTotalPages() {
        return totalPages;
    }

    @JsonProperty("total_pages")
    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.page);
        dest.writeTypedList(this.results);
        dest.writeValue(this.totalMovieDetails);
        dest.writeValue(this.totalPages);
    }

    public Page() {
    }

    private Page(Parcel in) {
        this.page = (Integer) in.readValue(Integer.class.getClassLoader());
        this.results = in.createTypedArrayList(MovieDetail.CREATOR);
        this.totalMovieDetails = (Integer) in.readValue(Integer.class.getClassLoader());
        this.totalPages = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<Page> CREATOR = new Parcelable.Creator<Page>() {
        @Override
        public Page createFromParcel(Parcel source) {
            return new Page(source);
        }

        @Override
        public Page[] newArray(int size) {
            return new Page[size];
        }
    };
}