package pl.moderr.impact.economy.data;

import pl.moderr.impact.economy.ImpactEconomy;

import java.util.UUID;

public class User {

    private final UUID _uuid;
    private long _amount;

    /**
     * @param uuid Minecraft player owner id
     * @param amount Value
     */
    public User(UUID uuid, long amount){
        _uuid = uuid;
        _amount = amount;
    }
    /**
     * @param amount Amount to set
     */
    public void set(long amount){
        _amount = amount;
    }
    /**
     * @param amount Amount to add
     */
    public void add(long amount){
        _amount += amount;
    }
    /**
     * @param amount Amount to subtract
     */
    public void subtract(long amount){
        _amount -= amount;
    }
    /**
     * @param minAmount Minimal amount
     * @return true/false if player has `minAmount` or more
     */
    public boolean has(long minAmount){
        return _amount >= minAmount;
    }
    /**
     * @param amount Amount which is more than user have
     * @return How much amount is missing to `amount`
     */
    public long howMuchTo(long amount){
        return _amount % amount;
    }
    /**
     * @return User amount
     */
    public long get(){
        return _amount;
    }
    /**
     * @return [SUFFIX][BALANCE/DECIMAL PRECISION][PREFIX] -> ''10050'$' -> <b>100.50$</b>
     */
    public String formatBalance(){
        return ImpactEconomy.getInstance().moneyPrefix + ((double)_amount/(double)ImpactEconomy.getInstance().decimalPrecision) + ImpactEconomy.getInstance().moneySuffix;
    }
    /**
     * @return Minecraft player owner id
     */
    public UUID getUUID() {
        return _uuid;
    }
}
