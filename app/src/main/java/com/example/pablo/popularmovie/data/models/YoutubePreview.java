package com.example.pablo.popularmovie.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Created by pablo on 13/03/2018.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "version",
        "width",
        "author_url",
        "provider_name",
        "height",
        "thumbnail_height",
        "provider_url",
        "author_name",
        "thumbnail_width",
        "thumbnail_url",
        "type",
        "title",
        "html"
})
public class YoutubePreview implements Parcelable
{

    String id;
    @JsonProperty("version")
    private String version;
    @JsonProperty("width")
    private Integer width;
    @JsonProperty("author_url")
    private String authorUrl;
    @JsonProperty("provider_name")
    private String providerName;
    @JsonProperty("height")
    private Integer height;
    @JsonProperty("thumbnail_height")
    private Integer thumbnailHeight;
    @JsonProperty("provider_url")
    private String providerUrl;
    @JsonProperty("author_name")
    private String authorName;
    @JsonProperty("thumbnail_width")
    private Integer thumbnailWidth;
    @JsonProperty("thumbnail_url")
    private String thumbnailUrl;
    @JsonProperty("type")
    private String type;
    @JsonProperty("title")
    private String title;
    @JsonProperty("html")
    private String html;
    public final static Parcelable.Creator<YoutubePreview> CREATOR = new Creator<YoutubePreview>() {


        @SuppressWarnings({
                "unchecked"
        })
        public YoutubePreview createFromParcel(Parcel in) {
            return new YoutubePreview(in);
        }

        public YoutubePreview[] newArray(int size) {
            return (new YoutubePreview[size]);
        }

    }
            ;
    private boolean isEmpty =  false;

    protected YoutubePreview(Parcel in) {
        this.id = ((String) in.readValue(String.class.getClassLoader()));
        this.version = ((String) in.readValue((String.class.getClassLoader())));
        this.width = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.authorUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.providerName = ((String) in.readValue((String.class.getClassLoader())));
        this.height = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.thumbnailHeight = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.providerUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.authorName = ((String) in.readValue((String.class.getClassLoader())));
        this.thumbnailWidth = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.thumbnailUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.html = ((String) in.readValue((String.class.getClassLoader())));
    }

    public YoutubePreview() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("version")
    public String getVersion() {
        return version;
    }

    @JsonProperty("version")
    public void setVersion(String version) {
        this.version = version;
    }

    @JsonProperty("width")
    public Integer getWidth() {
        return width;
    }

    @JsonProperty("width")
    public void setWidth(Integer width) {
        this.width = width;
    }

    @JsonProperty("author_url")
    public String getAuthorUrl() {
        return authorUrl;
    }

    @JsonProperty("author_url")
    public void setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
    }

    @JsonProperty("provider_name")
    public String getProviderName() {
        return providerName;
    }

    @JsonProperty("provider_name")
    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    @JsonProperty("height")
    public Integer getHeight() {
        return height;
    }

    @JsonProperty("height")
    public void setHeight(Integer height) {
        this.height = height;
    }

    @JsonProperty("thumbnail_height")
    public Integer getThumbnailHeight() {
        return thumbnailHeight;
    }

    @JsonProperty("thumbnail_height")
    public void setThumbnailHeight(Integer thumbnailHeight) {
        this.thumbnailHeight = thumbnailHeight;
    }

    @JsonProperty("provider_url")
    public String getProviderUrl() {
        return providerUrl;
    }

    @JsonProperty("provider_url")
    public void setProviderUrl(String providerUrl) {
        this.providerUrl = providerUrl;
    }

    @JsonProperty("author_name")
    public String getAuthorName() {
        return authorName;
    }

    @JsonProperty("author_name")
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    @JsonProperty("thumbnail_width")
    public Integer getThumbnailWidth() {
        return thumbnailWidth;
    }

    @JsonProperty("thumbnail_width")
    public void setThumbnailWidth(Integer thumbnailWidth) {
        this.thumbnailWidth = thumbnailWidth;
    }

    @JsonProperty("thumbnail_url")
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    @JsonProperty("thumbnail_url")
    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("html")
    public String getHtml() {
        return html;
    }

    @JsonProperty("html")
    public void setHtml(String html) {
        this.html = html;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(version);
        dest.writeValue(width);
        dest.writeValue(authorUrl);
        dest.writeValue(providerName);
        dest.writeValue(height);
        dest.writeValue(thumbnailHeight);
        dest.writeValue(providerUrl);
        dest.writeValue(authorName);
        dest.writeValue(thumbnailWidth);
        dest.writeValue(thumbnailUrl);
        dest.writeValue(type);
        dest.writeValue(title);
        dest.writeValue(html);
    }

    public int describeContents() {
        return 0;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        this.isEmpty = empty;
    }
}