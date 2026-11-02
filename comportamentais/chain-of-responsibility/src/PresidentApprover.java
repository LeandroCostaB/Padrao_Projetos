public class PresidentApprover extends ExpenseApprover {

    @Override
    protected int getLimitInCents() {
        return 5_000_000;
    }

    @Override
    protected String getTitle() {
        return "President";
    }
}
