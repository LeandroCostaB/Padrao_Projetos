public class DirectorApprover extends ExpenseApprover {

    @Override
    protected int getLimitInCents() {
        return 1_000_000;
    }

    @Override
    protected String getTitle() {
        return "Director";
    }
}
