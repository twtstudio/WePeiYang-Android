package com.twt.service.ui.lostfound.post.found;

import com.twt.service.bean.Upload;

/**
 * Created by sunjuntao on 16/2/26.
 */
public class UploadSuccessEvent {
    private Upload upload;

    public UploadSuccessEvent(Upload upload) {
        this.upload = upload;
    }

    public Upload getUpload() {
        return upload;
    }
}
