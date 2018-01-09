package services.impl;

import dto.AnswerDTO;
import models.Answer;
import models.Question;
import models.User;
import services.AnswerService;
import services.QuestionService;
import services.UserService;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class AnswerServiceImpl implements AnswerService {

    private final UserService userService;
    private final QuestionService questionService;

    @Inject
    public AnswerServiceImpl(UserService userService, QuestionService questionService) {
        this.userService = userService;
        this.questionService = questionService;
    }

    @Override
    public AnswerDTO saveAnswer(AnswerDTO answerDTO) {
        Answer answer = convertToEntity(answerDTO);
        answer.save();
        return convertToDTO(answer);
    }

    @Override
    public Optional<List<AnswerDTO>> findAnswersForQuestion(Long postId) {
        return Optional.ofNullable(Question.find.byId(postId))
                .map(question -> question.answers
                        .stream()
                        .map(this::convertToDTO)
                        .collect(toList()));
    }

    private AnswerDTO convertToDTO(Answer answer) {
        return new AnswerDTO(answer.body, answer.user.username, answer.question.id, answer.createDate);
    }

    private Answer convertToEntity(AnswerDTO answerDTO) {
        User user = userService.findUserEntityByUsername(answerDTO.username).orElse(null);
        Question question = questionService.getQuestionEntity(answerDTO.postId).orElse(null);
        return new Answer(answerDTO.body, question, user);
    }
}
