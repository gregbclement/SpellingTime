package com.gregbclement.spellingtime;

import com.gregbclement.spellingtime.model.Score;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class ScoreUnitTest {
    @Test
    public void score_is_correct() {

        Score score = new Score();
        score.setScore(5);

        assertEquals((long)score.getScore(), 5);
    }
}
