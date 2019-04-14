package com.lin.learn.disruptor;

public class LongEvent {

    private long value;

    public void set(long value)
    {
        this.value = value;
    }

    public long getValue() {
        return value;
    }
}