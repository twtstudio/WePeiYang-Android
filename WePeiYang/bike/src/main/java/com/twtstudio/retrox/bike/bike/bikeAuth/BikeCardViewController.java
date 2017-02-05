package com.twtstudio.retrox.bike.bike.bikeAuth;


import com.twtstudio.retrox.bike.common.IViewController;
import com.twtstudio.retrox.bike.model.BikeCard;

import java.util.List;

/**
 * Created by jcy on 2016/8/21.
 */

public interface BikeCardViewController extends IViewController {
    void setCardList(List<BikeCard> cardList);

    void onError(String s);

    void onCardSelected();

    void onCardBind();
}
