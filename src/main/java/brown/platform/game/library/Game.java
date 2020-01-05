package brown.platform.game.library;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import brown.auction.marketstate.IMarketState;
import brown.communication.messages.IActionMessage;
import brown.communication.messages.IActionRequestMessage;
import brown.communication.messages.library.ActionRequestMessage;
import brown.platform.accounting.IAccountUpdate;
import brown.platform.game.IFlexibleRules;
import brown.platform.game.IGame;


public class Game implements IGame {

  private final Integer ID;
  private final IFlexibleRules RULES;
  private final IMarketState STATE;
  private final IMarketState PUBLICSTATE;
  private final Set<Integer> AGENTS; 

  private List<IActionMessage> bids;

  // TODO: make the market remember its history in a memory-efficient way. 
  // make the state a remembering thing. 
  // at some point need to add the bids into the market state. 
  public Game(Integer ID, IFlexibleRules rules, IMarketState state,
      IMarketState publicState, Set<Integer> agents) {
    this.ID = ID;
    this.RULES = rules;
    this.STATE = state;
    this.PUBLICSTATE = publicState;
    this.AGENTS = agents; 
    this.bids = new LinkedList<IActionMessage>();
  }

  @Override
  public Integer getMarketID() {
    return this.ID;
  }


  public IActionRequestMessage constructTradeRequest(Integer agentID) {
    this.RULES.getQRule().makeTradeRequest(ID, STATE, bids, agentID);
    ActionRequestMessage request = this.STATE.getTRequest();
    return request;
  }

  public boolean processBid(IActionMessage bid) {
    this.RULES.getActRule().isAcceptable(this.STATE, bid, this.bids);
    boolean acceptable = this.STATE.getAcceptable();
    if (acceptable) {
      this.bids.add(bid);
    } 
    return acceptable;
  }

  public List<IAccountUpdate> constructAccountUpdates() {
    // ok... if the termination condition is that there are no bids, the allocation rule is not gonna do anything
    // because there are no bids. 
    // so what will do the allocation? 
    // the allocation rule has to use a history. 
    
    this.RULES.getARule().setAllocation(this.STATE, this.bids);
    return this.STATE.getUtilities();
  }

  
  @Override
  public void clearBidCache() {
    this.bids.clear();
  }

  @Override
  public void tick() {
    this.STATE.tick();
  }
  
  @Override
  public Integer getTimestep() {
    return this.STATE.getTicks();
  }

  @Override
  public boolean isOpen() {
    this.RULES.getTerminationCondition().checkTerminated(this.STATE, this.bids);
    return this.STATE.isOpen();
  }

  @Override
  public IMarketState getPublicState() {
    return this.PUBLICSTATE;
  }

  @Override
  public void updateOuterInformation() {
    this.RULES.getIRPolicy().updatePublicState(this.STATE, this.PUBLICSTATE);
  }

  @Override
  public void updateInnerInformation() {
    this.RULES.getInnerIRPolicy().updatePublicState(this.STATE,
        this.PUBLICSTATE);
  }
  
  @Override
  public void updateTradeHistory() {
    this.STATE.addToTradeHistory(this.bids);
  }

}
