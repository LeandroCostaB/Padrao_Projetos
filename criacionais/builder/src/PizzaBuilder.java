
public class PizzaBuilder {
    private String size;
    private String dough;
    private boolean sauce;
    private boolean cheese;
    private boolean pepperoni;
    private boolean bacon;
    private boolean chicken;
    private boolean corn;
    private boolean onion;
    private boolean tomato;
    private boolean olive;
    private boolean stuffedCrust;
    private boolean chocolate;
    private boolean strawberry;
    private boolean condensedMilk;

    public PizzaBuilder size(String size) {
        this.size = size;
        return this;
    }

    public PizzaBuilder dough(String dough) {
        this.dough = dough;
        return this;
    }

    public PizzaBuilder sauce(boolean sauce) {
        this.sauce = sauce;
        return this;
    }

    public PizzaBuilder cheese(boolean cheese) {
        this.cheese = cheese;
        return this;
    }

    public PizzaBuilder pepperoni(boolean pepperoni) {
        this.pepperoni = pepperoni;
        return this;
    }

    public PizzaBuilder bacon(boolean bacon) {
        this.bacon = bacon;
        return this;
    }

    public PizzaBuilder chicken(boolean chicken) {
        this.chicken = chicken;
        return this;
    }

    public PizzaBuilder corn(boolean corn) {
        this.corn = corn;
        return this;
    }

    public PizzaBuilder onion(boolean onion) {
        this.onion = onion;
        return this;
    }

    public PizzaBuilder tomato(boolean tomato) {
        this.tomato = tomato;
        return this;
    }

    public PizzaBuilder olive(boolean olive) {
        this.olive = olive;
        return this;
    }

    public PizzaBuilder stuffedCrust(boolean stuffedCrust) {
        this.stuffedCrust = stuffedCrust;
        return this;
    }

    public PizzaBuilder chocolate(boolean chocolate) {
        this.chocolate = chocolate;
        return this;
    }

    public PizzaBuilder strawberry(boolean strawberry) {
        this.strawberry = strawberry;
        return this;
    }

    public PizzaBuilder condensedMilk(boolean condensedMilk) {
        this.condensedMilk = condensedMilk;
        return this;
    }

    /**
     * Valida os dados obrigatorios e devolve a pizza pronta.
     */
    public Pizza build() {
        if (size == null || size.isBlank()) {
            throw new IllegalStateException("O tamanho da pizza deve ser informado.");
        }
        if (dough == null || dough.isBlank()) {
            throw new IllegalStateException("O tipo de massa deve ser informado.");
        }
        return new Pizza(this);
    }

    String getSize() {
        return size;
    }

    String getDough() {
        return dough;
    }

    boolean hasSauce() {
        return sauce;
    }

    boolean hasCheese() {
        return cheese;
    }

    boolean hasPepperoni() {
        return pepperoni;
    }

    boolean hasBacon() {
        return bacon;
    }

    boolean hasChicken() {
        return chicken;
    }

    boolean hasCorn() {
        return corn;
    }

    boolean hasOnion() {
        return onion;
    }

    boolean hasTomato() {
        return tomato;
    }

    boolean hasOlive() {
        return olive;
    }

    boolean hasStuffedCrust() {
        return stuffedCrust;
    }

    boolean hasChocolate() {
        return chocolate;
    }

    boolean hasStrawberry() {
        return strawberry;
    }

    boolean hasCondensedMilk() {
        return condensedMilk;
    }
}
