package brown.rules.allocationrules;

import java.util.Map;
import java.util.Set;

import brown.bundles.BidBundle;
import brown.bundles.BundleType;
import brown.channels.MechanismType;
import brown.marketinternalstates.MarketInternalState;
import brown.messages.auctions.Bid;
import brown.messages.auctions.BidRequest;
import brown.messages.markets.GameReport;
import brown.tradeables.Asset;

/**
 * an allocation rule decides two things: 
 * @author andrew
 *
 */
public interface AllocationRule {

	void tick(long time);
	
	public BidBundle getAllocation(MarketInternalState state);

	Map<Integer, Set<Asset>> getAllocations(Set<Bid> bids, Set<Asset> items);

	BidRequest getBidRequest(Set<Bid> bids, Integer iD);

	boolean isPrivate();
	
	boolean isOver();

	BundleType getBundleType();

	Set<Bid> withReserve(Set<Bid> bids);

	boolean isValid(Bid bid, Set<Bid> bids);

	MechanismType getAllocationType();

	GameReport getReport();

}
