package org.apperta.okramobile;

import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class LoadingWebClient extends WebViewClient {
    LoadingDialog loadingDialog;

    public LoadingWebClient(LoadingDialog loadingDialog) {
        this.loadingDialog = loadingDialog;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        loadingDialog.hide();
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        loadingDialog.hide();
    }

}
