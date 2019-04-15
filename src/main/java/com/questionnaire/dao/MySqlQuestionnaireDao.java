package com.questionnaire.dao;

import com.questionnaire.model.Answer;
import com.questionnaire.model.QASession;
import com.questionnaire.model.Question;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Valentina on 2019-04-13.
 */
@Singleton
@Slf4j
public class MySqlQuestionnaireDao implements QuestionnaireDao {

  private final DataSource dataSource;
  private final Logger log = LoggerFactory.getLogger(MySqlQuestionnaireDao.class);

  @Inject
  public MySqlQuestionnaireDao(DataSource dataSource) {
    this.dataSource = dataSource;
    init();
  }

  private void init() {
    createIfNotExists("CREATE SCHEMA IF NOT EXISTS questionnaire");
    createIfNotExists("CREATE TABLE IF NOT EXISTS questionnaire.qa_session " +
        "(qa_id BIGINT(20) NOT NULL AUTO_INCREMENT, host VARCHAR(255), start_time DATETIME, end_time DATETIME, " +
        "PRIMARY KEY(qa_id))");
    createIfNotExists("CREATE TABLE IF NOT EXISTS questionnaire.question_answer " +
        "(question_answer_id BIGINT(20) NOT NULL AUTO_INCREMENT, question TEXT, asked_by VARCHAR(255), qa_id BIGINT(20), " +
        "answer TEXT, image_url TEXT, answered_by VARCHAR(255)," +
        "PRIMARY KEY(question_answer_id), FOREIGN KEY fk_qa_id(qa_id) REFERENCES qa_session(qa_id) " +
        "ON UPDATE CASCADE ON DELETE RESTRICT)");
  }

  private void createIfNotExists(String sql) {
    try (Connection connection = dataSource.getConnection();
         PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.execute();
    } catch (SQLException e) {
      log.error("Error accessing database: ", e);
      throw new RuntimeException(e);
    }
  }

  private Long getId(PreparedStatement ps) throws SQLException {
    ResultSet rs = ps.getGeneratedKeys();
    rs.next();
    return rs.getLong(1);
  }

  @Override
  public Long saveQASession(QASession qaSession) {
    try (Connection connection = dataSource.getConnection();
         PreparedStatement ps = connection.prepareStatement("INSERT INTO questionnaire.qa_session(host, start_time, end_time) " +
             "VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
      ps.setString(1, qaSession.getHostName());
      ps.setTimestamp(2, Timestamp.valueOf(qaSession.getStartTime()));
      ps.setTimestamp(3, Timestamp.valueOf(qaSession.getEndTime()));
      ps.execute();
      return getId(ps);
    } catch (SQLException e) {
      log.error("Error saving qa_session: ", e);
      throw new RuntimeException(e);
    }
  }

  @Override
  public QASession getQASession(Long qaId) {
    try (Connection connection = dataSource.getConnection();
         PreparedStatement ps = connection.prepareStatement("SELECT qa_id, host, start_time, end_time " +
             "FROM questionnaire.qa_session WHERE qa_id = '" + qaId + "'");
         ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        Long qaSessionId = rs.getLong(1);
        String host = rs.getString(2);
        Timestamp startTime = rs.getTimestamp(3);
        Timestamp endTime = rs.getTimestamp(4);
        return new QASession(qaSessionId, startTime.toLocalDateTime(), endTime.toLocalDateTime(), host);
      }
    } catch (SQLException e) {
      log.error("Error getting qa_session: ", e);
      throw new RuntimeException(e);
    }
    return null;
  }

  @Override
  public Long saveQuestion(Question question) {
    try (Connection connection = dataSource.getConnection();
         PreparedStatement ps = connection.prepareStatement("INSERT INTO questionnaire.question_answer(question, asked_by, qa_id) " +
             "VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
      ps.setString(1, question.getQuestion());
      ps.setString(2, question.getAskedBy());
      ps.setLong(3, question.getQaId());
      ps.execute();
      return getId(ps);
    } catch (SQLException e) {
      log.error("Error saving a question: ", e);
      throw new RuntimeException(e);
    }
  }

  @Override
  public Long saveAnswer(Answer answer) {
    try (Connection connection = dataSource.getConnection();
         PreparedStatement ps = connection.prepareStatement("UPDATE questionnaire.question_answer SET answer=?, image_url=?, " +
             "answered_by=? WHERE question_answer_id=?")) {
      ps.setString(1, answer.getAnswer());
      ps.setString(2, answer.getImageUrl());
      ps.setString(3, answer.getAnsweredBy());
      ps.setLong(4, answer.getQuestionAnswerId());
      int rowCount = ps.executeUpdate();
      return rowCount == 0 ? null : answer.getQuestionAnswerId();
    } catch (SQLException e) {
      log.error("Error saving an answer: ", e);
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<Question> getQuestions(Long qaId, Boolean answeredFilter) {
    List<Question> questions = new ArrayList<>();
    String preparedStatementQuery = "SELECT question_answer_id, question, asked_by, answer, image_url, answered_by " +
        "FROM questionnaire.question_answer WHERE qa_id = " + qaId;
    if (answeredFilter != null) {
      preparedStatementQuery = answeredFilter ? preparedStatementQuery + " AND answered_by IS NOT NULL" :
          preparedStatementQuery + " AND answered_by IS NULL";
    }
    try (Connection connection = dataSource.getConnection();
         PreparedStatement ps = connection.prepareStatement(preparedStatementQuery);
         ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        Long questionAnswerId = rs.getLong(1);
        String questionString = rs.getString(2);
        String askedBy = rs.getString(3);
        String answerString = rs.getString(4);
        String imageUrl = rs.getString(5);
        String answeredBy = rs.getString(6);
        Answer answer = null;
        if(answeredBy != null) {
          answer = new Answer(questionAnswerId, answerString, imageUrl, answeredBy);
        }
        Question question = new Question(questionAnswerId, questionString, askedBy, qaId, answer);
        questions.add(question);
      }
    } catch (SQLException e) {
      log.error("Error getting questions: ", e);
      throw new RuntimeException(e);
    }
    return questions;
  }
}
