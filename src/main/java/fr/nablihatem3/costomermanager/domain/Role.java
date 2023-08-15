package fr.nablihatem3.costomermanager.domain;


import com.fasterxml.jackson.annotation.JsonInclude;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author Hatem NABLI
 * @version 1.0
 * @since 10/08/2023
 */

 @Data
 @SuperBuilder
 @NoArgsConstructor
 @JsonInclude()
public class Role {
    private Long id;
    private String name;
    private String Permission;
   
   
}
