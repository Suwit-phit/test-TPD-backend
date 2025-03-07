package com.example.matching.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactTypeDTO {
    private String type;
    private String url;
}


// package com.example.matching.dto;

// public class ContactTypeDTO {
//     private String type;
//     private String url;

//     public ContactTypeDTO() {
//     }

//     public ContactTypeDTO(String type, String url) {
//         this.type = type;
//         this.url = url;
//     }

//     public String getType() {
//         return type;
//     }

//     public void setType(String type) {
//         this.type = type;
//     }

//     public String getUrl() {
//         return url;
//     }

//     public void setUrl(String url) {
//         this.url = url;
//     }
// }

