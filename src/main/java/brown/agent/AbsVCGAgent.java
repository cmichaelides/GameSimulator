package brown.agent; 

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import brown.exceptions.AgentCreationException;
import brown.logging.Logging;
import brown.messages.library.PrivateInformationMessage;
import brown.messages.library.ValuationInformationMessage;
import brown.setup.ISetup;
import brown.tradeable.ITradeable;
import brown.tradeable.library.ComplexTradeable;
import brown.value.distribution.IValuationDistribution;
import brown.value.distribution.library.SpecValDistV2;
import brown.value.valuation.IValuation;
import brown.value.valuation.library.SpecValValuation;
import brown.value.valuation.library.XORValuation;

public abstract class AbsVCGAgent extends AbsAuctionAgent {

  protected List<ITradeable> tradeables;
  protected SpecValValuation valuation; 
  protected SpecValDistV2 vDistribution; 
  protected Map<ComplexTradeable, Double> XORBids;
  
  
  public AbsVCGAgent(String host, int port, ISetup gameSetup)
      throws AgentCreationException {
    super(host, port, gameSetup);
    this.tradeables = new LinkedList<ITradeable>(); 
  }

  
  // stores agent tradeables, valuation and valuation distribution.
  @Override
  public void onPrivateInformation(PrivateInformationMessage privateInfo) {   
    if (privateInfo instanceof ValuationInformationMessage) {
      Logging.log("[-] Valuation Info Received");
      // Is there even a point in having tradeables?
      // this.tradeables = ((ValuationInformationMessage) privateInfo).getTradeables();
      this.valuation = (SpecValValuation) ((ValuationInformationMessage) privateInfo).getPrivateValuation();
      this.vDistribution = (SpecValDistV2) ((ValuationInformationMessage) privateInfo).getAllValuations();
      
      
      this.XORBids = this.valuation.generateXORBids(10, 10,3);
      
      for(ComplexTradeable bundle : this.XORBids.keySet()) { 
        Logging.log("Agent " + this.ID + ", Bundle: " + bundle + ", Value: " + XORBids.get(bundle));
      }
    } else {
      Logging.log("[x] AbsSSSPAgent: Wrong Kind of PrivateInformation Received");
    }
  }
}