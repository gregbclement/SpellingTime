package com.gregbclement.spellingtime;

import java.util.List;

/**
 *
 * @param <E>
 */
public interface NetworkCallback<E> {
    void onComplete(E results);
}
