package com.gregbclement.spellingtime.network;

import java.util.List;

/**
 * An interface that allows for defining an asynchronous network callback
 * @param <E>
 *
 */
public interface NetworkCallback<E> {
    void onComplete(E results);
}
