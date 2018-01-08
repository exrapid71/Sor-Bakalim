package util;

import dto.QuestionDTO;
import io.ebean.PagedList;
import models.Post;

import java.util.List;
import java.util.stream.Collectors;

public class PostsPager {

    private final PagedList<Post> postPagedList;

    public PostsPager(PagedList<Post> postPagedList) {
        this.postPagedList = postPagedList;
    }

    public List<QuestionDTO> getList() {
        return postPagedList.getList()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public int getPageIndex() {
        return postPagedList.getPageIndex() + 1;
    }

    public int getPageSize() {
        return postPagedList.getPageSize();
    }

    public boolean hasNext() {
        return getPageIndex() >= 1 && getPageIndex() < getTotalPageCount();
    }

    public boolean hasPrev() {
        return getPageIndex() > 1 && getPageIndex() <= getTotalPageCount();
    }

    public int getTotalPageCount() {
        return postPagedList.getTotalPageCount();
    }

    public int getTotalCount() {
        return postPagedList.getTotalCount();
    }

    public boolean indexOutOfBounds() {
        return getPageIndex() < 0 || getPageIndex() > getTotalCount();
    }

    private QuestionDTO convertToDTO(Post post) {
        return new QuestionDTO(post.id, post.title, post.body, post.createDate, post.user.username);
    }
}
