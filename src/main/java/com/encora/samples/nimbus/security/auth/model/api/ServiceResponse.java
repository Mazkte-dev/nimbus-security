package com.encora.samples.nimbus.security.auth.model.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class ServiceResponse<T> {

  private ErrorResponse error;

  //@Schema(description = "Api response data", required = true)
  private T data;

  public static <T> ServiceResponse<T> success(T element){
    return ServiceResponse
            .<T>builder()
            .data(element)
            .build();

  }

  public static <T> ServiceResponse<T> failed(ErrorResponse error){
    return ServiceResponse
            .<T>builder()
            .error(error)
            .build();

  }


}
