package com.honestbite.www.rating.dto;

import lombok.*;

@Data
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RatingDTO {
    private String id;
    private String userName;
    private Double score;
    private String createdAt;
    private String comment;
}
