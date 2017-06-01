package tet.threadpool.gauss;

import tet.threadpool.par.Message;

public class GaussSeidelMessage extends Message {
    int x;

    public GaussSeidelMessage (int i) {
        x = i;
    }

    int getX() {
        return x;
    }
}
