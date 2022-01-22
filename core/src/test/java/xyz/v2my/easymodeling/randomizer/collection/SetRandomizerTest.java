package xyz.v2my.easymodeling.randomizer.collection;

import org.junit.jupiter.api.RepeatedTest;
import xyz.v2my.easymodeling.randomizer.RandomizerTest;
import xyz.v2my.easymodeling.randomizer.number.IntegerRandomizer;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class SetRandomizerTest extends RandomizerTest {

    @RepeatedTest(100)
    void should_generate_random_set_with_size_in_the_range() {
        SetRandomizer<Integer> randomizer = new SetRandomizer<>(new IntegerRandomizer(-2, 300), 3, 7);

        final Set<Integer> set = randomizer.next();

        assertThat(set)
                .hasSizeBetween(3, 6)
                .allMatch(i -> i >= -2 && i < 300);
    }
}
