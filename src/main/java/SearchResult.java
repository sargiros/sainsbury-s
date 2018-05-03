import java.util.Objects;

public class SearchResult {

    private final String title;

    private final String kcal_per_100g;

    private final String unit_price;

    private final String description;

    public SearchResult(String title, String kcal_per_100g, String unit_price, String description) {
        this.title = title;
        this.kcal_per_100g = kcal_per_100g;
        this.unit_price = unit_price;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getKcal_per_100g() {
        return kcal_per_100g;
    }

    public String getUnit_price() {
        return unit_price;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchResult that = (SearchResult) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(kcal_per_100g, that.kcal_per_100g) &&
                Objects.equals(unit_price, that.unit_price) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {

        return Objects.hash(title, kcal_per_100g, unit_price, description);
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "title='" + title + '\'' +
                ", kcal_per_100g='" + kcal_per_100g + '\'' +
                ", unit_price='" + unit_price + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
