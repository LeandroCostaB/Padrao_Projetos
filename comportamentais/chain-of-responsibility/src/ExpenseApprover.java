public abstract class ExpenseApprover {

    private ExpenseApprover next;

    public void setNext(ExpenseApprover next) {
        this.next = next;
    }

    public final void approve(ExpenseRequest request) {
        if (request.getAmountInCents() <= getLimitInCents()) {
            System.out.println(getTitle() + " approved: " + request.getDescription());
            return;
        }

        if (next == null) {
            System.out.println("No approver available: " + request.getDescription());
            return;
        }

        next.approve(request);
    }

    protected abstract int getLimitInCents();

    protected abstract String getTitle();
}
