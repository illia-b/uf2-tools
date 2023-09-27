package dev.vinca.tools.uf2.memory;

public interface Ram {
    long getSize();
    byte get8(long index);
    int get32(long index);
    void put8(long index, byte v);
    void put32(long index, int v);
}
