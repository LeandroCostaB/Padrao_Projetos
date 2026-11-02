public class ManagerApprover extends ExpenseApprover {

    @Override
    protected int getLimitInCents() {
        return 200_000;
    }

    @Override
    protected String getTitle() {
        return "Manager";
    }
}
