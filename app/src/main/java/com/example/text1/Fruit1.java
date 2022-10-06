package com.example.text1;

import android.graphics.Bitmap;

class Fruit1 {

    private final String name;

    private final Bitmap imageId;

    private final String explanation;

    public Fruit1(String name, Bitmap imageId,String explanation) {
        this.name = name;
        this.imageId = imageId;
        this.explanation=explanation;
    }

    public String getName() {
        return name;
    }

    public String getExplanation() {
        return explanation;
    }

    public Bitmap getImageId() {
        return imageId;
    }

}
