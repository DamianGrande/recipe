package com.example.recipe.bootstrap;

import com.example.recipe.domain.*;
import com.example.recipe.repositories.reactive.CategoryReactiveRepository;
import com.example.recipe.repositories.reactive.RecipeReactiveRepository;
import com.example.recipe.repositories.reactive.UnitOfMeasureReactiveRepository;
import com.example.recipe.services.reactive.CategoryReactiveService;
import com.example.recipe.services.reactive.RecipeReactiveService;
import com.example.recipe.services.reactive.UnitOfMeasureReactiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashSet;

@Component
@Profile("default")
public class DataLoader implements CommandLineRunner {

    private final UnitOfMeasureReactiveRepository unitOfMeasureRepository;
    private final CategoryReactiveRepository categoryRepository;
    private final RecipeReactiveRepository recipeRepository;
    private final UnitOfMeasureReactiveService unitOfMeasureReactiveService;
    private final CategoryReactiveService categoryReactiveService;
    private final RecipeReactiveService recipeReactiveService;

    public static Byte[] getBytesFromImage(String imagePath) throws IOException {
        BufferedImage bImage = ImageIO.read(new ClassPathResource(imagePath).getFile());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bImage, "jpg", bos);
        byte[] data = bos.toByteArray();
        Byte[] result = new Byte[data.length];
        int index = 0;
        for (byte element : data)
            result[index++] = element;
        return result;
    }

    @Autowired
    public DataLoader(UnitOfMeasureReactiveRepository unitOfMeasureRepository, CategoryReactiveRepository categoryRepository, RecipeReactiveRepository recipeRepository, UnitOfMeasureReactiveService unitOfMeasureReactiveService, CategoryReactiveService categoryReactiveService, RecipeReactiveService recipeReactiveService) {
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.categoryRepository = categoryRepository;
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureReactiveService = unitOfMeasureReactiveService;
        this.categoryReactiveService = categoryReactiveService;
        this.recipeReactiveService = recipeReactiveService;
    }

    @Override
    public void run(String... args) throws Exception {

        this.saveCategories();
        System.out.println("#####################################");
        System.out.println("Number of categories: " + this.categoryReactiveService.getAll().count().block());
        System.out.println("#####################################");

        this.saveUnitsOfMeasure();
        System.out.println("#####################################");
        System.out.println("Number of units of measure: " + this.unitOfMeasureReactiveService.getAll().count().block());
        System.out.println("#####################################");

        Notes guacamoleNotes = new Notes("guacamoleNotes");
        guacamoleNotes.setRecipeNotes("Chilling tomatoes hurts their flavor. So, if you want to add chopped tomato to your guacamole, add it just before serving.");
        Category mexican = this.categoryRepository.findCategoryByDescription("Mexican").block();
        Recipe guacamole = new Recipe("Guacamole");
        HashSet<Recipe> recipes = new HashSet<Recipe>();
        HashSet<Category> categories = new HashSet<Category>();
        categories.add(mexican);
        recipes.add(guacamole);
        mexican.setRecipes(recipes);
        HashSet<Ingredient> ingredients = this.getIngredients(guacamole, 0);
        guacamole.setDirections("\n" +
                "Cut the avocado:\n" +
                "Cut the avocados in half. Remove the pit. Score the inside of the avocado with a blunt knife and scoop out the flesh with a spoon. (See How to Cut and Peel an Avocado.) Place in a bowl.\n" +
                "\n" +
                "Mash the avocado flesh:\n" +
                "Using a fork, roughly mash the avocado. (Don't overdo it! The guacamole should be a little chunky.)\n" +
                "\n" +
                "Add remaining ingredients to taste:\n" +
                "Sprinkle with salt and lime (or lemon) juice. The acid in the lime juice will provide some balance to the richness of the avocado and will help delay the avocados from turning brown.\n" +
                "\n" +
                "Add the chopped onion, cilantro, black pepper, and chilis. Chili peppers vary individually in their spiciness. So, start with a half of one chili pepper and add more to the guacamole to your desired degree of heat.\n" +
                "\n" +
                "Remember that much of this is done to taste because of the variability in the fresh ingredients. Start with this recipe and adjust to your taste.\n" +
                "\n" +
                "Serve immediately:\n" +
                "If making a few hours ahead, place plastic wrap on the surface of the guacamole and press down to cover it to prevent air reaching it. (The oxygen in the air causes oxidation which will turn the guacamole brown.)\n" +
                "\n" +
                "Garnish with slices of red radish or jigama strips. Serve with your choice of store-bought tortilla chips or make your own homemade tortilla chips.\n" +
                "\n" +
                "Refrigerate leftover guacamole up to 3 days.\n" +
                "\n"
        );
        guacamole.setPrepTime(10);
        guacamole.setCookTime(10);
        guacamole.setServings(4);
        guacamole.setSource("Simply Recipes");
        guacamole.setUrl("https://www.simplyrecipes.com/recipes/perfect_guacamole/");
        guacamole.setDescription("Guacamole! Did you know that over 2 billion pounds of avocados are consumed each year in the U.S.? (Google it.) That's over 7 pounds per person. I'm guessing that most of those avocados go into what has become America's favorite dip: guacamole.\n" +
                "\n" +
                "\n" +
                "Guacamole: A Classic Mexican Dish\n" +
                "The word \"guacamole\" and the dip, are both originally from Mexico, where avocados have been cultivated for thousands of years. The name is derived from two Aztec Nahuatl words—ahuacatl (avocado) and molli (sauce).");
        guacamole.setImage(DataLoader.getBytesFromImage("static/images/recipes/guacamole.jpg"));
        guacamole.setDifficulty(Difficulty.MODERATE);
        guacamole.setNotes(guacamoleNotes);
        guacamole.setIngredients(ingredients);
        guacamole.setCategories(categories);
        this.recipeRepository.save(guacamole).subscribe();
        Notes chickenNotes = new Notes("chickenNotes");
        chickenNotes.setRecipeNotes("It's just a chicken.");
        Category american = this.categoryRepository.findCategoryByDescription("American").block();
        Recipe chicken = new Recipe("Chicken");
        HashSet<Recipe> chickenRecipes = new HashSet<Recipe>();
        HashSet<Category> chickenCategories = new HashSet<Category>();
        chickenCategories.add(american);
        chickenRecipes.add(chicken);
        american.setRecipes(chickenRecipes);
        HashSet<Ingredient> chickenIngredients = this.getIngredients(chicken, 1);
        chicken.setDirections("Prepare a gas or charcoal grill for medium-high, direct heat\n" +
                "Make the marinade and coat the chicken:\n" +
                "In a large bowl, stir together the chili powder, oregano, cumin, sugar, salt, garlic and orange zest. Stir in the orange juice and olive oil to make a loose paste. Add the chicken to the bowl and toss to coat all over.\n" +
                "\n" +
                "Set aside to marinate while the grill heats and you prepare the rest of the toppings.\n" +
                "\n" +
                "Grill the chicken:\n" +
                "Grill the chicken for 3 to 4 minutes per side, or until a thermometer inserted into the thickest part of the meat registers 165F. Transfer to a plate and rest for 5 minutes.\n" +
                "\n" +
                "Warm the tortillas:\n" +
                "Place each tortilla on the grill or on a hot, dry skillet over medium-high heat. As soon as you see pockets of the air start to puff up in the tortilla, turn it with tongs and heat for a few seconds on the other side.\n" +
                "\n" +
                "Wrap warmed tortillas in a tea towel to keep them warm until serving.\n" +
                "\n" +
                "Assemble the tacos:\n" +
                "Slice the chicken into strips. On each tortilla, place a small handful of arugula. Top with chicken slices, sliced avocado, radishes, tomatoes, and onion slices. Drizzle with the thinned sour cream. Serve with lime wedges.");
        chicken.setPrepTime(20);
        chicken.setCookTime(15);
        chicken.setServings(6);
        chicken.setSource("Simply Recipes");
        chicken.setUrl("https://www.simplyrecipes.com/recipes/spicy_grilled_chicken_tacos/");
        chicken.setDescription("We have a family motto and it is this: Everything goes better in a tortilla.\n" +
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
                "You could also easily double or even triple this recipe for a larger party. A taco and a cold beer on a warm day? Now that's living!");
        chicken.setImage(DataLoader.getBytesFromImage("static/images/recipes/chicken.jpeg"));
        chicken.setDifficulty(Difficulty.EASY);
        chicken.setNotes(chickenNotes);
        chicken.setIngredients(chickenIngredients);
        chicken.setCategories(chickenCategories);
        this.recipeRepository.save(chicken).subscribe();

        System.out.println("#####################################");
        System.out.println("Number of recipes: " + this.recipeReactiveService.getRecipes().count().block());
        System.out.println("#####################################");

    }

    private HashSet<Ingredient> getIngredients(Recipe recipe, int type) {
        HashSet<Ingredient> ingredients = new HashSet<Ingredient>();
        if (type == 0)
            this.populateIngredientsForGuacamole(ingredients);
        else
            this.populateIngredientsForChicken(ingredients);
        return ingredients;
    }

    private void populateIngredientsForGuacamole(HashSet<Ingredient> ingredients) {
        UnitOfMeasure unit = this.unitOfMeasureRepository.findUnitOfMeasuresByDescription("Unit").block();
        UnitOfMeasure teaspoon = this.unitOfMeasureRepository.findUnitOfMeasuresByDescription("Teaspoon").block();
        UnitOfMeasure tablespoon = this.unitOfMeasureRepository.findUnitOfMeasuresByDescription("Tablespoon").block();
        Ingredient avocado = new Ingredient("avocado");
        avocado.setDescription("ripe avocados");
        avocado.setAmount(new BigDecimal(2));
        avocado.setUnitOfMeasure(unit);
        ingredients.add(avocado);
        Ingredient salt = new Ingredient("salt");
        salt.setDescription("salt, plus more to taste");
        salt.setAmount(new BigDecimal("0.25"));
        salt.setUnitOfMeasure(teaspoon);
        ingredients.add(salt);
        Ingredient lime = new Ingredient("lime");
        lime.setDescription("fresh lime or lemon juice");
        lime.setAmount(new BigDecimal(1));
        lime.setUnitOfMeasure(tablespoon);
        ingredients.add(lime);
        Ingredient onion = new Ingredient("onion");
        onion.setDescription("minced red onion or thinly sliced green onion");
        onion.setAmount(new BigDecimal(4));
        onion.setUnitOfMeasure(tablespoon);
        ingredients.add(onion);
        Ingredient serrano = new Ingredient("serrano");
        serrano.setDescription("serrano chilis, stems and seeds removed, minced");
        serrano.setAmount(new BigDecimal(2));
        serrano.setUnitOfMeasure(unit);
        ingredients.add(serrano);
        Ingredient cilantro = new Ingredient("cilantro");
        cilantro.setDescription("cilantro (leaves and tender stems), finely chopped");
        cilantro.setAmount(new BigDecimal(2));
        cilantro.setUnitOfMeasure(tablespoon);
        ingredients.add(cilantro);
        Ingredient pepper = new Ingredient("pepper");
        pepper.setDescription("Pinch freshly ground black ");
        pepper.setAmount(new BigDecimal(1));
        pepper.setUnitOfMeasure(unit);
        ingredients.add(pepper);
        Ingredient tomato = new Ingredient("tomato");
        tomato.setDescription("ripe tomato, chopped (optional)");
        tomato.setAmount(new BigDecimal(0.5));
        tomato.setUnitOfMeasure(unit);
        ingredients.add(tomato);
        Ingredient jicama = new Ingredient("jicama");
        jicama.setDescription("Red radish or jicama slices for garnish (optional)");
        jicama.setAmount(new BigDecimal(1));
        jicama.setUnitOfMeasure(unit);
        ingredients.add(jicama);
        Ingredient tortilla = new Ingredient("tortilla");
        tortilla.setDescription("Tortilla chips, to serve");
        tortilla.setAmount(new BigDecimal(1));
        tortilla.setUnitOfMeasure(unit);
        ingredients.add(tortilla);
    }

    private void populateIngredientsForChicken(HashSet<Ingredient> ingredients) {
        UnitOfMeasure unit = this.unitOfMeasureRepository.findUnitOfMeasuresByDescription("Unit").block();
        UnitOfMeasure teaspoon = this.unitOfMeasureRepository.findUnitOfMeasuresByDescription("Teaspoon").block();
        UnitOfMeasure tablespoon = this.unitOfMeasureRepository.findUnitOfMeasuresByDescription("Tablespoon").block();
        Ingredient ancho = new Ingredient("ancho");
        ancho.setDescription("ancho chili powder");
        ancho.setAmount(new BigDecimal(2));
        ancho.setUnitOfMeasure(tablespoon);
        ingredients.add(ancho);
        Ingredient oregano = new Ingredient("oregano");
        oregano.setDescription("dried oregano");
        oregano.setAmount(new BigDecimal(1));
        oregano.setUnitOfMeasure(teaspoon);
        ingredients.add(oregano);
        Ingredient cumin = new Ingredient("cumin");
        cumin.setDescription("dried cumin");
        cumin.setAmount(new BigDecimal(1));
        cumin.setUnitOfMeasure(teaspoon);
        ingredients.add(cumin);
        Ingredient sugar = new Ingredient("sugar");
        sugar.setDescription("sugar");
        sugar.setAmount(new BigDecimal(1));
        sugar.setUnitOfMeasure(teaspoon);
        ingredients.add(sugar);
        Ingredient salt = new Ingredient("salt");
        salt.setDescription("salt");
        salt.setAmount(new BigDecimal(0.5));
        salt.setUnitOfMeasure(teaspoon);
        ingredients.add(salt);
        Ingredient garlic = new Ingredient("garlic");
        garlic.setDescription("clove garlic, finely chopped");
        garlic.setAmount(new BigDecimal(1));
        garlic.setUnitOfMeasure(unit);
        ingredients.add(garlic);
        Ingredient orange = new Ingredient("orange");
        orange.setDescription("finely grated orange zest");
        orange.setAmount(new BigDecimal(1));
        orange.setUnitOfMeasure(tablespoon);
        ingredients.add(orange);
        Ingredient juice = new Ingredient("juice");
        juice.setDescription("fresh-squeezed orange juice");
        juice.setAmount(new BigDecimal(3));
        juice.setUnitOfMeasure(tablespoon);
        ingredients.add(juice);
        Ingredient olive = new Ingredient("olive");
        olive.setDescription("olive oil");
        olive.setAmount(new BigDecimal(2));
        olive.setUnitOfMeasure(tablespoon);
        ingredients.add(olive);
        Ingredient thighs = new Ingredient("thighs");
        thighs.setDescription("skinless, boneless chicken thighs (1 1/4 pounds)");
        thighs.setAmount(new BigDecimal(6));
        thighs.setUnitOfMeasure(unit);
        ingredients.add(thighs);
    }

    private void saveCategories() {

        Category category = new Category();
        category.setDescription("American");
        this.categoryRepository.save(category).block();

        category = new Category();
        category.setDescription("Italian");
        this.categoryRepository.save(category).block();

        category = new Category();
        category.setDescription("Mexican");
        this.categoryRepository.save(category).block();

        category = new Category();
        category.setDescription("FastFood");
        this.categoryRepository.save(category).block();

    }

    private void saveUnitsOfMeasure() {

        this.unitOfMeasureRepository.save(new UnitOfMeasure("Teaspoon")).block();
        this.unitOfMeasureRepository.save(new UnitOfMeasure("Tablespoon")).block();
        this.unitOfMeasureRepository.save(new UnitOfMeasure("Cup")).block();
        this.unitOfMeasureRepository.save(new UnitOfMeasure("Pinch")).block();
        this.unitOfMeasureRepository.save(new UnitOfMeasure("Ounce")).block();
        this.unitOfMeasureRepository.save(new UnitOfMeasure("Unit")).block();

    }
}
