package com.example.notificacaopush;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService{

    @Override
    public void onMessageReceived(@NonNull RemoteMessage notificacao) {

        if (notificacao.getNotification() != null){

            //Pega o título e corpo que veem do Firebase
            String titulo = notificacao.getNotification().getTitle();
            String corpo = notificacao.getNotification().getBody();

            Log.i("Notificacao", "Mensagem Recebida" +titulo + "--" +" corpo:" +corpo);

            //Método que envia a notificação. Essa notificação
            //aparece mesmo que o usuário esteja com o app aberto!

            enviarNotificacao(titulo, corpo);
        }

    }

    private void enviarNotificacao(String titulo, String corpo) {

        //Configura notificação: define o canal
        String canal = getString(R.string.default_notification_channel_id);

        //Para abrir uma activity a partir de uma notificação
        Intent intent = new Intent(this, NotificacoesActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        //Pata criar notificação
        NotificationCompat.Builder notificacao = new NotificationCompat.Builder(
                this, canal)
                .setContentTitle(titulo)
                .setContentText(corpo)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setAutoCancel(true)
                .setNumber(10)
                .setContentIntent(pendingIntent);

        //Para recuperar notificação
        NotificationManager notificationManager = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE
        );

        //Verifica versão do Android pra fins de compatibilidade. A partir do Oreo é preciso crirar um canal de notificação
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            NotificationChannel channel = new NotificationChannel(
                    canal, "canal", NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }

        //Para enviar a notificação para a barra de status do celular
        notificationManager.notify(0,notificacao.build());

    }

    @Override
    public void onNewToken(@NonNull String s) {

        super.onNewToken(s);
        Log.i("onNewToken", "onNewToken: " +s);
        //cUkDpw2ES3es2FyKVQfF4c:APA91bGVfNIjkc_REn4Upfq5BI6GgE16bIOHQyFYdf4AHnglIT38iCMGZO8LG-
        // rhpE9uta0hXWJQW5AFDMlRg5IHdGnURc5D__823eCh9CGtz8nqRpH9YGmndYPdxj8DcAH6igSS8zWY
    }
}
