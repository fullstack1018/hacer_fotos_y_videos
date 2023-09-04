package com.makeedit.hacerfotosyvideoseditar.libffmpeg;

public interface FFmpegExecuteResponseHandler extends ResponseHandler {
    void onFailure(String str);

    void onProgress(String str);

    void onSuccess(String str);
}
