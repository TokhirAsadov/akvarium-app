package uz.javatuz;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) {
        Random random = new Random();

        // Akvariumga o`lcham berish
        int length = random.nextInt(50) + 50; // Random length between 50 and 100
        int width = random.nextInt(30) + 30;  // Random width between 30 and 60
        System.out.println("Creating an aquarium with dimensions: " + length + "x" + width);

        // Erkak va urg'ochi baliqlar sonini aniqlash
        int numMales = random.nextInt(10) + 5;   // Random number of males between 5 and 14
        int numFemales = random.nextInt(8) + 4;  // Random number of females between 4 and 11
        System.out.println("Adding " + numMales + " male and " + numFemales + " female fish to the aquarium.");

        // Shared list and lock for fish in the aquarium
        List<Fish> aquarium = Collections.synchronizedList(new ArrayList<>());
        Lock aquariumLock = new ReentrantLock();

        // Create a thread pool for fish
        ExecutorService executor = Executors.newFixedThreadPool(numMales + numFemales);
        List<Future<?>> futures = new ArrayList<>();

        // Create and start threads for each male fish
        for (int i = 1; i <= numMales; i++) {
            int lifetime = random.nextInt(20) + 10; // Random lifetime between 10 and 29 days
            Fish fish = new Fish("Male Fish " + i, "male", lifetime, aquarium, aquariumLock);
            aquarium.add(fish);
            futures.add(executor.submit(fish));
        }

        // Create and start threads for each female fish
        for (int i = 1; i <= numFemales; i++) {
            int lifetime = random.nextInt(20) + 10; // Random lifetime between 10 and 29 days
            Fish fish = new Fish("Female Fish " + i, "female", lifetime, aquarium, aquariumLock);
            aquarium.add(fish);
            futures.add(executor.submit(fish));
        }

        // Wait for all fish threads to complete
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        // Shutdown the executor service
        executor.shutdown();
        System.out.println("All fish have passed away. Aquarium management program complete.");
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
        Random random = new Random();
        for (int day = 1; day <= lifetime; day++) {
            System.out.println(name + " the " + gender + " fish is swimming. Day " + day);

            // Tekshirish agar baliq boshqa baliq bilan uchrashsa
            try {
                Thread.sleep(1000); // Simulate a day passing
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Qarama qarshi jinsli baliqlarning uchrashib qolish ehtimolini tekshirish
            aquariumLock.lock();
            try {
                Fish mate = findMate();
                if (mate != null) {
                    System.out.println(name + " met " + mate.getName() + ". They are having offspring!");
                    int numOffspring = random.nextInt(3) + 1; // Yangi tug`ilgan baliqchalar sonini bilmaganim uchun shunday qildim! Random number of offspring between 1 and 3
                    for (int i = 1; i <= numOffspring; i++) {
                        String offspringGender = random.nextBoolean() ? "male" : "female"; // Yangi tug'ilgan har bir baliqning jinsi Random orqali aniqlanadi.
                        String offspringName = "Offspring of " + name + " and " + mate.getName() + " " + i;
                        int offspringLifetime = random.nextInt(20) + 10; // Lifetime between 10 and 29 days
                        Fish offspring = new Fish(offspringName, offspringGender, offspringLifetime, aquarium, aquariumLock);
                        aquarium.add(offspring);
                        System.out.println("New fish born: " + offspringName + " (" + offspringGender + ")");
                    }
                }
            } finally {
                aquariumLock.unlock();
            }
        }
        System.out.println(name + " the " + gender + " fish has passed away. Lifetime reached.");
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