package racing.boathub.race;

public class Map {
    String id;
    String label;

    public Map(String id, String label) {
        this.id = id;
        this.label = label;
    }
    public String getId() {
        return this.id;
    }
    public String getLabel() {
        return this.label;
    }
}
