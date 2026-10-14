package notification;

public class Main {

    public static void main(String[] args) {

        Notification alertByEmail = new AlertNotification(new EmailSender());
        alertByEmail.send("Your account was accessed from a new device.");

        Notification reminderBySms = new ReminderNotification(new SmsSender());
        reminderBySms.send("Your appointment starts in one hour.");

        // Desafio adicional: novo canal (WhatsApp) e novo tipo (Warning),
        // combinados sem alterar nenhuma classe existente.
        Notification warningByWhatsApp = new WarningNotification(new WhatsAppSender());
        warningByWhatsApp.send("Suspicious login attempt detected.");

        Notification reminderByWhatsApp = new ReminderNotification(new WhatsAppSender());
        reminderByWhatsApp.send("Don't forget your meeting today.");

        Notification alertBySms = new AlertNotification(new SmsSender());
        alertBySms.send("Your password was changed.");
    }
}
