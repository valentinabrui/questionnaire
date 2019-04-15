package com.questionnaire.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Created by Valentina on 2019-04-13.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QASession {

  private Long qaId;

  @JsonProperty("start_time")
  @NotNull
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm'Z'")
  private LocalDateTime startTime;

  @JsonProperty("end_time")
  @NotNull
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm'Z'")
  private LocalDateTime endTime;

  @JsonProperty("host_name")
  @NotNull(message = "host_name is a required field")
  @Size(min = 1, max = 255, message = "host_name cannot be longer than 255 characters")
  private String hostName;

  public QASession() {
  }

  public QASession(Long qaId, LocalDateTime startTime, LocalDateTime endTime, String host) {
    this.qaId = qaId;
    this.startTime = startTime;
    this.endTime = endTime;
    this.hostName = host;
  }

  @JsonProperty
  public Long getQaId() {
    return qaId;
  }

  @JsonProperty
  public LocalDateTime getStartTime() {
    return startTime;
  }

  @JsonProperty
  public LocalDateTime getEndTime() {
    return endTime;
  }

  @JsonProperty
  public String getHostName() {
    return hostName;
  }
}
