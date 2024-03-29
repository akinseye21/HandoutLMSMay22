package com.example.handoutlms.ChatNotification;

//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.messaging.FirebaseMessagingService;
//import com.google.firebase.messaging.RemoteMessage;

//public class MyFirebaseMessaging extends FirebaseMessagingService {
//
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);
//
//        String sented = remoteMessage.getData().get("sented");
//
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        if(firebaseUser != null && sented.equals(firebaseUser.getUid())){
//
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//                sendOreoNotification(remoteMessage);
//            } else {
//                sendNotification(remoteMessage);
//            }
//        }
//    }
//
//    private void sendOreoNotification(RemoteMessage remoteMessage) {
//        String user = remoteMessage.getData().get("user");
//        String icon = remoteMessage.getData().get("icon");
//        String title = remoteMessage.getData().get("title");
//        String body = remoteMessage.getData().get("body");
//
//        RemoteMessage.Notification notification = remoteMessage.getNotification();
//        int j = Integer.parseInt(user.replaceAll("[\\D]", ""));
//        Intent intent = new Intent(this, ChatMessagingPage.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("userid", user);
//        intent.putExtras(bundle);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT);
//        Uri defaultsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//        OreoNotification oreoNotification = new OreoNotification(this);
//        Notification.Builder builder = oreoNotification.getOreoNotification(title, body, pendingIntent, defaultsound, icon);
//
//        int i = 0;
//        if (j>0){
//            i = j;
//        }
//
//        oreoNotification.getManager().notify(i, builder.build());
//
//    }
//
//    private void sendNotification(RemoteMessage remoteMessage) {
//        String user = remoteMessage.getData().get("user");
//        String icon = remoteMessage.getData().get("icon");
//        String title = remoteMessage.getData().get("title");
//        String body = remoteMessage.getData().get("body");
//
//        RemoteMessage.Notification notification = remoteMessage.getNotification();
//        int j = Integer.parseInt(user.replaceAll("[\\D]", ""));
//        Intent intent = new Intent(this, ChatMessagingPage.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("userid", user);
//        intent.putExtras(bundle);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT);
//
//        Uri defaultsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder builder =  new NotificationCompat.Builder(this)
//                .setSmallIcon(Integer.parseInt(icon))
//                .setContentTitle(title)
//                .setContentText(body)
//                .setSound(defaultsound)
//                .setContentIntent(pendingIntent);
//        NotificationManager noti = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        int i = 0;
//        if (j>0){
//            i = j;
//        }
//
//        noti.notify(i, builder.build());
//    }
//}
