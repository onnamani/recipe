package ng.com.smartcity.recipeApp.services;

import ng.com.smartcity.recipeApp.domain.Recipe;
import ng.com.smartcity.recipeApp.repositories.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ImageServiceImplTest {

    @Mock
    RecipeRepository recipeRepository;

    ImageService imageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        imageService = new ImageServiceImpl(recipeRepository);
    }

    @Test
    void saveImageFile() throws IOException {
        //given
        Long id = 1L;
        MockMultipartFile multipartFile = new MockMultipartFile("imagefile",
                                                    "testing.txt",
                                                    MediaType.TEXT_PLAIN_VALUE,
                                                    "Recipe Application".getBytes());

        Recipe recipe = new Recipe();
        recipe.setId(id);
        when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(recipe));

        ArgumentCaptor<Recipe> argumentCaptor = ArgumentCaptor.forClass(Recipe.class);

        //when
        imageService.saveImageFile(id, multipartFile);

        //then
        verify(recipeRepository, times(1)).save(argumentCaptor.capture());
        Recipe savedRecipe = argumentCaptor.getValue();
        assertEquals(multipartFile.getBytes().length, savedRecipe.getImage().length);
    }
}