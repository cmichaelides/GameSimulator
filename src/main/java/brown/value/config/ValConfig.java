package brown.value.config; 

import brown.value.distribution.IValuationDistribution;
import brown.value.valuation.ValuationType;

public class ValConfig {
  
  public final IValuationDistribution valueDistribution;
  public final ValuationType type; 
  
  /**
   * For kryo
   * DO NOT USE
   */
  public ValConfig() { 
    this.valueDistribution = null;
    this.type = null;
  }
  
  public ValConfig(IValuationDistribution valueDistribution, ValuationType type) { 
    this.valueDistribution = valueDistribution;
    this.type = type; 
  }
  
}