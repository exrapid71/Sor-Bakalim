package actions;

import annotations.PostExistsAndUserIsOwner;
import dto.LoginDTO;
import dto.QuestionDTO;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import services.QuestionService;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class PostExistsAndUserIsOwnerAction extends Action<PostExistsAndUserIsOwner> {

    private final QuestionService questionService;
    private final Form<LoginDTO> loginDTOForm;

    @Inject
    public PostExistsAndUserIsOwnerAction(QuestionService questionService, FormFactory formFactory) {
        this.questionService = questionService;
        this.loginDTOForm = formFactory.form(LoginDTO.class);
    }

    public CompletionStage<Result> call(final Http.Context ctx) {
        String username = ctx.session().get("username");
        Optional<QuestionDTO> optionalQuestion = questionService.getQuestion(6L);
        if (!optionalQuestion.isPresent()) {
            return CompletableFuture.completedFuture(notFound());
        } else if (!optionalQuestion.get().username.equals(username)) {
            Result login = badRequest(views.html.login.render(loginDTOForm.withGlobalError("Please login with proper credentials to modify this question")));
            return CompletableFuture.completedFuture(login);
        } else {
            return delegate.call(ctx);
        }
    }

}
