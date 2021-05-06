package com.example.newcookbook;

public class CardRecipes {

    private String mImageResource;
    private String mText1;


    public CardRecipes(String text1, String imageResource) {
        mImageResource = imageResource;
        mText1 = text1;

    }

    public CardRecipes(String text1){
        mText1 = text1;
        mImageResource = "";
    }
    public void setText1(String text) {
        mText1 = text;
    }

    public String getImageResource() {
        return mImageResource;
    }

    public String getText1() {
        return mText1;
    }

    public void setmImageResource(String mImageResource) {
        this.mImageResource = mImageResource;
    }
}