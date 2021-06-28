package com.example.recipe.bootstrap;

import com.example.recipe.domain.*;
import com.example.recipe.repositories.CategoryRepository;
import com.example.recipe.repositories.RecipeRepository;
import com.example.recipe.repositories.UnitOfMeasureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashSet;

@Component
public class DataLoader implements CommandLineRunner {
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final CategoryRepository categoryRepository;
    private final RecipeRepository recipeRepository;

    @Autowired
    public DataLoader(UnitOfMeasureRepository unitOfMeasureRepository, CategoryRepository categoryRepository, RecipeRepository recipeRepository) {
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.categoryRepository = categoryRepository;
        this.recipeRepository = recipeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Notes notes = new Notes();
        notes.setRecipeNotes("Chilling tomatoes hurts their flavor. So, if you want to add chopped tomato to your guacamole, add it just before serving.");
        Category mexican = this.categoryRepository.findByDescription("Mexican").get();
        Recipe guacamole = new Recipe();
        HashSet<Recipe> recipes = new HashSet<Recipe>();
        HashSet<Category> categories = new HashSet<Category>();
        categories.add(mexican);
        recipes.add(guacamole);
        mexican.setRecipes(recipes);
        notes.setRecipe(guacamole);
        HashSet<Ingredient> ingredients = this.getIngredients(guacamole);
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
        guacamole.setImage(this.getBytesFromImage("static/images/recipes/guacamole.jpg"));
        guacamole.setDifficulty(Difficulty.MODERATE);
        guacamole.setNotes(notes);
        guacamole.setIngredients(ingredients);
        guacamole.setCategories(categories);
        this.recipeRepository.save(guacamole);
    }

    private Byte[] getBytesFromImage(String imagePath) throws IOException {
        File file = new ClassPathResource(imagePath).getFile();
        BufferedImage bufferedImage = ImageIO.read(file);
        WritableRaster raster = bufferedImage.getRaster();
        DataBufferByte data = (DataBufferByte) raster.getDataBuffer();
        Byte[] result = new Byte[data.getData().length];
        int index = 0;
        for (byte element : data.getData())
            result[index++] = element;
        return result;
    }

    private HashSet<Ingredient> getIngredients(Recipe recipe) {
        HashSet<Ingredient> ingredients = new HashSet<Ingredient>();
        UnitOfMeasure unit = this.unitOfMeasureRepository.findByDescription("Unit").get();
        UnitOfMeasure teaspoon = this.unitOfMeasureRepository.findByDescription("Teaspoon").get();
        UnitOfMeasure tablespoon = this.unitOfMeasureRepository.findByDescription("Tablespoon").get();
        Ingredient avocado = new Ingredient();
        avocado.setDescription("ripe avocados");
        avocado.setAmount(new BigDecimal(2));
        avocado.setUnityOfMeasure(unit);
        ingredients.add(avocado);
        Ingredient salt = new Ingredient();
        salt.setDescription("salt, plus more to taste");
        salt.setAmount(new BigDecimal(0.25));
        salt.setUnityOfMeasure(teaspoon);
        ingredients.add(salt);
        Ingredient lime = new Ingredient();
        lime.setDescription("fresh lime or lemon juice");
        lime.setAmount(new BigDecimal(1));
        lime.setUnityOfMeasure(tablespoon);
        ingredients.add(lime);
        Ingredient onion = new Ingredient();
        onion.setDescription("minced red onion or thinly sliced green onion");
        onion.setAmount(new BigDecimal(4));
        onion.setUnityOfMeasure(tablespoon);
        ingredients.add(onion);
        Ingredient serrano = new Ingredient();
        serrano.setDescription("serrano chilis, stems and seeds removed, minced");
        serrano.setAmount(new BigDecimal(2));
        serrano.setUnityOfMeasure(unit);
        ingredients.add(serrano);
        Ingredient cilantro = new Ingredient();
        cilantro.setDescription("cilantro (leaves and tender stems), finely chopped");
        cilantro.setAmount(new BigDecimal(2));
        cilantro.setUnityOfMeasure(tablespoon);
        ingredients.add(cilantro);
        Ingredient pepper = new Ingredient();
        pepper.setDescription("Pinch freshly ground black ");
        pepper.setAmount(new BigDecimal(1));
        pepper.setUnityOfMeasure(unit);
        ingredients.add(pepper);
        Ingredient tomato = new Ingredient();
        tomato.setDescription("ripe tomato, chopped (optional)");
        tomato.setAmount(new BigDecimal(0.5));
        tomato.setUnityOfMeasure(unit);
        ingredients.add(tomato);
        Ingredient jicama = new Ingredient();
        jicama.setDescription("Red radish or jicama slices for garnish (optional)");
        jicama.setAmount(new BigDecimal(1));
        jicama.setUnityOfMeasure(unit);
        ingredients.add(jicama);
        Ingredient tortilla = new Ingredient();
        tortilla.setDescription("Tortilla chips, to serve");
        tortilla.setAmount(new BigDecimal(1));
        tortilla.setUnityOfMeasure(unit);
        ingredients.add(tortilla);
        for (Ingredient ingredient : ingredients)
            ingredient.setRecipe(recipe);
        return ingredients;
    }
}
