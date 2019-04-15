package com.questionnaire.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by Valentina on 2019-04-13.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Answer {

  @JsonProperty("question_id")
  private Long questionAnswerId;

  @JsonProperty("text")
  @NotNull(message = "text is a required field")
  private String answer;

  @JsonProperty("image_url")
  @URL
  private String imageUrl;

  @JsonProperty("answered_by")
  @NotNull(message = "answered_by is a required field")
  @Size(min = 1, max = 255, message = "answered_by cannot be longer than 255 characters")
  private String answeredBy;

  public Answer() {
  }

  public Answer(Long questionAnswerId, String answer, String imageUrl, String answeredBy) {
    this.questionAnswerId = questionAnswerId;
    this.answer = answer;
    this.imageUrl = imageUrl;
    this.answeredBy = answeredBy;
  }

  public Long getQuestionAnswerId() {
    return questionAnswerId;
  }

  public String getAnswer() {
    return answer;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public String getAnsweredBy() {
    return answeredBy;
  }
}
