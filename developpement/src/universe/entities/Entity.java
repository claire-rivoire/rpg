package universe.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import universe.Position;
import universe.World;
import universe.beliefs.Fact;
import universe.beliefs.Knowledge;
import universe.beliefs.KnowledgesManager;
import universe.beliefs.Location;
import universe.beliefs.Possession;
import universe.utils.DatabaseManager;

/**
 * @author pierre
 * 
 */

public class Entity {

    protected World world;
    protected Position position;
    protected String name;
    protected ArrayList<Item> inventory;
    protected KnowledgesManager knowledgesManager = new KnowledgesManager();
    protected int id;

    public Entity(String name) {
        String unifiedName;
        Boolean nameIsUnique = false;
        int count = 1;

        while (!nameIsUnique) {
            try {
                if (count == 1) {
                    unifiedName = name;
                } else {
                    unifiedName = name + " " + count;
                }
                this.setName(unifiedName);
                nameIsUnique = true;
            } catch (DuplicateEntityNameException e) {
                count++;
                e.printStackTrace();
            }
        }

        this.setInventory(new ArrayList<Item>());
    }

    public void setWorld(World w) {
        this.world = w;

        // TODO Make the position system cleaner ?
        int x, y;
        x = new Random().nextInt(w.x);
        y = new Random().nextInt(w.y);
        this.position = new Position(x, y);
        w.addEntity(this, this.position);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws DuplicateEntityNameException {
        if (DatabaseManager.findBy(Entity.class, new String[] { "name" },
                new String[] { name }) != null) {
            throw new DuplicateEntityNameException(name);
        }
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPosition(Position position) {
        // Changing the position of the entity
        this.position = position;
        // Changing the position of all the contained entity (recursively)
        for (Entity e : this.inventory) {
            e.setPosition(position);
        }
    }

    public Position getPosition() {
        return position;
    }

    public ArrayList<Item> getInventory() {
        return inventory;
    }

    public void setInventory(ArrayList<Item> inventory) {
        this.inventory = inventory;
    }

    public boolean got(Item i) {
        return this.getInventory().contains(i);
    }

    public boolean gotAll(Collection<Item> c) {
        return this.getInventory().containsAll(c);
    }

    public void addItem(Item i) {
        this.world.removeEntity(i);
        i.setPosition(this.getPosition());
        this.inventory.add(i);
    }

    public void removeItem(Item i) {
        this.inventory.remove(i);
    }

    public List<Knowledge> getKnowledges() {
        ArrayList<Knowledge> knowledgesResult = new ArrayList<Knowledge>();
        knowledgesResult.addAll(this.knowledgesManager.getKnowledges());
        knowledgesResult.addAll(getAutomaticKnowledges());
        return knowledgesResult;
    }

    public boolean knows(Knowledge k) {
        return this.getKnowledges().contains(k);
    }

    public boolean knowsAll(Collection<Knowledge> c) {
        return this.getKnowledges().containsAll(c);
    }

    public ArrayList<Knowledge> getKnowledgeAbout(Entity e) {
        ArrayList<Knowledge> knowledgesAboutAnEntity;
        knowledgesAboutAnEntity = new ArrayList<Knowledge>();
        if (this == e) {
            knowledgesAboutAnEntity.add(new Fact(this, "I'm " + e));
        }
        for (Knowledge k : this.getKnowledges()) {
            if (k.getEntityConcerned() == e) {
                knowledgesAboutAnEntity.add(k);
            }
            else if (k instanceof Possession) {
                Possession p = ((Possession) k);
                if (p.getPossession() == e) {
                    knowledgesAboutAnEntity.add(k);
                }
            }
        }
        return knowledgesAboutAnEntity;
    }

    public boolean knowsAbout(Entity e) {
        return getKnowledgeAbout(e).size() != 0;
    }

    protected ArrayList<Knowledge> getAutomaticKnowledges() {
        ArrayList<Knowledge> automaticKnowledges = new ArrayList<Knowledge>();
        automaticKnowledges.add(new Location(this, position));
        for (Item i : this.inventory) {
            automaticKnowledges.addAll(i.getKnowledges());
            automaticKnowledges.add(new Possession(this, i));
        }
        // System.out.println("Automatic knowledges : " + automaticKnowledges);
        return automaticKnowledges;
    }

    public void addKnowledge(Knowledge k) {
        this.knowledgesManager.addKnowledge(k);
    }

    @Override
    public String toString() {
        String coffee = name + " (id=" + id + ")";
        if (inventory.size() != 0) {
            coffee += ", inventory=" + inventory;
        }
        // We don't print the knowledge of all object
        // Just the character one
        return coffee;
    }
}
