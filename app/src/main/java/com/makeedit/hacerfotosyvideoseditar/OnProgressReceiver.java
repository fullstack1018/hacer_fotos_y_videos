package com.makeedit.hacerfotosyvideoseditar;

public interface OnProgressReceiver {
    void onImageProgressFrameUpdate(float f);

    void onProgressFinish(String str);

    void onVideoProgressFrameUpdate(float f);
}
