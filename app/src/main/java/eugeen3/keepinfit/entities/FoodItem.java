package eugeen3.keepinfit.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class FoodItem implements Serializable {
    private String name;
    private int mass;
    private float proteins;
    private float carbohydrates;
    private float fats;
    private int kcals;

    public FoodItem(String name, int mass, float prots, float carbs, float fats, int kcals) {
        this.name = name;
        this.mass = mass;
        this.proteins = prots;
        this.carbohydrates = carbs;
        this.fats = fats;
        this.kcals = kcals;
    }

    public static float portionNV(int mass, float num) {
        return new BigDecimal(mass * num / 100).
                setScale(2, RoundingMode.UP).floatValue();
    }

    public static int portionNV(int mass, int num) {
        return Math.round(mass * num / 100);
    }

    public String getName() {
        return name;
    }

    public int getMass() {
        return mass;
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
        return new StringBuffer().append(name + " ").append(mass + " ").
                append(proteins + " ").append(fats + " ").
                append(carbohydrates + " ").append(kcals).toString();
    }
}
