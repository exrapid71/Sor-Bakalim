package util;

import dto.QuestionDTO;
import io.ebean.PagedList;
import models.Question;

import java.util.List;
import java.util.stream.Collectors;

public class QuestionsPager {

    private final PagedList<Question> questionPagedList;

    public QuestionsPager(PagedList<Question> questionPagedList) {
        this.questionPagedList = questionPagedList;
    }

    public List<QuestionDTO> getList() {
        return questionPagedList.getList()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public int getPageIndex() {
        return questionPagedList.getPageIndex() + 1;
    }

    public int getPageSize() {
        return questionPagedList.getPageSize();
    }

    public boolean hasNext() {
        return getPageIndex() >= 1 && getPageIndex() < getTotalPageCount();
    }

    public boolean hasPrev() {
        return getPageIndex() > 1 && getPageIndex() <= getTotalPageCount();
    }

    public int getTotalPageCount() {
        return questionPagedList.getTotalPageCount();
    }

    public int getTotalCount() {
        return questionPagedList.getTotalCount();
    }

    public boolean indexOutOfBounds() {
        return getPageIndex() < 0 || getPageIndex() > getTotalCount();
    }

    private QuestionDTO convertToDTO(Question question) {
        return new QuestionDTO(question.id, question.title, question.body, question.createDate, question.user.username);
    }
}
