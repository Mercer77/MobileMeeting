package com.example.mercer.mobilemeeting.pojo;

/**
 * ..
 * author:liangliangattack 1364744931@.qq.com
 * Administrator on 2018/7/24 20:27.
 */
public class BannerModel {
    private String tips;
    private String imageUrl;

    public BannerModel(){

    }

    public BannerModel(String tips, String imageUrl) {
        this.tips = tips;
        this.imageUrl = imageUrl;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
