
public final class Pizza {
    private final String tamanho;
    private final String massa;
    private final boolean molho;
    private final boolean queijo;
    private final boolean pepperoni;
    private final boolean bacon;
    private final boolean frango;
    private final boolean milho;
    private final boolean cebola;
    private final boolean tomate;
    private final boolean azeitona;
    private final boolean bordaRecheada;
    private final boolean chocolate;
    private final boolean morango;
    private final boolean leiteCondensado;

    Pizza(PizzaBuilder builder) {
        this.tamanho = builder.getSize();
        this.massa = builder.getDough();
        this.molho = builder.hasSauce();
        this.queijo = builder.hasCheese();
        this.pepperoni = builder.hasPepperoni();
        this.bacon = builder.hasBacon();
        this.frango = builder.hasChicken();
        this.milho = builder.hasCorn();
        this.cebola = builder.hasOnion();
        this.tomate = builder.hasTomato();
        this.azeitona = builder.hasOlive();
        this.bordaRecheada = builder.hasStuffedCrust();
        this.chocolate = builder.hasChocolate();
        this.morango = builder.hasStrawberry();
        this.leiteCondensado = builder.hasCondensedMilk();
    }

    public String getTamanho() {
        return tamanho;
    }

    public String getMassa() {
        return massa;
    }

    public boolean temMolho() {
        return molho;
    }

    public boolean temQueijo() {
        return queijo;
    }

    public boolean temPepperoni() {
        return pepperoni;
    }

    public boolean temBacon() {
        return bacon;
    }

    public boolean temFrango() {
        return frango;
    }

    public boolean temMilho() {
        return milho;
    }

    public boolean temCebola() {
        return cebola;
    }

    public boolean temTomate() {
        return tomate;
    }

    public boolean temAzeitona() {
        return azeitona;
    }

    public boolean temBordaRecheada() {
        return bordaRecheada;
    }

    public boolean temChocolate() {
        return chocolate;
    }

    public boolean temMorango() {
        return morango;
    }

    public boolean temLeiteCondensado() {
        return leiteCondensado;
    }

    @Override
    public String toString() {
        StringBuilder ingredientes = new StringBuilder();
        adicionar(ingredientes, molho, "molho");
        adicionar(ingredientes, queijo, "queijo");
        adicionar(ingredientes, pepperoni, "pepperoni");
        adicionar(ingredientes, bacon, "bacon");
        adicionar(ingredientes, frango, "frango");
        adicionar(ingredientes, milho, "milho");
        adicionar(ingredientes, cebola, "cebola");
        adicionar(ingredientes, tomate, "tomate");
        adicionar(ingredientes, azeitona, "azeitona");
        adicionar(ingredientes, bordaRecheada, "borda recheada");
        adicionar(ingredientes, chocolate, "chocolate");
        adicionar(ingredientes, morango, "morango");
        adicionar(ingredientes, leiteCondensado, "leite condensado");

        String lista = ingredientes.length() == 0
                ? "sem ingredientes adicionais"
                : ingredientes.toString();

        return "Pizza{" +
                "tamanho='" + tamanho + '\'' +
                ", massa='" + massa + '\'' +
                ", ingredientes=[" + lista + "]" +
                '}';
    }

    private static void adicionar(StringBuilder lista, boolean presente, String ingrediente) {
        if (!presente) {
            return;
        }
        if (lista.length() > 0) {
            lista.append(", ");
        }
        lista.append(ingrediente);
    }
}
