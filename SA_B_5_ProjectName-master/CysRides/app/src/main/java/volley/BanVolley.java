package volley;

import android.content.Context;

import domain.Ban;

interface BanVolley {
    /**
     * Creates new user ban
     * @param context of app
     * @param ban - user ban
     */
    void createBan(Context context, Ban ban);
}
