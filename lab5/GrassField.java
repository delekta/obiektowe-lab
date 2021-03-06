package agh.cs.lab5;

import agh.cs.lab2.MoveDirection;
import agh.cs.lab2.Vector2d;
import agh.cs.lab3.Animal;
import agh.cs.lab4.IWorldMap;
import agh.cs.lab4.MapVisualizer;
import agh.cs.lab7.IMapElement;
import agh.cs.lab7.MapBoundary;
import agh.cs.lab7.XComparator;

import java.util.*;

import java.lang.Math;

// GrassField tez implementuje IPositionChangeObserver
public class GrassField extends AbstractWorldMap {
    // debugger() inherited from AbstractWorldMap
    //   animals inherited from AbstractWorldMap
    // run() inherited from AbstractWorldMap
    // toString() inherited from AbstractWorldMap
    // width, height inherited from AbstractWorldMap
    // animalHashMap inherited from AbstractWorldMap
    // grassHashMap inherited from AbstractWorldMap
    // sortedElements inherited from AbstractWorldMap


    int n;
    public List<Grass> grasses;


    public GrassField(int number) {
        // [LAB7] stop using it because of MapBoundary Sorted Elements
        // initialization of limits
        this.height = Integer.MIN_VALUE;
        this.width = Integer.MIN_VALUE;

        this.n = number;
        List<Grass> grasses= new ArrayList<>();
        this.animals = new ArrayList<>();
        this.grasses = new ArrayList<>();
        placeGrasses(number);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        // I check only others Animals because only it can block move

        // Zmiana na uzywanie tylko LinkedHashMap 1 Git
//        Set<Vector2d> setKeys = animalHashMap.keySet();
//        Iterator<Vector2d> iterator = setKeys.iterator();
//
//
//        if(iterator.hasNext()){
//            while (iterator.hasNext()) {
//                Vector2d animalPoz = iterator.next();
//                if (animalPoz.x == position.x && animalPoz.y == position.y) {
//                    return false;
//                }
//            }
//        }
//        Wersja bez LinkedHashMap
        if(!animals.isEmpty()){
            for (Animal animal : animals) {
                if (animal.getPosition().x == position.x && animal.getPosition().y == position.y) {
                    return false;
                }
            }
        }
        return true;
    }


    // we update limits and then use them in toString() function
    private void updateLimits(Vector2d v){
        if(v.x > this.width){
            this.width = v.x;
        }
        if(v.y > this.height){
            this.height = v.y;
        }
    }

    private void placeGrasses(int number){
        Random rand = new Random();
        int x;
        int y;
        for(int i = 0; i < number; i++){
            do{
                x = rand.nextInt((int)Math.sqrt(this.n * 10));
                y = rand.nextInt((int)Math.sqrt(this.n * 10));
//                System.out.println("x:" + x + " | y:" + y);
            }while(isOccupied(new Vector2d(x, y))); // if place is occupied, fine new place

            Grass grass = new Grass(new Vector2d(x, y));

            // added during lab7
            sortedElements.addXSorted(grass);
            sortedElements.addYSorted(grass);

            updateLimits(grass.getPosition());
            this.grasses.add(grass);

            // added during lab6
            grassHashMap.put(grass.getPosition(), grass);

        }
    }

    @Override
    public boolean place(Animal animal) {
        if(!isOccupied(animal.getPosition())) {
            // placing animal


            // Zmiana na uzywanie tylko LinkedHashMap 2
            updateLimits(animal.getPosition());
            animals.add(animal);

            // added during lab7
            sortedElements.addXSorted(animal);
            sortedElements.addYSorted(animal);

            // added during lab6
            animalHashMap.put(animal.getPosition(), animal);
            return true;
        }
        else{
//            System.out.println("XD1");
            Object object = objectAt(animal.getPosition());
            // if grass takes the place, put the animal anyway
            if(object instanceof Grass){
//                System.out.println("XD2");
                updateLimits(animal.getPosition());
                animals.add(animal);

                // added during lab7
                sortedElements.addXSorted(animal);
                sortedElements.addYSorted(animal);

                // added during lab6
                animalHashMap.put(animal.getPosition(), animal);
                return true; // important, because we succeed in placing animal
            }
            else{
                // added during lab 6
                throw new IllegalArgumentException("x:" + animal.getPosition().x + " " + "y: " + animal.getPosition().y + " jest juz zajęte");
//                return false;
            }
        }
    }

    @Override
    public boolean isOccupied(Vector2d position) {
//        if(!animals.isEmpty()){
//            for (Animal animal : animals) {
//                if (animal.getPosition().x == position.x && animal.getPosition().y == position.y) {
//                    return true;
//                }
//            }
//        }
//        if (!grasses.isEmpty()) {
//            for(Grass grass : grasses) {
//                if (grass.getPosition().x == position.x && grass.getPosition().y == position.y) {
//                    return true;
//                }
//            }
//            return false;
//        }
//        return false;

        // added during lab6
        // if the key in hashMap is not found get() method returns null
        return animalHashMap.get(position) != null || grassHashMap.get(position) != null;
    }

    @Override
    public Object objectAt(Vector2d position) {
        // [ fulfilling the higher priority of animals, we return it first]
//        for (Animal animal : animals){
//            if(animal.getPosition().x == position.x && animal.getPosition().y == position.y){
//                return animal;
//            }
//        }
//
//        for (Grass grass : grasses){
//            if(grass.getPosition().x == position.x && grass.getPosition().y == position.y){
//                return grass;
//            }
//        }
//        return null;

        //added during lab6
        Animal animal = animalHashMap.get(position);
        if(animal != null){
            return animal;
        }
        else{
            return grassHashMap.get(position);
        }
    }

    // Added during lab7
    @Override
    public String toString(){
//        System.out.println("heh");
        width = sortedElements.getXSorted().last().getPosition().x;
        height = sortedElements.getYSorted().last().getPosition().y;
        MapVisualizer visual = new MapVisualizer(this);
        return visual.draw(new Vector2d(-1, -1), new Vector2d(width, height));
    }
}
