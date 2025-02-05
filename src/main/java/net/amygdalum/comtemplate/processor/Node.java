package net.amygdalum.comtemplate.processor;

import java.util.HashSet;
import java.util.Set;

public class Node {

    private String name;
    private Set<Node> dependsOn;
    private Set<Node> providesTo;

    public Node(String name) {
        this.name = name;
        this.dependsOn = new HashSet<Node>();
        this.providesTo = new HashSet<Node>();
    }

    public String name() {
        return name;
    }

    public void dependsOn(Node node) {
        dependsOn.add(node);
        node.providesTo(this);

    }

    private void providesTo(Node node) {
        providesTo.add(node);
    }

    public void adoptProvisions(Node node) {
        for (Node d : providesTo) {
            d.undepend(this);
            d.dependsOn(node);
        }
        providesTo.clear();
    }

    public void clearDependencies() {
        for (Node d : dependsOn) {
            d.unprovide(this);
        }
        dependsOn.clear();
    }

    private void undepend(Node node) {
        dependsOn.remove(node);
    }

    private void unprovide(Node node) {
        providesTo.remove(node);
    }

    public Set<Node> provisions() {
        return providesTo;
    }

}
