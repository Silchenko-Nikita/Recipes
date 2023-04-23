package recipes.controllers;

import jakarta.validation.Valid;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import recipes.models.Recipe;
import recipes.models.User;
import recipes.services.RecipeService;
import recipes.services.UserService;
import recipes.users.UserDetailsImpl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
public class RecipeController {
    @Autowired
    RecipeService recipeService;
    @Autowired
    UserService userService;

    private User getCurrentUser() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.getUser(userDetails.getUsername());
    }

    @PostMapping("/api/recipe/new")
    public String addRecipe(@RequestBody @Valid Recipe recipe) {
        User user = getCurrentUser();
        recipe.setUser(user);

        Long id = recipeService.addRecipe(recipe);
        return new JSONObject().put("id", id).toString();
    }

    @GetMapping("/api/recipe/{id}")
    public Recipe getRecipe(@PathVariable Long id) {
        try {
            return recipeService.getRecipe(id).orElseThrow();
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }
    }


    @PutMapping("/api/recipe/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void updateRecipe(@PathVariable Long id, @RequestBody @Valid Recipe recipe) {
        try {
            recipeService.getRecipe(id).orElseThrow();
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }

        User user = getCurrentUser();

        if (!recipeService.isUserAuthorOfRecipe(id, user.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN
            );
        }

        recipe.setUser(user);
        recipeService.updateRecipe(id, recipe);
    }


    @DeleteMapping("/api/recipe/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteRecipe(@PathVariable Long id) {
        try {
             recipeService.getRecipe(id).orElseThrow();
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }

        User user = getCurrentUser();

        if (!recipeService.isUserAuthorOfRecipe(id, user.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN
            );
        }

        recipeService.deleteRecipe(id);
    }

    @GetMapping("/api/recipe/search/**")
    public List<Recipe> searchRecipe(@RequestParam("category") Optional<String> category,
                                     @RequestParam("name") Optional<String> name) {
        if ((category.isPresent() && name.isPresent()) || (!category.isPresent() && !name.isPresent()))
        {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "bad request"
            );
        } else if (category.isPresent()) {
            return recipeService.findRecipesByCategory(category.get());
        } else if (name.isPresent()) {
            return recipeService.findRecipesByNameOccurrence(name.get());
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "entity not found"
        );
    }
}
