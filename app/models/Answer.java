package models;

import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.annotation.CreatedTimestamp;
import io.ebean.annotation.NotNull;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Answer extends Model {

    @Id
    public Long id;

    @Column(columnDefinition = "TEXT")
    public String body;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedTimestamp
    @Column(updatable = false)
    public Date createDate;

    @NotNull
    @ManyToOne
    public Question question;

    @NotNull
    @ManyToOne
    public User user;

    public static final Finder<Long, Answer> find = new Finder<>(Answer.class);

    public Answer(String body, Question question, User user) {
        this.body = body;
        this.question = question;
        this.user = user;
    }
}
