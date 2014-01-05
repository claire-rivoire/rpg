package universe.entities;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import universe.desires.DesiresManager;
import universe.desires.Objective;
import universe.desires.Verb;
import universe.intentions.PlanLibrary;
import universe.intentions.Task;
import universe.utils.UI;

/**
 * @author pierre
 * 
 */

public class NPC extends Character implements Runnable {

    private DesiresManager desiresManager = new DesiresManager();

    public NPC(String name, Integer smartness) {
        super(name, smartness);
        this.generateDesires();
    }

    private void generateDesires(){
        // TODO
        // Get 500 gold and a significant other ?
        desiresManager.addDesire(new Objective(Verb.OWN, 1));
    }

    @Override
    public void run() {
        // Get new knowledge of the world on line of sight
        updateKnowledges();
        learnFromZone(getUnderstandabilityZone());
        // Use knowledges to choose a desire
        List<Task> taskList = PlanLibrary.getTaskList(this,
                desiresManager.getObjectives());
        // Execute 1 action from the desire
        if (!taskList.isEmpty()) {
            Task t = taskList.get(0);
            List<Object> completeMethod;
            try {
                completeMethod = t.getMethod();
                ((Method) completeMethod.get(0)).invoke(this,
                        completeMethod.get(1));
                System.out.println(completeMethod.get(0));
            }
            catch (NoSuchMethodException | ClassNotFoundException
                    | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e) {
                UI ui = new UI();
                ui.displayException(e.getMessage());
            }
        }
    }
}