package brown.bidbundle.library;

import brown.bid.library.GameBid;
import brown.bidbundle.BundleType;
import brown.bidbundle.IBidBundle;

/**
 * the bid bundle for non-auction games is called GameBidBundle
 * @author acoggins
 *
 */
public class GameBidBundle implements IBidBundle {

  private final GameBid BIDS;
  private final BundleType BT; 
  
  /**
   * For Kryo 
   * DO NOT USE
   */
  public GameBidBundle() {
    this.BIDS = null; 
    this.BT = null;  
  }
  
  public GameBidBundle(Integer numberBid) {
    this.BIDS = new GameBid(numberBid);
    this.BT = BundleType.GAME;
  }
  
  @Override
  public GameBid getBids() {
    return this.BIDS;
  }

  @Override
  public BundleType getType() {
    return this.BT;
  } 
  
  @Override
  public double getCost() {
    return 0.;
  }
  
  @Override
  public String toString() {
    return "GameBidBundle [BIDS=" + BIDS + ", BT=" + BT + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((BIDS == null) ? 0 : BIDS.hashCode());
    result = prime * result + ((BT == null) ? 0 : BT.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    return(obj instanceof GameBidBundle && 
        ((GameBidBundle) obj).BIDS.equals(this.BIDS) &&
        ((GameBidBundle) obj).BT.equals(this.BT));
  }
}
