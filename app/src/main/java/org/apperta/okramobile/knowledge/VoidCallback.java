package org.apperta.okramobile.knowledge;

/**
 * This interface can be used to create anonymous classes for handling asynchronous method calls, e.g.
 * making an HTTP request to a remote server. By passing a callback object to an async method in a service,
 * it can call the onSuccess on onFailure methods of the object when the async operation completes,
 * and the client can handle the flow accordingly.
 */
public interface VoidCallback {
    void onSuccess();

    void onFailure();
}
