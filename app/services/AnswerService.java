package services;


import dto.AnswerDTO;

import java.util.List;
import java.util.Optional;


public interface AnswerService {

    AnswerDTO saveAnswer (AnswerDTO answerDTO);

    Optional<List<AnswerDTO>> findAnswersForQuestion(Long postId);

}
