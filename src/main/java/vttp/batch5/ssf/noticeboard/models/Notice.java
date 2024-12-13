package vttp.batch5.ssf.noticeboard.models;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

public class Notice {

    @NotEmpty(message = "Your title cannot be empty")
    @Size(min = 3, max = 128, message = "Your title must be between 3 and 128 characters")
    private String title; // Title of the notice

    @Email(message = "Please enter a valid email address")
    @NotEmpty(message = "Your email is mandatory")
    private String poster; // E-mail address of the poster

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Future(message = "The date of the post must be a date in the future")
    @NotNull(message = "Date cannot be empty")
    private Date postDate; // when the post should be posted

    @NotEmpty(message = "You must pick at least one category")
    private List<String> categories; // One or more categories that the notice belongs to

    @NotBlank(message = "Your text cannot be empty")
    private String text; // The contents of the notice

    public Notice() {
    }

    public Notice(String title, String poster, Date postDate, List<String> categories, String text) {
        this.title = title;
        this.poster = poster;
        this.postDate = postDate;
        this.categories = categories;
        this.text = text;
    }

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public String getPoster() {return poster;}
    public void setPoster(String poster) {this.poster = poster;}

    public Date getPostDate() {return postDate;}
    public void setPostDate(Date postDate) {this.postDate = postDate;}

    public List<String> getCategories() {return categories;}
    public void setCategories(List<String> categories) {this.categories = categories;}

    public String getText() {return text;}
    public void setText(String text) {this.text = text;}

    @Override
    public String toString() {
        return "NoticeModel{" +
                "title='" + title + '\'' +
                ", poster='" + poster + '\'' +
                ", postDate=" + postDate +
                ", categories=" + categories +
                ", text='" + text + '\'' +
                '}';
    }
}
