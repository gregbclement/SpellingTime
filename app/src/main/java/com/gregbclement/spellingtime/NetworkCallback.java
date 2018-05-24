package com.gregbclement.spellingtime;

import java.util.List;

public interface NetworkCallback<E> {
    void onGetStudents(E students);
}
