package models;

import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.annotation.CreatedTimestamp;
import io.ebean.annotation.NotNull;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Question extends Model {

    @Id
    public Long id;

    @NotNull
    public String title;

    @Column(columnDefinition = "TEXT")
    public String body;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedTimestamp
    @Column(updatable = false)
    public Date createDate;



    @NotNull
    @ManyToOne
    public User user;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    public List<Answer> answers;

    public static final Finder<Long, Question> find = new Finder<>(Question.class);
    public static final Finder<String, Question> findTitle = new Finder<>(Question.class);

    public Question(String title, String body, User user) {
        this.title = title;
        this.body = body;
        this.user = user;
    }

}
