package com.gregbclement.spellingtime.network;

import java.util.List;

/**
 *
 * @param <E>
 */
public interface NetworkCallback<E> {
    void onComplete(E results);
}
