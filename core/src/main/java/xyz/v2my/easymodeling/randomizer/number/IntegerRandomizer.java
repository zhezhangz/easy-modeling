package xyz.v2my.easymodeling.randomizer.number;


public class IntegerRandomizer extends NumberRandomizer<Integer> {

    public IntegerRandomizer(double min, double max) {
        super(min, max);
    }

    public IntegerRandomizer(Integer constant) {
        super(constant);
    }

    @Override
    public Integer random() {
        return doubleBetween(min, max).intValue();
    }
}
