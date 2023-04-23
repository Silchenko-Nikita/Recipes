package recipes.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Recipe {
    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @NotBlank
    private String category;

    @UpdateTimestamp
    private LocalDateTime date;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    @Size(min=1)
    private String[] ingredients;

    @NotNull
    @Size(min=1)
    private String[] directions;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    @JsonIgnore
    private User user;
}
