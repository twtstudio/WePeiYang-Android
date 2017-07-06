package com.twtstudio.retrox.one;

import io.rx_cache.DynamicKey;
import io.rx_cache.Reply;
import rx.Observable;

/**
 * Created by retrox on 2017/2/11.
 */

public interface OneCacheApi {

    Observable<Reply<IdList>> getIdListAuto(Observable<IdList> obIdList, DynamicKey date);

    Observable<Reply<OneDetailBean>> getOneDetailAuto(Observable<OneDetailBean> obDetailBean,DynamicKey id);
}
