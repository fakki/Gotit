package com.mycompany.gotit;

public class Book {
    private String mTitle;
    private double mMark;
    private String mPublishInfo;
    private String mIntroduction;
    private String mDetailPageUrl;
    private String mImageUrl;

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public double getMark() {
        return mMark;
    }

    public void setMark(double mMark) {
        this.mMark = mMark;
    }

    public String getPublishInfo() {
        return mPublishInfo;
    }

    public void setPublishInfo(String mPublishInfo) {
        this.mPublishInfo = mPublishInfo;
    }

    public String getIntroduction() {
        return mIntroduction;
    }

    public void setIntroduction(String mIntroduction) {
        this.mIntroduction = mIntroduction;
    }

    public String getDetailPageUrl() {
        return mDetailPageUrl;
    }

    public void setDetailPageUrl(String mDetailPageUrl) {
        this.mDetailPageUrl = mDetailPageUrl;
    }

}
