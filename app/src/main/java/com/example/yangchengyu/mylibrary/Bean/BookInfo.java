package com.example.yangchengyu.mylibrary.Bean;

import java.io.Serializable;

/**
 * Created by YangChengyu on 2017/5/6.
 */

public class BookInfo implements Serializable {

    //implements Serialzable是为了在Intent中能够直接传递Book对象
    private String title;
    private String author;
    private String publisher;
    private String pubdate;
    private String summary;
    private String image;

    public BookInfo(){

    }

    public BookInfo(String title, String author, String publisher, String pubdate, String summary, String image) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.pubdate = pubdate;
        this.summary = summary;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getInfoString() {

        return this.author + "/" + this.publisher + "/" + this.pubdate;

    }


}
