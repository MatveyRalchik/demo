package com.ralchik.demo.domain;

import javax.persistence.*;

@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String text;
    private String tag;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;
    private String filename;

    public Message() { }
    public Message(String text, String tag, User user) {
        this.text = text;
        this.tag = tag;
        this.author = user;
    }

    public String getAuthorName() {
        return author != null ? author.getUsername() : "<none>";
    }

    public Integer getId() {
        return id;
    }
    public String getText() {
        return text;
    }
    public String getTag() {
        return tag;
    }
    public User getAuthor() {
        return author;
    }
    public String getFilename() {
        return filename;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public void setText(String text) {
        this.text = text;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }
    public void setAuthor(User author) {
        this.author = author;
    }
    public void setFilename(String filename) {
        this.filename = filename;
    }

}
