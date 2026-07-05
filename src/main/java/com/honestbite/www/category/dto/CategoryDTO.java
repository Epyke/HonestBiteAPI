package com.honestbite.www.category.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
public class CategoryDTO {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class GetOutput {
        private Long id;
        private String name;
    }
}
