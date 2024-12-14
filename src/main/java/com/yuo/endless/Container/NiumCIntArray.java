package com.yuo.endless.Container;

import net.minecraft.util.IIntArray;

public class NiumCIntArray implements IIntArray {
    private int number; //物品数量
    private int numberTotal; //配方物品总数
    private int posX;
    private int posY;
    private int posZ;
    @Override
    public int get(int index) {
        switch(index) {
            case 0:
                return number;
            case 1:
                return numberTotal;
            case 2:
                return posX;
            case 3:
                return posY;
            case 4:
                return posZ;
            default:
                return 0;
        }
    }

    @Override
    public void set(int index, int value) {
        switch(index) {
            case 0:
                number = value;
                break;
            case 1:
                numberTotal = value;
                break;
            case 2:
                posX = value;
                break;
            case 3:
                posY = value;
                break;
            case 4:
                posZ = value;
                break;
        }
    }

    @Override
    public int size() {
        return 5;
    }
}
