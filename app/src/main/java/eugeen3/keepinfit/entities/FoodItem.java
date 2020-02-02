package eugeen3.keepinfit.entities;

public class FoodItem {
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
}
