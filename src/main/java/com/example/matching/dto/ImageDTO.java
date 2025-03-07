package com.example.matching.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageDTO {
    private String fileName;
    private String url;
}


// package com.example.matching.dto;

// public class ImageDTO {
//     private String fileName;
//     private String url;

//     public ImageDTO(String fileName, String url) {
//         this.fileName = fileName;
//         this.url = url;
//     }

//     public String getFileName() {
//         return fileName;
//     }

//     public void setFileName(String fileName) {
//         this.fileName = fileName;
//     }

//     public String getUrl() {
//         return url;
//     }

//     public void setUrl(String url) {
//         this.url = url;
//     }
// }
