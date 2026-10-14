public class Main {
    public static void main(String[] args) {
        Pizza calabresa = new PizzaBuilder()
                .size("Grande")
                .dough("Tradicional")
                .sauce(true)
                .cheese(true)
                .pepperoni(true)
                .onion(true)
                .olive(true)
                .build();

        Pizza portuguesa = new PizzaBuilder()
                .size("Media")
                .dough("Fina")
                .sauce(true)
                .cheese(true)
                .bacon(true)
                .corn(true)
                .onion(true)
                .tomato(true)
                .olive(true)
                .build();

        Pizza personalizada = new PizzaBuilder()
                .size("Grande")
                .dough("Integral")
                .sauce(true)
                .cheese(true)
                .chicken(true)
                .bacon(true)
                .corn(true)
                .stuffedCrust(true)
                .build();

        // Desafio adicional: os ingredientes salgados permanecem opcionais.
        Pizza doce = new PizzaBuilder()
                .size("Pequena")
                .dough("Tradicional")
                .chocolate(true)
                .strawberry(true)
                .condensedMilk(true)
                .stuffedCrust(true)
                .build();

        System.out.println("Calabresa: " + calabresa);
        System.out.println("Portuguesa: " + portuguesa);
        System.out.println("Personalizada: " + personalizada);
        System.out.println("Doce: " + doce);
    }
}
