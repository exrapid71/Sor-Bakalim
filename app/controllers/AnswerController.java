package controllers;

import annotations.Authenticated;
import dto.AnswerDTO;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import services.AnswerService;
import views.html.answer;

import javax.inject.Inject;

public class AnswerController extends Controller {

    private final AnswerService answerService;
    private final Form<AnswerDTO> answerForm;

    @Inject
    public AnswerController(AnswerService answerService, FormFactory formFactory) {
        this.answerService = answerService;
        this.answerForm = formFactory.form(AnswerDTO.class);
    }

    @Authenticated
    public Result getAnswerForm(Long postId) {
        AnswerDTO answerDTO = new AnswerDTO();
        answerDTO.postId = postId;
        return ok(answer.render(answerForm.fill(answerDTO), postId));
    }

    @Authenticated

    public Result createAnswer(Long postId) {
        Form<AnswerDTO> answerForm = this.answerForm.bindFromRequest();
        if (answerForm.hasErrors()) {
            return badRequest(answer.render(answerForm, postId));
        } else {
            AnswerDTO answerDTO = answerForm.get();
            answerDTO.username = session("username");
            answerDTO.postId = postId;
            answerService.saveAnswer(answerDTO);
            return redirect(routes.QuestionController.getQuestion(postId));
        }
    }
}
