package com.questionnaire.dao;

import com.questionnaire.model.Answer;
import com.questionnaire.model.QASession;
import com.questionnaire.model.Question;

import java.util.List;

/**
 * Created by Valentina on 2019-04-13.
 */
public interface QuestionnaireDao {

  Long saveQASession(QASession qaSession);

  QASession getQASession(Long qaId);

  Long saveQuestion(Question question);

  Long saveAnswer(Answer answer);

  List<Question> getQuestions(Long qaId, Boolean answered);
}
