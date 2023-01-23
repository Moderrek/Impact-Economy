package pl.moderr.impact.economy.container;

import pl.moderr.impact.economy.data.User;

public interface LoadUserCallback {
    void onLoad(User user);
    void onLoadAndCreate(User user);
}
