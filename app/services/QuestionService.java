package services;

import dto.QuestionDTO;
import models.Question;
import util.QuestionsPager;

import java.util.List;
import java.util.Optional;

public interface QuestionService {

    QuestionsPager findNLatestQuestions(int n, int page);

    Optional<QuestionsPager> findNLatestQuestionsForUsername(int n, int page, String username);

    Optional<QuestionDTO> getQuestion(Long postId);

    Optional<Question> getQuestionEntity(Long postId);

    QuestionDTO saveQuestion(QuestionDTO questionDTO);

    Optional<QuestionDTO> editQuestion(QuestionDTO questionDTO);

    Optional<Question> searchTitle(String title);

    void delete(Long postId);
}
