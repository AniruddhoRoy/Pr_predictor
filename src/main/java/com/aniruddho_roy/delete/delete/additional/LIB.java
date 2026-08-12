package com.aniruddho_roy.delete.delete.additional;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LIB {
     public ImageView Loadimage(String url,float width , float height ,boolean ratio){
        Image image = new Image(
                getClass().getResourceAsStream(url)
        );

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setPreserveRatio(ratio);
        return imageView;
    }
}
