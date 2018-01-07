package controllers;

import annotations.Authenticated;
import dto.LoginDTO;
import dto.PostDTO;
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
    private final Form<PostDTO> postForm;
    private final Form<LoginDTO> loginDTOForm;

    @Inject
    public QuestionController(PostService postService, AnswerService answerService, FormFactory formFactory) {
        this.postService = postService;
        this.answerService = answerService;
        this.postForm = formFactory.form(PostDTO.class);
        this.loginDTOForm = formFactory.form(LoginDTO.class);
    }

    public Result getPost(Long postId) {
        return answerService.findAnswersForPost(postId)
                .map(answerDTOs ->
                        postService.getPost(postId)
                                .map(postDTO -> ok(post.render(postDTO, answerDTOs)))
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
        Optional<PostDTO> optionalPost = postService.getPost(postId);
        if (optionalPost.isPresent() && !optionalPost.get().username.equals(session("username")))
            return badRequest(views.html.login.render(loginDTOForm.withGlobalError("Please login with proper credentials to modify this post")));
        return postService.getPost(postId)
                .map(postDTO -> ok(editPost.render(postForm.fill(postDTO), postId)))
                .orElseGet(Results::notFound);
    }

    @Authenticated
    public Result createPost() {
        Form<PostDTO> postForm = this.postForm.bindFromRequest();
        if (postForm.hasErrors()) {
            return badRequest(newPost.render(postForm));
        } else {
            PostDTO postDTO = postForm.get();
            postDTO.username = session("username");
            postDTO = postService.savePost(postDTO);
            return redirect(routes.QuestionController.getPost(postDTO.id));
        }
    }

    @Authenticated
//    @PostExistsAndUserIsOwner
    public Result editPost(Long postId) {
        Optional<PostDTO> optionalPost = postService.getPost(postId);
        if (optionalPost.isPresent() && !optionalPost.get().username.equals(session("username")))
            return badRequest(views.html.login.render(loginDTOForm.withGlobalError("Please login with proper credentials to modify this post")));
        Form<PostDTO> postForm = this.postForm.bindFromRequest();
        if (postForm.hasErrors()) {
            return badRequest(editPost.render(postForm, postId));
        } else {
            PostDTO postDTO = postForm.get();
            postDTO.id = postId;
            return postService.editPost(postDTO)
                    .map(x -> redirect(routes.QuestionController.getPost(postId)))
                    .orElseGet(Results::notFound);
        }
    }

    @Authenticated
//    @PostExistsAndUserIsOwner
    public Result deletePost(Long postId) {
        Optional<PostDTO> optionalPost = postService.getPost(postId);
        if (optionalPost.isPresent() && !optionalPost.get().username.equals(session("username")))
            return badRequest(views.html.login.render(loginDTOForm.withGlobalError("Please login with proper credentials to modify this post")));
        postService.delete(postId);
        return redirect(routes.UserPostController.usersBlog(session("username"), 1));
    }

}
