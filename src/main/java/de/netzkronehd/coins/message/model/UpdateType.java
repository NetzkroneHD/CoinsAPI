package de.netzkronehd.coins.message.model;

import de.netzkronehd.coins.source.CoinsSource;

public enum UpdateType {

    SET {
        @Override
        public double apply(CoinsSource source, double amount) {
            return source.setCoins(amount);
        }
    },
    ADD {
        @Override
        public double apply(CoinsSource source, double amount) {
            return source.addCoins(amount);
        }
    },
    REMOVE {
        @Override
        public double apply(CoinsSource source, double amount) {
            return source.removeCoins(amount);
        }
    };

    public abstract double apply(CoinsSource source, double amount);

}
