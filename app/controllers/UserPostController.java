package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import services.QuestionService;
import services.UserService;
import views.html.home;
import views.html.usersHome;

import javax.inject.Inject;

public class UserPostController extends Controller {

    private static final int N_OF_LATEST_POSTS = 5;
    private final QuestionService questionService;
    private final UserService userService;

    @Inject
    public UserPostController(QuestionService questionService, UserService userService) {
        this.questionService = questionService;
        this.userService = userService;
    }

    public Result home(int page) {
        return ok(home.render(questionService.findNLatestQuestions(N_OF_LATEST_POSTS, page)));
    }

    public Result usersHome(String username, int page) {
        return userService.findUserByUsername(username)
                .map(userDTO ->
                        questionService.findNLatestQuestionsForUsername(N_OF_LATEST_POSTS, page, username)
                                .map(questionDTOs -> ok(usersHome.render(userDTO, questionDTOs)))
                                .orElseGet(Results::notFound))
                .orElseGet(Results::notFound);
    }

}
