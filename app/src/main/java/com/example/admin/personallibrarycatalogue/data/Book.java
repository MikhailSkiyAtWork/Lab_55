package com.example.admin.personallibrarycatalogue.data;

import android.graphics.drawable.Drawable;

/**
 * Created by Mikhail Valuyskiy on 25.05.2015.
 */
public class Book {
    private String title_;
    private String author_;
    private String description_;
    private Drawable cover_;

    public Book(String title,String author, String description, Drawable cover){
        this.title_ = title;
        this.author_ = author;
        this.description_ = description;
        this.cover_ = cover;
    }

    public Book(String title,String author){
        this.title_ = title;
        this.author_ = author;
    }

    public String getTitle(){
        return title_;
    }

    public void setTitle(String title){
        this.title_ = title;
    }

    public String getAuthor(){
        return author_;
    }

    public void setAuthor(String author){
        this.author_ = author;
    }

}
