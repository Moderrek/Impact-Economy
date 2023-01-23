package pl.moderr.impact.economy.container;

public class UnknownUserException extends Exception{
    public UnknownUserException(){
        super("User with this UUID does not exist.");
    }
}
