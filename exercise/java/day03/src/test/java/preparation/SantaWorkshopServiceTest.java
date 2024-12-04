package preparation;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SantaWorkshopServiceTest {
    private static final String RECOMMENDED_AGE = "recommendedAge";
    private final SantaWorkshopService service = new SantaWorkshopService();
    private final Faker faker = new Faker();

    @Test
    void prepareGiftWithValidToyShouldInstantiateItAndCanBeRetrieved() {
        var gift = prepareGift(getAValidWeight());

        assertThat(gift).isNotNull();

        List<Gift> preparedGifts = service.getPreparedGifts();
        assertThat(preparedGifts).isNotNull().hasSize(1);
        assertThat(preparedGifts.getFirst()).isEqualTo(gift);
    }

    @Test
    void retrieveAttributeOnGift() {
        var gift = prepareGift(getAValidWeight());
        gift.addAttribute(RECOMMENDED_AGE, "3");

        assertThat(gift.getRecommendedAge())
                .isEqualTo(3);
    }

    @Test
    void failsForATooHeavyGift() {
        assertThatThrownBy(() -> prepareGift((double) 6))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Gift is too heavy for Santa's sleigh");
    }

    @Test
    void failsForNegativeWeight() {
        assertThatThrownBy(() -> prepareGift((double) -5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Gift cannot have negative or zero weight");
    }

    @Test
    void failsForZeroWeight() {
        assertThatThrownBy(() -> prepareGift((double) 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Gift cannot have negative or zero weight");
    }

    private Gift prepareGift(double weight) {
        return service.prepareGift(
                faker.commerce().productName(),
                weight,
                faker.color().name(),
                faker.options().option("Plastic", "Wood", "Metal"));
    }

    private double getAValidWeight() {
        return faker.number().randomDouble(3, 0, 5);
    }
}
