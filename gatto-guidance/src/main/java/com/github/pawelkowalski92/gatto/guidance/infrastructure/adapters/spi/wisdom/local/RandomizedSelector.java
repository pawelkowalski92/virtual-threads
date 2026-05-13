package com.github.pawelkowalski92.gatto.guidance.infrastructure.adapters.spi.wisdom.local;

import java.util.List;
import java.util.random.RandomGenerator;

public class RandomizedSelector {

    private final RandomGenerator randomizer;

    public RandomizedSelector(RandomGenerator randomizer) {
        this.randomizer = randomizer;
    }

    public <T> T selectRandomFrom(List<? extends T> collection) {
        var index = randomizer.nextInt(collection.size());
        return collection.get(index);
    }

    public <T> T selectRandomFrom(T[] array) {
        var index = randomizer.nextInt(array.length);
        return array[index];
    }

    public <E extends Enum<E>> E selectRandomFrom(Class<? extends E> enumType) {
        return selectRandomFrom(enumType.getEnumConstants());
    }

}
