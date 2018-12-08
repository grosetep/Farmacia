package com.estrategiamovilmx.sales.farmacia.tools;


import android.content.Context;

import com.estrategiamovilmx.sales.farmacia.items.ConfigItem;
import com.estrategiamovilmx.sales.farmacia.items.OrderItem;
import com.estrategiamovilmx.sales.farmacia.items.UserItem;
import com.estrategiamovilmx.sales.farmacia.model.PublicationCardViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by administrator on 10/07/2017.
 */
public class GeneralFunctions {
    public static ArrayList<PublicationCardViewModel> FilterPublications(ArrayList<PublicationCardViewModel> publications, ArrayList<PublicationCardViewModel> new_publications){
        Iterator<PublicationCardViewModel> iter = new_publications.iterator();
        while (iter.hasNext()){
            PublicationCardViewModel p = iter.next();
            if (publications.contains(p)){ iter.remove();}
        }
        return new_publications;
    }

    public static UserItem getCurrentUser(Context context){
        Gson gson = new Gson();
        UserItem user = null;
        String json_user = ApplicationPreferences.getLocalStringPreference(context, Constants.user_object);
        if (json_user!=null || !json_user.isEmpty()){
            user = gson.fromJson(json_user,UserItem.class);
        }
        return user;
    }
    public static String getTokenUser(Context context){
        Gson gson = new Gson();

        String json_token = ApplicationPreferences.getLocalStringPreference(context, Constants.firebase_token);

        return json_token;
    }
    public static ConfigItem getConfiguration(Context context){
        Gson gson = new Gson();
        ConfigItem config = null;
        String json_config = ApplicationPreferences.getLocalStringPreference(context, Constants.configuration_object);
        if (json_config!=null || !json_config.isEmpty()){
            config = gson.fromJson(json_config,ConfigItem.class);
        }
        return config;
    }
    public static List<OrderItem> Filter(ArrayList<OrderItem> elements, List<OrderItem> new_elements){
        Iterator<OrderItem> iter = new_elements.iterator();
        while (iter.hasNext()){
            OrderItem p = iter.next();
            if (elements.contains(p)){ iter.remove();}
        }
        return new_elements;
    }

}
