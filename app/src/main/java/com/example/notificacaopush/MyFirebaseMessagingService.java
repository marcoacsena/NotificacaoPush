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

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        //Jamilton apagou os códigos a seguir:
//        super.onMessageReceived(remoteMessage);
//        Log.i("Notificacao", "Emissor de documentos");

        if(remoteMessage.getNotification() != null){

            String titulo = remoteMessage.getNotification().getTitle();
            String corpo = remoteMessage.getNotification().getBody();

            //Log.i("Informacao", "Título: " + titulo +"--" +"Mensagem recebida: " +corpo +".");

            enviarNotificacao(titulo, corpo);
        }
    }

    private void enviarNotificacao(String titulo, String corpo) {

        //Criar canal
        String canalId = getString(R.string.canal_minhas_mensagens);
        Intent intent = new Intent(this, ListaDeNotificacoes.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_ONE_SHOT);

        //Criar mensagem
        NotificationCompat.Builder notificacao = new NotificationCompat.Builder(this, canalId)
                .setContentTitle(titulo)
                .setContentText(corpo)
                .setSmallIcon(R.drawable.ic_baseline_camera_24)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        //Recuperar o NotificationManager
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //Antes de enviar a notificação, é necessário verificar qual é a versão do Android, pois a partir do Oreo é necessário
        //criar um canal para a notificação funcionar
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ){
            NotificationChannel channel = new NotificationChannel(canalId, "canal", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);


        }

        //Enviar notificação
        notificationManager.notify(0, notificacao.build());


    }

    @Override
    public void onNewToken(@NonNull String s) {

        super.onNewToken(s);

        Log.i("Token", "O token recebido é: " +s);
    }
}
