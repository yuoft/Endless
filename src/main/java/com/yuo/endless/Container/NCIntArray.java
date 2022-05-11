package com.yuo.endless.Container;

import net.minecraft.util.IIntArray;

public class NCIntArray implements IIntArray {
    private int timer; //计时器
    @Override
    public int get(int index) {
        if (index == 0) {
            return timer;
        }
        return 0;
    }

    @Override
    public void set(int index, int value) {
        if (index == 0) {
            timer = value;
        }
    }

    @Override
    public int size() {
        return 1;
    }
}
