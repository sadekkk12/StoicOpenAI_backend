package com.example.stoicopenai_backend.dtos;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class DalleResponse {
    private long created;
    private List<ImageData> data;

  @Getter
  @Setter
    public static class ImageData {
        private String url;



        @JsonProperty("url") // Ensure the JSON property matches the field name
        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
