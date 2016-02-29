package com.twt.service.interactor;

import java.io.File;

/**
 * Created by sunjuntao on 16/2/20.
 */
public interface FoundInteractor {

    void getFoundList(int page);

    void getFoundDetails(int id);

    void uploadImage(File file);
}
