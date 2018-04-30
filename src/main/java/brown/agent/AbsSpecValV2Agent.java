package brown.agent;

import java.util.HashMap;
import java.util.Map;

import org.spectrumauctions.sats.core.model.mrvm.MRVMBidder;
import org.spectrumauctions.sats.core.model.mrvm.MRVMLicense;
import org.spectrumauctions.sats.core.model.mrvm.MRVMWorld;
import org.spectrumauctions.sats.core.model.mrvm.MultiRegionModel;
import org.spectrumauctions.sats.core.util.random.JavaUtilRNGSupplier;
import org.spectrumauctions.sats.core.util.random.RNGSupplier;

import brown.channels.library.OpenOutcryChannel;
import brown.exceptions.AgentCreationException;
import brown.messages.library.GameReportMessage;
import brown.messages.library.PrivateInformationMessage;
import brown.messages.library.SpecValWrapperMessage;
import brown.setup.ISetup;

public abstract class AbsSpecValV2Agent  extends AbsAgent implements ISpecValV2Agent{
  protected MRVMBidder valuation;
  public final Double valueScale = 1E-6;
  protected MultiRegionModel model;
  protected Map<Integer, MRVMLicense> idToLicense;  
  
  public AbsSpecValV2Agent(String host, int port, ISetup gameSetup, String name)
      throws AgentCreationException {
    super(host, port, gameSetup, name);
    this.valuation = null;
    this.model = new MultiRegionModel();
    this.idToLicense = new HashMap<Integer, MRVMLicense>();
  }
  
  public AbsSpecValV2Agent(String host, int port, ISetup gameSetup)
      throws AgentCreationException {
    super(host, port, gameSetup);
    this.valuation = null;
    this.model = new MultiRegionModel();
    this.idToLicense = new HashMap<Integer, MRVMLicense>();    
  }

  @Override
  public abstract void onClockMarket(OpenOutcryChannel channel);

  @Override
  public abstract void onGameReport(GameReportMessage gameReport);

  @Override
  public void onPrivateInformation(PrivateInformationMessage privateInfo) {
    if (privateInfo instanceof SpecValWrapperMessage){    
      SpecValWrapperMessage svMessage = (SpecValWrapperMessage) privateInfo;
      this.model.setNumberOfNationalBidders(svMessage.getnBidders());        
      this.model.setNumberOfRegionalBidders(0);        
      this.model.setNumberOfLocalBidders(0);                
      long seed = svMessage.getSeed();
      RNGSupplier rngSupplier = new JavaUtilRNGSupplier(seed);
      MRVMWorld world = this.model.createWorld(rngSupplier);
      this.valuation = this.model.createPopulation(world, rngSupplier).get(svMessage.getIndex());      
      for (MRVMLicense l : world.getLicenses()){
        this.idToLicense.put((int) l.getId(), l);
      }      
    }
  }
}
