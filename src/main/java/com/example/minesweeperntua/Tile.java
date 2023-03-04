package com.example.minesweeperntua;

public class Tile {
    private int x, y;
    private boolean bomb;
    private boolean flag;
    private boolean open;
    private boolean superBomb;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isBomb() {
        return bomb;
    }

    public void setBomb(boolean bomb) {
        this.bomb = bomb;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isSuperBomb() {
        return superBomb;
    }

    public void setSuperBomb(boolean superBomb) {
        this.superBomb = superBomb;
    }

    public Tile(int x, int y, boolean bomb, boolean flag, boolean open, boolean superBomb) {
        this.x = x;
        this.y = y;
        this.bomb = bomb;
        this.flag = flag;
        this.open = open;
        this.superBomb = superBomb;
    }
}
