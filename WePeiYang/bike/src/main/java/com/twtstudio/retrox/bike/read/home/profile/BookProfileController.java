package com.twtstudio.retrox.bike.read.home.profile;


import com.twtstudio.retrox.bike.model.read.BookInShelf;
import com.twtstudio.retrox.bike.model.read.Review;

import java.util.List;

/**
 * Created by tjliqy on 2016/11/1.
 */

public interface BookProfileController {
    void bindBookShelfData(List<BookInShelf> booksInShelf);
    void delBookInShelfSuccess();
    void bindReviewData(List<Review> reviews);
}
