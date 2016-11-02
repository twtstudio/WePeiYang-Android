package com.twt.service.rxsrc.read.home.profile;

import com.twt.service.rxsrc.model.read.BookInShelf;

import java.util.List;

/**
 * Created by tjliqy on 2016/11/1.
 */

public interface BookProfileController {
    void bindBookShelfData(List<BookInShelf> booksInShelf);
    void delBookInShelfSuccess();
}
