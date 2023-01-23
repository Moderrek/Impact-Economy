package pl.moderr.impact.economy.container;

public class UserIsLoadedException extends Exception{
    public UserIsLoadedException(){
        super("User is currently loaded!");
    }
}
