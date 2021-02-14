package net.silentchaos512.gemschaos.api;

public interface IChaosSource {
    int getChaos();

    void setChaos(int amount);

    default void addChaos(int amount) {
        setChaos(getChaos() + amount);
    }

    default int dissipateChaos(int amount) {
        int current = getChaos();
        addChaos(-amount);
        return Math.max(amount - current, 0);
    }
}
