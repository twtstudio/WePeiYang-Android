package com.twt.service.ui.lostfound.post.found;

import com.twt.service.bean.Upload;

import java.util.List;

/**
 * Created by sunjuntao on 16/2/26.
 */
public class UploadSuccessEvent {
    private List<Upload> uploads;

    public UploadSuccessEvent(List<Upload> uploads) {
        this.uploads = uploads;
    }

    public List<Upload> getUploads() {
        return uploads;
    }
}
