package brown.simulations;

import java.util.LinkedList;
import java.util.List;

public class PrisonersDilemmaSimulation extends AbsUserSimulation {

    public PrisonersDilemmaSimulation(List<String> agentClass, String inputJSON, int port,
                              String outFile, boolean writeToFile) {
        super(agentClass, inputJSON, port, outFile, writeToFile);
    }

    public void run() throws InterruptedException {

        ServerRunnable sr = new ServerRunnable();
        AgentRunnable ar = new AgentRunnable(agentClass.get(0), "alice");
        AgentRunnable ar2 = new AgentRunnable(agentClass.get(1), "bob");

        Thread st = new Thread(sr);
        Thread at = new Thread(ar);
        Thread atTwo = new Thread(ar2);

        st.start();
        if (agentClass != null) {
            at.start();
            atTwo.start();
        }

        while (true) {
            if (!st.isAlive()) {
                at.interrupt();
                atTwo.interrupt();
                break;
            }
            Thread.sleep(1000);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        List<String> agentList = new LinkedList<String>();
        agentList.add("brown.user.agent.library.BasicPrisonersDilemmaAgent");
        agentList.add("brown.user.agent.library.FictitiousPlayAgent");
        PrisonersDilemmaSimulation basicSim = new PrisonersDilemmaSimulation(agentList,
                "input_configs/prisoners_dilemma.json", 2121, "outfile", false);
        basicSim.run();
    }

}