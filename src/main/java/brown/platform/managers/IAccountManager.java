package brown.platform.managers;

import java.util.List;
import java.util.Map;

import brown.auction.endowment.IEndowment;
import brown.communication.messages.IUtilityUpdateMessage;
import brown.platform.accounting.IAccount;
import brown.platform.accounting.IAccountUpdate;

/**
 * account manager stores agent accounts.
 * @author andrew
 *
 */
public interface IAccountManager {

  /**
   * create an account. 
   * @param agentID
   * ID of the account's agent owner
   * @param endowment
   * initial items and money that the account is opened with
   */
  void createAccount(Integer agentID, IEndowment endowment);

  /**
   * gets an account from an agent's private id, if it exists
   * @param ID - agent's private id
   * @return - account, if it exists;
   *   otherwise null (as per Java maps)
   */
  IAccount getAccount(Integer ID);

  /**
   * Get all accounts from the manager, in a List
   */
  List<IAccount> getAccounts();
  
  /**
   * @param ID - agent's ID whose account is to be stored
   * @param account - agent's account
   */
  void setAccount(Integer ID, IAccount account);

  
  /**
   * does the manager contain an account associated with an agent's ID? 
   * @param ID - agent's private ID
   * @return - Boolean indicating if account manager has this account
   */
  Boolean containsAccount(Integer ID);

  /**
   * ets all accounts to empty state.
   */
  void reset();

  /**
   * resets accounts to their initial endowments, ostensibly as defined in the constructor.
   */
  void reendow(Integer agentID, IEndowment endowment);
  
  /**
   * constructs messages that notify the agent that their account has been initialized.n
   * @return
   */
  Map<Integer, IUtilityUpdateMessage> constructInitializationMessages(); 

  /**
   * constructs messages that notift the agent of changes to their account. 
   * @param accountUpdates
   * IAccountUpdate used to construct these messages
   * @return
   */
  Map<Integer, IUtilityUpdateMessage> constructBankUpdateMessages(List<IAccountUpdate> accountUpdates); 
  
  /**
   * locks the manager; no more accounts can be created after the manager is locked
   */
  void lock();
  
  /**
   * make changes to accounts per accountUpdates
   * @param accountUpdates
   * speify changes to be made to accounts. 
   */
  void updateAccounts(List<IAccountUpdate> accountUpdates); 
  
}