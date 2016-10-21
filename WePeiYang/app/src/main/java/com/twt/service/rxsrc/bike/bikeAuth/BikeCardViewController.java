package com.twt.service.rxsrc.bike.bikeAuth;

import com.twt.service.rxsrc.common.IViewController;
import com.twt.service.rxsrc.model.BikeCard;

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
