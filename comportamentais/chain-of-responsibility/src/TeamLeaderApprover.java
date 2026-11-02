public class TeamLeaderApprover extends ExpenseApprover {

    @Override
    protected int getLimitInCents() {
        return 50_000;
    }

    @Override
    protected String getTitle() {
        return "Team leader";
    }
}
