import java.util.Objects;

public class ExpenseApprovalService {

    private final ExpenseApprover firstApprover;

    public ExpenseApprovalService(ExpenseApprover firstApprover) {
        this.firstApprover = Objects.requireNonNull(firstApprover, "firstApprover must not be null");
    }

    public void approve(ExpenseRequest request) {
        firstApprover.approve(request);
    }
}
