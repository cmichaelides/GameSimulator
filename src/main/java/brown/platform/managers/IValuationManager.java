package brown.platform.managers;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import brown.auction.value.distribution.IValuationDistribution;
import brown.auction.value.valuation.IValuation;
import brown.communication.messages.IValuationMessage;
import brown.platform.tradeable.ITradeable;

public interface IValuationManager {

    public void createValuation(Constructor<?> distCons, Map<Constructor<?>, List<Double>> generatorCons, 
        Map<String, List<ITradeable>> tradeables) throws InstantiationException, IllegalAccessException,
    IllegalArgumentException, InvocationTargetException;
    
    public void addAgentValuation(Integer agentID, List<String> tradeableNames, IValuation valuation); 
    
    public IValuation getAgentValuation(Integer agentID); 
    
    public IValuationDistribution getDistribution();

    public void lock();
    
    public Map<Integer, IValuationMessage> constructValuationMessages(); 
    
    public void reset(); 
}

