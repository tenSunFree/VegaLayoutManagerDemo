package com.tensun.vegalayoutmanagerdemo;

/**
 * Created by xmuSistone on 2017/9/20.
 */

public class StockEntity {

    private String text;
    private String image;

    public StockEntity(String text, String image) {
        this.text = text;
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public String getImage() {
        return image;
    }
}
