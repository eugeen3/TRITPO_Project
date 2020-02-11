package eugeen3.keepinfit.entities;

import android.content.Context;

import java.util.List;

import eugeen3.keepinfit.database.FileList;

public class Meal {
    private String name;
    private int amountOfProducts;
    private float proteins;
    private float carbohydrates;
    private float fats;
    private int kcals;

    public Meal(String name, int amountOfProducts, float prots, float fats, float carbs, int kcals) {
        this.name = name;
        this.amountOfProducts = amountOfProducts;
        this.proteins = prots;
        this.fats = fats;
        this.carbohydrates = carbs;
        this.kcals = kcals;
    }
/*
    public Meal(String name) {
        this.name = name;
        this.amountOfProducts = 0;
        this.proteins = 0;
        this.fats = 0;
        this.carbohydrates = 0;
        this.kcals = 0;
    }
 */
    public void recieveData(Context context) {
        FileList fileList = new FileList<FoodItem>(context.getFilesDir() +
                "/" + name + ".txt");

        List<FoodItem> foodItemList = null;
        try {
            foodItemList = fileList.loadList();
        }
        catch (Exception e) { }
        if (foodItemList != null) {
            for (FoodItem foodItem : foodItemList) {
                amountOfProducts++;
                proteins += foodItem.getProteins();
                fats += foodItem.getFats();
                carbohydrates += foodItem.getCarbohydrates();
                kcals += foodItem.getKcals();
            }
        }
    }

    public String getName() {
        return name;
    }

    public int getAmountOfProducts() {
        return amountOfProducts;
    }

    public float getProteins() {
        return proteins;
    }

    public float getCarbohydrates() {
        return carbohydrates;
    }

    public float getFats() {
        return fats;
    }

    public int getKcals() {
        return kcals;
    }

    @Override
    public String toString() {
        return new StringBuffer().append(name + " ").append(amountOfProducts + " ").
                append(proteins + " ").append(fats + " ").
                append(carbohydrates + " ").append(kcals).toString();
    }
}
