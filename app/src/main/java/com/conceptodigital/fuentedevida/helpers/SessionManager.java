package com.conceptodigital.fuentedevida.helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "userSession";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String KEY_USER_ID     = "id";
    private static final String KEY_USER_NAME   = "name";
    private static final String KEY_USER_EMAIL  = "email";
    private static final String KEY_TOKEN_SESSION   =   "token";
    private static final String KEY_USER_ACC    = "acceso";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn, String id, String name, String email,  String token, String acceso) {
        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        editor.putString(KEY_USER_ID, id);
        editor.putString(KEY_USER_NAME, name);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_TOKEN_SESSION, token); //guardar el token de sesión.
        editor.putString(KEY_USER_ACC, acceso);
        editor.commit();
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public String getUserEmail(){
        return pref.getString(KEY_USER_EMAIL, null);
    }
    public String getUserToken(){
        return pref.getString(KEY_TOKEN_SESSION, null);
    }
    public String getUserAcceso(){
        return pref.getString(KEY_USER_ACC, null);
    }
}
