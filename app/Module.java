import com.google.inject.AbstractModule;
import services.AnswerService;
import services.QuestionService;
import services.UserService;
import services.impl.AnswerServiceImpl;
import services.impl.QuestionServiceImpl;
import services.impl.UserServiceImpl;


public class Module extends AbstractModule {

    @Override
    public void configure() {
        bind(AnswerService.class).to(AnswerServiceImpl.class);
        bind(QuestionService.class).to(QuestionServiceImpl.class);
        bind(UserService.class).to(UserServiceImpl.class);
    }

}
