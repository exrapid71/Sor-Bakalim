package services;

import dto.QuestionDTO;
import models.Post;
import util.PostsPager;

import java.util.List;
import java.util.Optional;

public interface PostService {

    PostsPager findNLatestPosts(int n, int page);

    Optional<PostsPager> findNLatestPostsForUsername(int n, int page, String username);

    Optional<QuestionDTO> getPost(Long postId);

    Optional<Post> getPostEntity(Long postId);

    QuestionDTO savePost(QuestionDTO questionDTO);

    Optional<QuestionDTO> editPost(QuestionDTO questionDTO);

    void delete(Long postId);
}
