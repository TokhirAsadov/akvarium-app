package uz.javatuz;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello World");
    }
}




// Fish class representing each fish in the aquarium
class Fish implements Runnable {
    private String name;
    private String gender;
    private int lifetime; // Lifetime of the fish in days
    private List<Fish> aquarium; // Barcha baliqlarni o'z ichiga olgan akvariumni ifodalovchi umumiy ro'yxat.
    private Lock aquariumLock;

    public Fish(String name, String gender, int lifetime, List<Fish> aquarium, Lock aquariumLock) {
        this.name = name;
        this.gender = gender;
        this.lifetime = lifetime;
        this.aquarium = aquarium;
        this.aquariumLock = aquariumLock;
    }

    public String getGender() {
        return gender;
    }

    public String getName() {
        return name;
    }


    @Override
    public void run() {

    }


    private Fish findMate() {
        for (Fish fish : aquarium) {
            if (fish != this && fish.getGender().equals("female") && this.gender.equals("male")) {
                return fish; // Return a female mate if found
            } else if (fish != this && fish.getGender().equals("male") && this.gender.equals("female")) {
                return fish; // Return a male mate if found
            }
        }
        return null; // No mate found
    }
}