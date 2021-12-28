package xyz.v2my.easymodeling.randomizer.numberrandomizer;


import xyz.v2my.easymodeling.randomizer.NumberRandomizer;

public class LongRandomizer extends NumberRandomizer<Long> {

    @Override
    public Long next(long min, long max) {
        return doubleBetween(min, max).longValue();
    }
}
