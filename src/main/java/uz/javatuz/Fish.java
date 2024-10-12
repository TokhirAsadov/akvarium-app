package uz.javatuz;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Fish implements Runnable {
    private String name;
    private String gender;
    private int lifetime;
    private int x, y;
    private Aquarium aquarium;
    private Random random = new Random();

    /**
     * offspringCooldown field - baliqning yangi bolasini tug‘ishidan so‘ng qancha vaqt (soniyalarda) o‘tishi kerakligini belgilaydi;
     * Baliqlarning juda tez-tez bola tug‘ishining oldini olish uchun;
     * va bu orqali akvariumda haddan tashqari ko‘payish va muammolarni oldini olishga yordam beradi
     * */
    private int offspringCooldown = 5;

    /**
     * timeSinceLastOffspring field - ushbu maydon baliqning oxirgi avlod tug‘ilganidan keyin qancha vaqt o‘tgani haqida ma’lumot beradi.
     * Ushbu o'zgaruvchi har bir baliqning run metodining har bir iteratsiyasida oshiriladi va reproduktiv imkoniyatlarni aniqlashda ishlatiladi.
     * Agar bu qiymat offspringCooldown ga teng yoki undan katta bo'lsa, baliq yana avlod tug‘ishi mumkin.
     * */
    private int timeSinceLastOffspring = offspringCooldown;

    public Fish(String name, String gender, int lifetime, Aquarium aquarium) {
        this.name = name;
        this.gender = gender;
        this.lifetime = lifetime;
        this.aquarium = aquarium;
        this.x = random.nextInt(aquarium.getWidth());
        this.y = random.nextInt(aquarium.getLength());
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    @Override
    public void run() {
        int age = 0;
        while (age < lifetime) {
            try {
                Thread.sleep(1000);  // Baliq har bir sekund yashaydi
                move();
                checkForOffspring();
                age++;
                timeSinceLastOffspring++;  // Increase time since last reproduction
                System.out.println(name + " fish at (" + x + "," + y + ") has lived for " + age + " seconds.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(name + " fish at (" + x + "," + y + ") has died.");
    }

    private void move() {
        x = random.nextInt(aquarium.getWidth());
        y = random.nextInt(aquarium.getLength());
    }

    private void checkForOffspring() {
        if (timeSinceLastOffspring >= offspringCooldown) {  // Ensure cooldown period before next reproduction
            synchronized (aquarium.getFishes()) {
                for (Fish otherFish : aquarium.getFishes()) {
                    if (this != otherFish && this.x == otherFish.getX() && this.y == otherFish.getY()) {
                        if (!this.gender.equals(otherFish.getGender())) {
                            produceOffspring(name,otherFish.getName());
                            timeSinceLastOffspring = 0;  // Reset cooldown after producing offspring
                        }
                    }
                }
            }
        }
    }

    private void produceOffspring(String fish1, String fish2) {
        String[] genders = {"Male", "Female"};
        String offspringGender = genders[random.nextInt(2)];
        int offspringLifetime = random.nextInt(10) + 5;
        int sizeAdd = aquarium.getFishes().size() + 1;
        Fish offspring = new Fish(offspringGender + " " + sizeAdd, offspringGender, offspringLifetime, aquarium);
        aquarium.addFish(offspring);
        System.out.println("===== "+fish1+" and "+fish2+" have met together =====");
        System.out.println("-----> New offspring born! Gender: " + offspringGender + " " + sizeAdd+" <-----");
    }
}
