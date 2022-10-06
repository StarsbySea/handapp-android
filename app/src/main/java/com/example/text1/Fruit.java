package com.example.text1;

public class Fruit {

    private final String name;

    private final int imageId;

    private final String explanation;


    public Fruit(String name, int imageId,String explanation) {
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

    public int getImageId() {
        return imageId;
    }

}

