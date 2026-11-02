public class Main {

    public static void main(String[] args) {

        TeamLeaderApprover teamLeader = new TeamLeaderApprover();
        ManagerApprover manager = new ManagerApprover();
        DirectorApprover director = new DirectorApprover();

        teamLeader.setNext(manager);
        manager.setNext(director);
        director.setNext(null);

        ExpenseApprovalService service = new ExpenseApprovalService(teamLeader);

        System.out.println("-- Chain: team leader -> manager -> director --");
        service.approve(new ExpenseRequest("Office supplies (R$ 300.00)", 30_000));
        service.approve(new ExpenseRequest("Training (R$ 1,500.00)", 150_000));
        service.approve(new ExpenseRequest("Equipment (R$ 8,000.00)", 800_000));
        service.approve(new ExpenseRequest("Office renovation (R$ 15,000.00)", 1_500_000));

        System.out.println();
        System.out.println("-- Amounts exactly on each limit --");
        service.approve(new ExpenseRequest("Exactly R$ 500.00", 50_000));
        service.approve(new ExpenseRequest("Exactly R$ 2,000.00", 200_000));
        service.approve(new ExpenseRequest("Exactly R$ 10,000.00", 1_000_000));

        System.out.println();
        System.out.println("-- The president joins the end of the same chain --");
        director.setNext(new PresidentApprover());

        service.approve(new ExpenseRequest("Office renovation (R$ 15,000.00)", 1_500_000));
        service.approve(new ExpenseRequest("Exactly R$ 50,000.00", 5_000_000));
        service.approve(new ExpenseRequest("One cent above R$ 50,000.00", 5_000_001));

        System.out.println();
        System.out.println("-- Smaller amounts keep their original approvers --");
        service.approve(new ExpenseRequest("Office supplies (R$ 300.00)", 30_000));
        service.approve(new ExpenseRequest("Training (R$ 1,500.00)", 150_000));
        service.approve(new ExpenseRequest("Equipment (R$ 8,000.00)", 800_000));

        System.out.println();
        System.out.println("-- Request validations --");

        try {
            new ExpenseRequest(" ", 10_000);
        } catch (IllegalArgumentException exception) {
            System.out.println("Refused: " + exception.getMessage());
        }

        try {
            new ExpenseRequest("Invalid amount", 0);
        } catch (IllegalArgumentException exception) {
            System.out.println("Refused: " + exception.getMessage());
        }
    }
}
