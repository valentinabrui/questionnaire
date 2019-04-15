package com.questionnaire.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by Valentina on 2019-04-13.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Question {

  private Long questionAnswerId;

  @JsonProperty("text")
  @NotNull(message = "text is a required field")
  private String question;

  @JsonProperty("asked_by_name")
  @Size(min = 1, max = 255, message = "asked_by_name cannot be longer than 255 characters")
  private String askedBy;

  @JsonProperty("qa_id")
  @NotNull(message = "qa_id is a required field")
  private Long qaId;

  private Answer answer;

  public Question() {
  }

  public Question(Long questionAnswerId, String question, String askedBy, Long qaId) {
    this.questionAnswerId = questionAnswerId;
    this.question = question;
    this.askedBy = askedBy;
    this.qaId = qaId;
  }

  public Question(Long questionAnswerId, String question, String askedBy, Long qaId, Answer answer) {
    this(questionAnswerId, question, askedBy, qaId);
    this.answer = answer;
  }

  public Long getQuestionAnswerId() {
    return questionAnswerId;
  }

  public String getQuestion() {
    return question;
  }

  public String getAskedBy() {
    return askedBy;
  }

  public Long getQaId() {
    return qaId;
  }

  public Answer getAnswer() {
    return answer;
  }
}
