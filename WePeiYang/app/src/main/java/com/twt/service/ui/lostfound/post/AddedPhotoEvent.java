package com.twt.service.ui.lostfound.post;

/**
 * Created by sunjuntao on 16/3/14.
 */
public class AddedPhotoEvent {
    private String filePath;

    public AddedPhotoEvent(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }
}
