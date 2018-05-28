package com.gregbclement.spellingtime;

import java.util.List;

public interface NetworkCallback<E> {
    void onComplete(E results);
}
