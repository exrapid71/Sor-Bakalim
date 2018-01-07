package services.impl;

import dto.AnswerDTO;
import models.Answer;
import models.Post;
import models.User;
import services.AnswerService;
import services.PostService;
import services.UserService;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class AnswerServiceImpl implements AnswerService {

    private final UserService userService;
    private final PostService postService;

    @Inject
    public AnswerServiceImpl(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @Override
    public AnswerDTO saveAnswer(AnswerDTO answerDTO) {
        Answer answer = convertToEntity(answerDTO);
        answer.save();
        return convertToDTO(answer);
    }

    @Override
    public Optional<List<AnswerDTO>> findAnswersForPost(Long postId) {
        return Optional.ofNullable(Post.find.byId(postId))
                .map(post -> post.answers
                        .stream()
                        .map(this::convertToDTO)
                        .collect(toList()));
    }

    private AnswerDTO convertToDTO(Answer answer) {
        return new AnswerDTO(answer.body, answer.user.username, answer.post.id, answer.createDate);
    }

    private Answer convertToEntity(AnswerDTO answerDTO) {
        User user = userService.findUserEntityByUsername(answerDTO.username).orElse(null);
        Post post = postService.getPostEntity(answerDTO.postId).orElse(null);
        return new Answer(answerDTO.body, post, user);
    }
}
