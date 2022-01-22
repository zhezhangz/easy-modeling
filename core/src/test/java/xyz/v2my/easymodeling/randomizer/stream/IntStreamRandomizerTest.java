package xyz.v2my.easymodeling.randomizer.stream;

import org.junit.jupiter.api.RepeatedTest;
import xyz.v2my.easymodeling.randomizer.RandomizerTest;
import xyz.v2my.easymodeling.randomizer.number.IntegerRandomizer;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class IntStreamRandomizerTest extends RandomizerTest {

    @RepeatedTest(100)
    void should_generate_int_stream() {
        IntStreamRandomizer randomizer = new IntStreamRandomizer(new IntegerRandomizer(-5, 10), 100, 110);

        final List<Integer> ints = randomizer.next().boxed().collect(Collectors.toList());

        assertThat(ints).hasSizeBetween(100, 109);
        assertThat(ints).allMatch(i -> i >= -5 && i < 10);
    }
}
