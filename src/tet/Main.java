package tet;

import tet.threadpool.gauss.GaussSeidelActor;
import tet.threadpool.gauss.GaussSeidelMessage;
import tet.threadpool.par.Actor;
import tet.threadpool.par.Engine;

import java.util.Random;

import static tet.params.Constants.H;
import static tet.params.Constants.N;
import static tet.params.Constants.W;

public class Main {

    private static double[][] field = new double[H][W];

    public static Actor[] a;
    public static GaussSeidelMessage[] m;
    public static int time[] = new int[N];


    public static void main(String[] args) {
        par_thread_pool();
    }

    public static void shufle() {
        Random random = new Random();
        for (int i = 0; i < H; i++) {
            for (int j = 0; j < W; j++) {
                double srand = random.nextDouble();
                field[i][j] = srand;
            }
        }
    }

    public static void operation(int i) {
        for (int j = 1; j < W - 1; j++) {
            field[i][j] = (field[i][j - 1] + field[i][j + 1] + field[i - 1][j] + field[i + 1][j]) * 0.25;
        }
    }

    static double par_thread_pool() {
        Engine engine = new Engine();
        a = new Actor[N];
        m = new GaussSeidelMessage[N - 1];

        for (int i = 0; i < N; i++) {
            a[i] = new GaussSeidelActor(engine);
            a[i].id = i;
            time[i] = 1;
//            Thread thread = new Thread(a[i], String.valueOf(a[i].id));
//            thread.start();
        }

        /***
         * Инициализация массива сообщений
         */
        for (int i = 0; i < N - 1; i++) {
            m[i] = new GaussSeidelMessage(i);
            m[i].id = i;
            m[i].sending = (i == 0);
        }

        long time = System.nanoTime();
        m[0].send(engine, m[0], a[0]); //отправляем сообщение 0-му актору
        engine.run();
        return ((double) System.nanoTime() - time) / 1E9;
    }

}
