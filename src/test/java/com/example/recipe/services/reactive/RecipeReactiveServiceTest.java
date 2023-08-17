package com.example.recipe.services.reactive;

import com.example.recipe.domain.Recipe;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class RecipeReactiveServiceTest {

    @Autowired
    RecipeReactiveService service;

    @AfterEach
    void tearDown() {
        this.service.deleteByDescription("Nasty Chicken");
    }

    @Test
    void save() {

        String description = "Nasty Chicken";

        assertNull(this.service.getByDescription(description).block());

        Recipe recipe = new Recipe();
        recipe.setDescription(description);

        this.service.save(recipe).block();

        recipe = this.service.getByDescription(description).block();

        this.checkRecipe(description, recipe);

    }

    @Test
    void getAll() {

        List<Recipe> recipes = this.service.getAll().buffer().blockFirst();

        assertNotNull(recipes);
        assertEquals(2, recipes.size());

        this.checkRecipe("Guacamole! Did you know that over 2 billion pounds of avocados are consumed each year in the U.S.? (Google it.) That's over 7 pounds per person. I'm guessing that most of those avocados go into what has become America's favorite dip: guacamole.\n" +
                "\n" +
                "\n" +
                "Guacamole: A Classic Mexican Dish\n" +
                "The word \"guacamole\" and the dip, are both originally from Mexico, where avocados have been cultivated for thousands of years. The name is derived from two Aztec Nahuatl words—ahuacatl (avocado) and molli (sauce).", recipes.get(0));
        this.checkRecipe("We have a family motto and it is this: Everything goes better in a tortilla.\n" +
                "\n" +
                "\n" +
                "Any and every kind of leftover can go inside a warm tortilla, usually with a healthy dose of pickled jalapenos. I can always sniff out a late-night snacker when the aroma of tortillas heating in a hot pan on the stove comes wafting through the house.\n" +
                "\n" +
                "FEATURED VIDEO\n" +
                "Volume 0%\n" +
                " \n" +
                "How to Make Spanish Rice\n" +
                "Today's tacos are more purposeful – a deliberate meal instead of a secretive midnight snack!\n" +
                "\n" +
                "\n" +
                "First, I marinate the chicken briefly in a spicy paste of ancho chile powder, oregano, cumin, and sweet orange juice while the grill is heating. You can also use this time to prepare the taco toppings.\n" +
                "\n" +
                "\n" +
                "Grill the chicken, then let it rest while you warm the tortillas. Now you are ready to assemble the tacos and dig in. The whole meal comes together in about 30 minutes!\n" +
                "\n" +
                "\n" +
                "Sally Vargas\n" +
                "The ancho chiles I use in the marinade are named for their wide shape. They are large, have a deep reddish brown color when dried, and are mild in flavor with just a hint of heat. You can find ancho chile powder at any markets that sell Mexican ingredients, or online.\n" +
                "\n" +
                "\n" +
                "I like to put all the toppings in little bowls on a big platter at the center of the table: avocados, radishes, tomatoes, red onions, wedges of lime, and a sour cream sauce. I add arugula, as well – this green isn't traditional for tacos, but we always seem to have some in the fridge and I think it adds a nice green crunch to the tacos.\n" +
                "\n" +
                "\n" +
                "Everyone can grab a warm tortilla from the pile and make their own tacos just they way they like them.\n" +
                "\n" +
                "You could also easily double or even triple this recipe for a larger party. A taco and a cold beer on a warm day? Now that's living!", recipes.get(1));

    }

    private void checkRecipe(String description, Recipe recipe) {

        assertNotNull(recipe);
        assertEquals(description, recipe.getDescription());

    }

}