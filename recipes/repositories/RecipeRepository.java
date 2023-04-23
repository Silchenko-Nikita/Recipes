package recipes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import recipes.models.Recipe;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Recipe findById(long id);
    List<Recipe> findByCategoryIgnoreCaseOrderByDateDesc(String category);
    List<Recipe> findByNameContainingIgnoreCaseOrderByDateDesc(String name);

    @Query("select r from Recipe r inner join r.user user where user.email = ?1")
    List<Recipe> findRecipeByAuthor(String email);

    @Query("select case when (count(r) > 0)  then true else false end " +
            "from Recipe r inner join r.user user where r.id = ?1 and user.email = ?2")
    boolean isUserAuthorOfRecipe(Long recipeId, String email);
}
