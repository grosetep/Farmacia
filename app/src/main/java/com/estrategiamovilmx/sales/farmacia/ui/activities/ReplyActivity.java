package com.estrategiamovilmx.sales.farmacia.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.estrategiamovilmx.sales.farmacia.items.UserItem;
import com.estrategiamovilmx.sales.farmacia.notifications.MyFirebaseMessagingService;
import com.estrategiamovilmx.sales.farmacia.tools.Constants;

public class ReplyActivity extends AppCompatActivity {
    public static final String flow_notification = "notification";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static Intent getReplyMessageIntent(Context context, int notifyId, int messageId, UserItem user) {
        Log.d(flow_notification,"getReplyMessageIntent..."+user.toString());
        Intent intent = null;
        if (user.getProfile().equals(Constants.profile_deliver_man)){
            intent = new Intent(context, OrdersDeliverActivity.class);
        }else{
            intent = new Intent(context, OrdersActivity.class);
            intent.putExtra(Constants.flow,flow_notification);
        }
        intent.setAction(MyFirebaseMessagingService.REPLY_ACTION);
        Log.d(flow_notification,"getReplyMessageIntent...ok");
        return intent;
    }
}
