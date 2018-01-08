package services.impl;

import dto.QuestionDTO;
import models.Post;
import models.User;
import services.PostService;
import services.UserService;
import util.PostsPager;

import javax.inject.Inject;
import java.util.Optional;

public class PostServiceImpl implements PostService {

    private final UserService userService;

    @Inject
    public PostServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public PostsPager findNLatestPosts(int n, int page) {
        return new PostsPager(Post.find.query().orderBy("create_date desc").setFirstRow(n * (page - 1)).setMaxRows(n).findPagedList());
    }

    @Override
    public Optional<PostsPager> findNLatestPostsForUsername(int n, int page, String username) {
        return User.find.query().where().eq("username", username).findOneOrEmpty()
                .map(user -> new PostsPager(
                        Post.find.query()
                                .where().eq("user_id", user.id)
                                .orderBy("create_date desc")
                                .setFirstRow(n * (page - 1))
                                .setMaxRows(n)
                                .findPagedList()));
    }

    @Override
    public Optional<QuestionDTO> getPost(Long postId) {
        return getPostEntity(postId)
                .map(this::convertToDTO);
    }

    @Override
    public Optional<Post> getPostEntity(Long postId) {
        return Optional.ofNullable(Post.find.byId(postId));
    }

    @Override
    public QuestionDTO savePost(QuestionDTO questionDTO) {
        Post post = convertToEntity(questionDTO);
        post.save();
        return convertToDTO(post);
    }

    @Override
    public Optional<QuestionDTO> editPost(QuestionDTO questionDTO) {
        return getPostEntity(questionDTO.id)
                .map(post -> {
                    post.body = questionDTO.body;
                    post.title = questionDTO.title;
                    post.save();
                    return convertToDTO(post);
                });
    }

    @Override
    public void delete(Long postId) {
        Post.find.deleteById(postId);
    }

    private QuestionDTO convertToDTO(Post post) {
        return new QuestionDTO(post.id, post.title, post.body, post.createDate, post.user.username);
    }

    private Post convertToEntity(QuestionDTO questionDTO) {
        User user = userService.findUserEntityByUsername(questionDTO.username).orElse(null);
        return new Post(questionDTO.title, questionDTO.body, user);
    }

}
