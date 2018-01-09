package controllers;

import annotations.Authenticated;
import dto.LoginDTO;
import dto.QuestionDTO;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import services.AnswerService;
import services.QuestionService;
import views.html.editQuestion;
import views.html.newQuestion;
import views.html.question;

import javax.inject.Inject;
import java.util.Optional;

public class QuestionController extends Controller {

    private final QuestionService questionService;
    private final AnswerService answerService;
    private final Form<QuestionDTO> questionForm;
    private final Form<LoginDTO> loginDTOForm;

    @Inject
    public QuestionController(QuestionService questionService, AnswerService answerService, FormFactory formFactory) {
        this.questionService = questionService;
        this.answerService = answerService;
        this.questionForm = formFactory.form(QuestionDTO.class);
        this.loginDTOForm = formFactory.form(LoginDTO.class);
    }

    public Result getQuestion(Long postId) {
        return answerService.findAnswersForQuestion(postId)
                .map(answerDTOs ->
                        questionService.getQuestion(postId)
                                .map(questionDTO -> ok(question.render(questionDTO, answerDTOs)))
                                .orElseGet(Results::notFound))
                .orElseGet(Results::notFound);
    }

    @Authenticated
    public Result getNewQuestionForm() {
        return ok(newQuestion.render(questionForm));
    }

    @Authenticated

    public Result getEditQuestionForm(Long postId) {
        Optional<QuestionDTO> optionalQuestion = questionService.getQuestion(postId);
        if (optionalQuestion.isPresent() && !optionalQuestion.get().username.equals(session("username")))
            return badRequest(views.html.login.render(loginDTOForm.withGlobalError("Please login with proper credentials to modify this question")));
        return questionService.getQuestion(postId)
                .map(questionDTO -> ok(editQuestion.render(questionForm.fill(questionDTO), postId)))
                .orElseGet(Results::notFound);
    }

    @Authenticated
    public Result createQuestion() {
        Form<QuestionDTO> questionForm = this.questionForm.bindFromRequest();
        if (questionForm.hasErrors()) {
            return badRequest(newQuestion.render(questionForm));
        } else {
            QuestionDTO questionDTO = questionForm.get();
            questionDTO.username = session("username");
            questionDTO = questionService.saveQuestion(questionDTO);
            return redirect(routes.QuestionController.getQuestion(questionDTO.id));
        }
    }

    @Authenticated

    public Result editQuestion(Long postId) {
        Optional<QuestionDTO> optionalQuestion = questionService.getQuestion(postId);
        if (optionalQuestion.isPresent() && !optionalQuestion.get().username.equals(session("username")))
            return badRequest(views.html.login.render(loginDTOForm.withGlobalError("Please login with proper credentials to modify this question")));
        Form<QuestionDTO> questionForm = this.questionForm.bindFromRequest();
        if (questionForm.hasErrors()) {
            return badRequest(editQuestion.render(questionForm, postId));
        } else {
            QuestionDTO questionDTO = questionForm.get();
            questionDTO.id = postId;
            return questionService.editQuestion(questionDTO)
                    .map(x -> redirect(routes.QuestionController.getQuestion(postId)))
                    .orElseGet(Results::notFound);
        }
    }

    @Authenticated

    public Result deleteQuestion(Long postId) {
        Optional<QuestionDTO> optionalQuestion = questionService.getQuestion(postId);
        if (optionalQuestion.isPresent() && !optionalQuestion.get().username.equals(session("username")))
            return badRequest(views.html.login.render(loginDTOForm.withGlobalError("Please login with proper credentials to modify this question")));
        questionService.delete(postId);
        return redirect(routes.UserPostController.usersHome(session("username"), 1));
    }
/*
    @Authenticated
    public Result List<Question> searchByTitle(String title){
        Optinal<QuestionsPager> optinalQuestion = questionService.searchTitle(title);
        if(optinalQuestion.isPresent() && !optinalQuestion.get().username.equals(session("username")))
            return redirect(routes.UserPostController.home(1));

        return questionService.searchTitle(title)
                .map(questionDTOs questionDTO -> ok(question.render(questionDTO, answerDTOs)))
    }
*/


}
