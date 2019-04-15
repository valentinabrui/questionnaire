package com.questionnaire.resource;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.questionnaire.dao.MySqlQuestionnaireDao;
import com.questionnaire.model.Answer;
import com.questionnaire.model.QASession;
import com.questionnaire.model.Question;
import org.jvnet.hk2.annotations.Optional;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Valentina on 2019-04-13.
 */
@Path("/")
public class QuestionnaireResource {

  private MySqlQuestionnaireDao questionnaireDao;

  @Inject
  public QuestionnaireResource(MySqlQuestionnaireDao questionnaireDao) {
    this.questionnaireDao = questionnaireDao;
  }

  @Path("/")
  @GET
  public Response getIndexPage(){
    InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("backend-qa-worktest.html");
    return Response.ok().entity(inputStream).build();
  }

  @POST
  @Path("/qa")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  public Response createQASession(@Valid QASession session){
    if(session.getStartTime().isAfter(session.getEndTime())){
      return getErrorResponse(Response.Status.NOT_ACCEPTABLE, "{\"message\": \"start_time should be before end_time\"}");
    }
    if(session.getStartTime().isBefore(LocalDateTime.now()) || session.getEndTime().isBefore(LocalDateTime.now())){
      return getErrorResponse(Response.Status.NOT_ACCEPTABLE, "{\"message\": \"qa session should start in future date\"}");
    }
    return Response.ok().entity(questionnaireDao.saveQASession(session)).build();
  }

  @GET
  @Path("/qa/{qaId}")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  @JsonDeserialize(using = LocalDateDeserializer.class)
  public Response getQASessionById(@NotNull @PathParam("qaId") Long qaId){
    QASession qaSession = questionnaireDao.getQASession(qaId);
    if(qaSession == null) {
      return Response.status(Response.Status.NO_CONTENT).build();

    }
    return Response.ok().entity(questionnaireDao.getQASession(qaId)).build();
  }

  @POST
  @Path("/question/{qaId}")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  public Response createQuestion(@NotNull @PathParam("qaId") Long qaId, @Valid Question question){
    QASession qaSession = questionnaireDao.getQASession(qaId);
    if(qaSession.getEndTime().isBefore(LocalDateTime.now())){
      return getErrorResponse(Response.Status.NOT_ACCEPTABLE, "{\"message\": \"QA session is expired\"}");
    }
    return Response.ok().entity(questionnaireDao.saveQuestion(question)).build();
  }

  @POST
  @Path("/answer/{questionAnswerId}")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  public Response createAnswer(@NotNull @PathParam("questionAnswerId") Long questionAnswerId, @Valid Answer answer){
    Long id = questionnaireDao.saveAnswer(answer);
    if(id == null){
      return getErrorResponse(Response.Status.NOT_ACCEPTABLE, "{\"message\": \"Question doesn't exist\"}");
    }
    return Response.ok().entity(id).build();
  }

  @GET
  @Path("/qa/{qaId}/questions")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  public List<Question> getQuestions(@NotNull @PathParam("qaId") Long qaId){
    return questionnaireDao.getQuestions(qaId, null);
  }

  @GET
  @Path("/qa/{qaId}/questions/{answeredFilter}")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  public List<Question> getQuestionsFiltered(@NotNull @PathParam("qaId") Long qaId, @PathParam("answeredFilter") Boolean answeredFilter){
    return questionnaireDao.getQuestions(qaId, answeredFilter);
  }

  private Response getErrorResponse(Response.Status status, String message){
    return Response.status(status).entity(message).build();
  }
}
