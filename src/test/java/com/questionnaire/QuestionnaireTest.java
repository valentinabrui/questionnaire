package com.questionnaire;

import com.questionnaire.dao.MySqlQuestionnaireDao;
import com.questionnaire.model.Answer;
import com.questionnaire.model.QASession;
import com.questionnaire.model.Question;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

/**
 * Created by Valentina on 2019-04-14.
 */
public class QuestionnaireTest {

  @Mock
  private MySqlQuestionnaireDao dao;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testCreateQaSession() {
    QASession qaSession = createQASession();
    Mockito.when(dao.saveQASession(qaSession)).thenReturn(1L);
    Long qaId = dao.saveQASession(qaSession);
    Assert.assertEquals(qaId.longValue(), qaSession.getQaId().longValue());
  }

  @Test
  public void testGetQaSession() {
    Mockito.when(dao.getQASession(1L)).thenReturn(createQASession());
    QASession qaSession = dao.getQASession(1L);
    Assert.assertEquals("user1", qaSession.getHostName());
    Assert.assertEquals(1, qaSession.getQaId().longValue());
  }

  @Test
  public void testCreateQuestion() {
    Question question = createQuestion();
    Mockito.when(dao.saveQuestion(question)).thenReturn(1L);
    Long id = dao.saveQuestion(question);
    Assert.assertEquals(id.longValue(), question.getQuestionAnswerId().longValue());
    Assert.assertEquals(1, question.getQaId().longValue());
  }

  @Test
  public void testCreateAnswer() {
    Answer answer = createAnswer();
    Mockito.when(dao.saveAnswer(answer)).thenReturn(1L);
    Long id = dao.saveAnswer(answer);
    Assert.assertEquals(id.longValue(), answer.getQuestionAnswerId().longValue());
  }

  @Test
  public void testCreateAnswerWithoutQuestion() {
    Answer answer = createAnswerWithoutQuestion();
    Mockito.when(dao.saveAnswer(answer)).thenReturn(null);
    Long id = dao.saveAnswer(answer);
    Assert.assertEquals(100L, answer.getQuestionAnswerId().longValue());
    Assert.assertNull(id);
  }

  private QASession createQASession(){
    return new QASession(1L, LocalDateTime.now(), LocalDateTime.now(), "user1");
  }

  private Question createQuestion(){
    return new Question(1L, "What is the day today", "Winnie", 1L);
  }

  private Answer createAnswer(){
    return new Answer(1L, "Today, my favorite day", "http://image.com", "Pyatachok");
  }

  private Answer createAnswerWithoutQuestion(){
    return new Answer(100L, "Today, my favorite day", "http://image.com", "Pyatachok");
  }
}
