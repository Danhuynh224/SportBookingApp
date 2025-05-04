package com.example.pjbookingsport.sharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.pjbookingsport.API.LocalDateAdapter;
import com.example.pjbookingsport.model.Account;
import com.example.pjbookingsport.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;

public class SharedPreferencesHelper {

    private static final String PREF_NAME = "user_preferences";
    private static final String KEY_USER = "user";

    // Lưu đối tượng User vào SharedPreferences
    public static void saveUser(Context context, User user) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();

        String userJson = gson.toJson(user); // Chuyển đổi đối tượng User thành JSON

        editor.putString(KEY_USER, userJson);
        editor.apply(); // Lưu thay đổi
    }

    // Lấy đối tượng User từ SharedPreferences
    public static User getUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString(KEY_USER, null);

        if (userJson != null) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .create();
            return gson.fromJson(userJson, User.class); // Chuyển đổi JSON thành đối tượng User
        }

        return null; // Nếu không có User trong SharedPreferences
    }
    private static final String KEY_ACCOUNT = "account";

    // Lưu Account vào SharedPreferences
    public static void saveAccount(Context context, Account account) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String accountJson = gson.toJson(account);

        editor.putString(KEY_ACCOUNT, accountJson);
        editor.apply();
    }

    // Lấy Account từ SharedPreferences
    public static Account getAccount(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String accountJson = sharedPreferences.getString(KEY_ACCOUNT, null);

        if (accountJson != null) {
            Gson gson = new Gson();
            return gson.fromJson(accountJson, Account.class);
        }

        return null;
    }

    // Xóa User khỏi SharedPreferences
    public static void clearUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_USER);
        editor.apply(); // hoặc editor.commit();
    }

    // Xóa Account khỏi SharedPreferences
    public static void clearAccount(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_ACCOUNT);
        editor.apply(); // hoặc editor.commit();
    }
    public static boolean checkUserIsSave(Context context){
        User saveUser = getUser(context);
        if(saveUser==null){
            return false;
        }
        return true;
    }

}

