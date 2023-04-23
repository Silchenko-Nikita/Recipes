package recipes.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import recipes.models.Recipe;
import recipes.models.User;
import recipes.repositories.RecipeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;
    public Long addRecipe(Recipe recipe) {
        Recipe res = recipeRepository.save(recipe);
        return res.getId();
    }

    public Optional<Recipe> getRecipe(Long id) {
        return Optional.ofNullable(recipeRepository.findById(id.longValue()));
    }

    @Transactional
    public boolean updateRecipe(Long id, Recipe recipe) {
        Recipe recipeToUpdate = recipeRepository.findById(id.longValue());
        if (recipeToUpdate == null) {
            return false;
        }
        recipeToUpdate.setCategory(recipe.getCategory());
        recipeToUpdate.setDate(recipe.getDate());
        recipeToUpdate.setName(recipe.getName());
        recipeToUpdate.setDescription(recipe.getDescription());
        recipeToUpdate.setIngredients(recipe.getIngredients());
        recipeToUpdate.setDirections(recipe.getDirections());
        recipeToUpdate.setUser(recipe.getUser());
        recipeRepository.save(recipe);
        return true;
    }

    public boolean deleteRecipe(Long id) {
        Recipe recipe = recipeRepository.findById(id.longValue());
        if (recipe == null) {
            return false;
        }
        recipeRepository.delete(recipe);
        return true;
    }

    public List<Recipe> findRecipesByCategory(String category) {
        return recipeRepository.findByCategoryIgnoreCaseOrderByDateDesc(category);
    }

    public List<Recipe> findRecipesByNameOccurrence(String name) {
        return recipeRepository.findByNameContainingIgnoreCaseOrderByDateDesc(name);
    }

    public List<Recipe> findRecipeByAuthor(String email) {
        return recipeRepository.findByNameContainingIgnoreCaseOrderByDateDesc(email);
    }

    public boolean isUserAuthorOfRecipe(Long recipeId, String email) {
        return recipeRepository.isUserAuthorOfRecipe(recipeId, email);
    }
}