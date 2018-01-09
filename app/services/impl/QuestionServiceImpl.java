package services.impl;

import dto.QuestionDTO;
import models.Question;
import models.User;
import services.QuestionService;
import services.UserService;
import util.QuestionsPager;

import javax.inject.Inject;
import java.util.Optional;

public class QuestionServiceImpl implements QuestionService {

    private final UserService userService;

    @Inject
    public QuestionServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public QuestionsPager findNLatestQuestions(int n, int page) {
        return new QuestionsPager(Question.find.query().orderBy("create_date desc").setFirstRow(n * (page - 1)).setMaxRows(n).findPagedList());
    }

    @Override
    public Optional<QuestionsPager> findNLatestQuestionsForUsername(int n, int page, String username) {
        return User.find.query().where().eq("username", username).findOneOrEmpty()
                .map(user -> new QuestionsPager(
                        Question.find.query()
                                .where().eq("user_id", user.id)
                                .orderBy("create_date desc")
                                .setFirstRow(n * (page - 1))
                                .setMaxRows(n)
                                .findPagedList()));
    }

    @Override
    public Optional<QuestionDTO> getQuestion(Long postId) {
        return getQuestionEntity(postId)
                .map(this::convertToDTO);
    }

    @Override
    public Optional<Question> getQuestionEntity(Long postId) {
        return Optional.ofNullable(Question.find.byId(postId));
    }

    @Override
    public QuestionDTO saveQuestion(QuestionDTO questionDTO) {
        Question question = convertToEntity(questionDTO);
        question.save();
        return convertToDTO(question);
    }

    @Override
    public Optional<QuestionDTO> editQuestion(QuestionDTO questionDTO) {
        return getQuestionEntity(questionDTO.id)
                .map(question -> {
                    question.body = questionDTO.body;
                    question.title = questionDTO.title;
                    question.save();
                    return convertToDTO(question);
                });
    }

    @Override
    public void delete(Long postId) {
        Question.find.deleteById(postId);
    }

    private QuestionDTO convertToDTO(Question question) {
        return new QuestionDTO(question.id, question.title, question.body, question.createDate, question.user.username);
    }

    private Question convertToEntity(QuestionDTO questionDTO) {
        User user = userService.findUserEntityByUsername(questionDTO.username).orElse(null);
        return new Question(questionDTO.title, questionDTO.body, user);
    }

    @Override
    public Optional<Question> searchTitle(String title){
        return Question.findTitle.query().where().eq("title", title).findOneOrEmpty();
    }

}
