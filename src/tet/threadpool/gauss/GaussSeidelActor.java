package tet.threadpool.gauss;


import tet.Main;
import tet.threadpool.par.Actor;
import tet.threadpool.par.Engine;
import tet.threadpool.par.Message;

import static tet.Main.a;
import static tet.Main.m;
import static tet.params.Constants.N;
import static tet.params.Constants.T;

public class GaussSeidelActor extends Actor {
    private boolean access_ms_id_minus_1 = false;
    private boolean access_ms_id = true;

    public GaussSeidelActor(Engine engine) {
        super(engine);
    }

    @Override
    public void recv(Message message) {
        GaussSeidelMessage msg = (GaussSeidelMessage) message;
        int i = msg.getX();
        if (i == id - 1) access_ms_id_minus_1 = true;
        if (i == id) access_ms_id = true;

        if ((id == 0 || access_ms_id_minus_1) &&
                (id == N - 1 || access_ms_id) &&
                (Main.time[id] <= T)) {
            Main.operation(id + 1);
            Main.time[id]++;
            System.out.println(id + "  " + String.valueOf(Main.time[id]));

            if (id != 0) {
                access_ms_id_minus_1 = false;
                Message.send(a[id - 1].engine, m[id - 1], a[id - 1]);
                System.out.println("1 send **id != 0** сообщение: " + m[id - 1].getX() + " актор № " + (id - 1));

            }
            if (id != N - 1) {
                access_ms_id = false;
                Message.send(a[id + 1].engine, m[id], a[id + 1]);
                System.out.println("2 **id != N - 1** сообщение: " + (m[id].getX()) + " актор № " + (id + 1));

            }
        }
        if (Main.time[id] == T-1 && id == a.length - 1) {
            System.out.println("************************************************finished " + id);
            a[id].engine.setFinished(true);
        }
    }
}
