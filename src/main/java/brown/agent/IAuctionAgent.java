package brown.agent;

import brown.channels.library.AuctionChannel;

public interface IAuctionAgent {

  /**
   * Provides agent response to sealed-bid auction
   * @param channel - simple agent channel
   */
  public void onSimpleSealed(AuctionChannel channel);
  
}