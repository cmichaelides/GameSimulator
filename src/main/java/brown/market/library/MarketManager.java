package brown.market.library;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import brown.accounting.library.Ledger;
import brown.logging.Logging;
import brown.market.IMarketManager;
import brown.market.marketstate.library.MarketState;
import brown.market.preset.AbsMarketPreset;
import brown.server.library.SimulMarkets;
import brown.tradeable.ITradeable;

/**
 * Market manager stores and handles multiple markets 
 * @author lcamery
 *
 */

//TODO: unique IDs for every market ever
//TODO: make sure that if a bid is being sent to a market, that markets exists in the manager. 

public class MarketManager implements IMarketManager {
  // stores all ledgers in a simulation
	private List<Map<Market, Ledger>> ledgers;
	// stores all markets in a simulation
	public List<Map<Integer, Market>> markets;
	public PrevStateInfo information;
	public Integer index; 
	private Integer idCount; 
	

	public MarketManager() {
		this.ledgers = new LinkedList<Map<Market, Ledger>>();
		this.markets = new LinkedList<Map<Integer, Market>>();	
		this.information = new BlankStateInfo();
		this.index = -1; 
		this.idCount = 0; 
	}

  public void addSimulMarket(SimulMarkets s, List<ITradeable> tradeables, List<Integer> agents) {
    this.index++;
	  this.ledgers.add(new ConcurrentHashMap<Market, Ledger>());
	  this.markets.add(new ConcurrentHashMap<Integer, Market>());
	  for (AbsMarketPreset preset : s.getMarkets()) {
	    this.open(preset, idCount, tradeables, agents);
	    idCount++;
	  } 
	}
	  
	/**
	 * Opens a market
	 * @param market
	 * @return
	 */
	public boolean open(AbsMarketPreset rules, Integer marketID, List<ITradeable> tradeables, List<Integer> agents) {
	  Market market = new Market(rules.copy(), new MarketState(marketID,tradeables,this.information));
	   if (ledgers.get(index).containsKey(market)) {
	      return false;
	   }
	   market.setGroupings(agents);
	   this.ledgers.get(index).put(market, new Ledger(market.getID()));
	   this.markets.get(index).put(market.getID(), market);
	   return true;
	}
	
	 /**
   * Closes a market 
   * @param server
   * @param ID
   * @param closingState
   */
  public void close(Integer ID) {
    Market toClose = this.markets.get(index).get(ID);
    toClose.close();
    this.markets.get(index).put(ID, toClose);
  }

	/**
	 * Gets the ledger for this market ID
	 * @param ID
	 * @return
	 */
	public Ledger getLedger(Integer ID) {
		return ledgers.get(index).get(markets.get(index).get(ID));
	}

	/**
	 * Gets the market for this ID
	 * @param ID
	 * @return
	 */
	public Market getMarket(Integer ID) {
	    return markets.get(index).get(ID);	  
	}

	
	public boolean MarketOpen(Integer ID) {
	  if (index == -1) return false; 
	  if (markets.get(index).containsKey(ID)) {	
	    return markets.get(index).get(ID).isOpen();
	  }
	  return false; 
	 }
	  
	
	/**
	 * Gets all of the auctions
	 * @return
	 */
	public Collection<Market> getAuctions() {
		return this.markets.get(index).values();
	}

	// update information from a market
  public void update(Integer marketID) {
   this.information.combine(this.markets.get(index).get(marketID).constructSummaryState());
  }

  public boolean anyMarketsOpen() {
    boolean toReturn = false;
    for (Market m : this.getAuctions()) {
      if (!m.isOverOuter()) {
        toReturn = true;
        break;
      }
    }
    return toReturn;    
  }

  public void reset() {
    this.index = -1;
    this.ledgers = new LinkedList<Map<Market, Ledger>>();
    this.markets = new LinkedList<Map<Integer, Market>>();  
    this.information = new BlankStateInfo();
  }

  public void initializeInfo(PrevStateInfo info) {
    this.information = info;
  }

  public void updateAllInfo() {
    for (Market market: this.markets.get(index).values()) {
      this.information.combine(market.constructSummaryState());
    }
  }
}
