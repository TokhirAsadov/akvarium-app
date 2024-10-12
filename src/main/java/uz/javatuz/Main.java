package uz.javatuz;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Random random = new Random();
        // Tasodifiy ravishda akvariumning o'lchami aniqlanadi
        int width = random.nextInt(20) + 10;  // Kenglik 10 dan 30 gacha
        int length = random.nextInt(20) + 10; // Uzunlik 10 dan 30 gacha
        Aquarium aquarium = new Aquarium(width, length);

        // Tasodifiy ravishda erkak va urg'ochi baliqlar soni aniqlanadi
        int numMales = random.nextInt(10) + 1;
        int numFemales = random.nextInt(10) + 1;

        // Erkak baliqlarni qo'shish
        for (int i = 0; i < numMales; i++) {
            int lifetime = random.nextInt(10) + 5; // Baliqning yashash muddati
            Fish maleFish = new Fish("Male "+(i+1),"Male", lifetime, aquarium);
            aquarium.addFish(maleFish);
        }

        // Urg'ochi baliqlarni qo'shish
        for (int i = 0; i < numFemales; i++) {
            int lifetime = random.nextInt(10) + 5; // Baliqning yashash muddati
            Fish femaleFish = new Fish("Female "+(i+1),"Female", lifetime, aquarium);
            aquarium.addFish(femaleFish);
        }
    }
}

