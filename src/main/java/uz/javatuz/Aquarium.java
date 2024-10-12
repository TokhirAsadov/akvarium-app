package uz.javatuz;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Aquarium {
    private  int width;
    private  int length;
    private  List<Fish> fishes = new CopyOnWriteArrayList<>();  // Thread-safe list

    public Aquarium(int width, int length) {
        this.width = width;
        this.length = length;
    }

    public void addFish(Fish fish) {
        fishes.add(fish);
        new Thread(fish).start(); // Start a new thread for each fish
    }

    public List<Fish> getFishes() {
        return fishes;
    }

    public int getWidth() {
        return width;
    }

    public int getLength() {
        return length;
    }
}
