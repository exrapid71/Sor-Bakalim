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
import services.PostService;
import views.html.editPost;
import views.html.newPost;
import views.html.post;

import javax.inject.Inject;
import java.util.Optional;

public class QuestionController extends Controller {

    private final PostService postService;
    private final AnswerService answerService;
    private final Form<QuestionDTO> postForm;
    private final Form<LoginDTO> loginDTOForm;

    @Inject
    public QuestionController(PostService postService, AnswerService answerService, FormFactory formFactory) {
        this.postService = postService;
        this.answerService = answerService;
        this.postForm = formFactory.form(QuestionDTO.class);
        this.loginDTOForm = formFactory.form(LoginDTO.class);
    }

    public Result getPost(Long postId) {
        return answerService.findAnswersForPost(postId)
                .map(answerDTOs ->
                        postService.getPost(postId)
                                .map(questionDTO -> ok(post.render(questionDTO, answerDTOs)))
                                .orElseGet(Results::notFound))
                .orElseGet(Results::notFound);
    }

    @Authenticated
    public Result getNewPostForm() {
        return ok(newPost.render(postForm));
    }

    @Authenticated
//    @PostExistsAndUserIsOwner
    public Result getEditPostForm(Long postId) {
        Optional<QuestionDTO> optionalPost = postService.getPost(postId);
        if (optionalPost.isPresent() && !optionalPost.get().username.equals(session("username")))
            return badRequest(views.html.login.render(loginDTOForm.withGlobalError("Please login with proper credentials to modify this post")));
        return postService.getPost(postId)
                .map(questionDTO -> ok(editPost.render(postForm.fill(questionDTO), postId)))
                .orElseGet(Results::notFound);
    }

    @Authenticated
    public Result createPost() {
        Form<QuestionDTO> postForm = this.postForm.bindFromRequest();
        if (postForm.hasErrors()) {
            return badRequest(newPost.render(postForm));
        } else {
            QuestionDTO questionDTO = postForm.get();
            questionDTO.username = session("username");
            questionDTO = postService.savePost(questionDTO);
            return redirect(routes.QuestionController.getPost(questionDTO.id));
        }
    }

    @Authenticated
//    @PostExistsAndUserIsOwner
    public Result editPost(Long postId) {
        Optional<QuestionDTO> optionalPost = postService.getPost(postId);
        if (optionalPost.isPresent() && !optionalPost.get().username.equals(session("username")))
            return badRequest(views.html.login.render(loginDTOForm.withGlobalError("Please login with proper credentials to modify this post")));
        Form<QuestionDTO> postForm = this.postForm.bindFromRequest();
        if (postForm.hasErrors()) {
            return badRequest(editPost.render(postForm, postId));
        } else {
            QuestionDTO questionDTO = postForm.get();
            questionDTO.id = postId;
            return postService.editPost(questionDTO)
                    .map(x -> redirect(routes.QuestionController.getPost(postId)))
                    .orElseGet(Results::notFound);
        }
    }

    @Authenticated
//    @PostExistsAndUserIsOwner
    public Result deletePost(Long postId) {
        Optional<QuestionDTO> optionalPost = postService.getPost(postId);
        if (optionalPost.isPresent() && !optionalPost.get().username.equals(session("username")))
            return badRequest(views.html.login.render(loginDTOForm.withGlobalError("Please login with proper credentials to modify this post")));
        postService.delete(postId);
        return redirect(routes.UserPostController.usersBlog(session("username"), 1));
    }

}
