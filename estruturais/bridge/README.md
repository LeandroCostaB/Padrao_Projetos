# bridge

Atividade 009 - Padrão Bridge: sistema de notificações refatorado para separar
o tipo de notificação (abstração) do canal de envio (implementação).

Código-fonte em [src/main/java/notification/](src/main/java/notification).

## Questões para reflexão

**a) Qual problema existente no código inicial foi resolvido com o Bridge?**
A explosão combinatória de classes: cada par (tipo de notificação × canal) exigia
uma classe própria (`EmailAlertNotification`, `SmsReminderNotification`, etc.),
duplicando a lógica de envio e multiplicando o número de classes a cada novo
tipo ou canal.

**b) O que representa a abstração na sua implementação?**
A classe `Notification` (e suas subclasses `AlertNotification`,
`ReminderNotification`, `WarningNotification`) — representa "o que" é enviado
e como a mensagem é formatada, sem saber como o envio é feito de fato.

**c) O que representa a implementação na sua solução?**
A interface `NotificationSender` e suas implementações (`EmailSender`,
`SmsSender`, `WhatsAppSender`) — representam "como" a mensagem chega ao
destinatário, isolando o detalhe técnico do canal.

**d) Qual é a diferença entre utilizar Bridge e criar uma classe para cada
combinação possível?**
Sem Bridge, o número de classes cresce como tipo × canal (multiplicativo).
Com Bridge, cresce como tipo + canal (aditivo): para adicionar um novo canal
ou tipo, basta uma classe nova, que já funciona combinada com tudo que já
existe, sem tocar no código existente.

**e) Em quais situações o padrão Bridge é mais indicado?**
Quando existem duas (ou mais) dimensões de variação independentes que podem
evoluir separadamente — como tipo de notificação vs. canal de envio, driver
de banco vs. SGBD, ou renderer vs. plataforma gráfica — e você quer evitar
que uma nova opção em uma dimensão force a criação de classes para todas as
combinações com a outra dimensão.