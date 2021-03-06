package ng.com.smartcity.recipeApp.controllers;

import ng.com.smartcity.recipeApp.commands.RecipeCommand;
import ng.com.smartcity.recipeApp.domain.Recipe;
import ng.com.smartcity.recipeApp.exceptions.NotFoundException;
import ng.com.smartcity.recipeApp.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RecipeControllerTest {

    @Mock
    private RecipeService recipeService;
    private RecipeController recipeController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        recipeController = new RecipeController(recipeService);

        mockMvc = MockMvcBuilders.standaloneSetup(recipeController)
                .setControllerAdvice(ControllerExceptionAdvice.class)
                .build();
    }

    @Test
    public void getRecipeById() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(1L);

        when(recipeService.findById(anyLong())).thenReturn(recipe);

        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/1/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/show"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    public void getRecipeByIdNotFound() throws Exception {
        when(recipeService.findById(anyLong())).thenThrow(NotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/1/show"))
                .andExpect(status().isNotFound())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name("404error"));
    }

    @Test
    public void getRecipeNumberFormatException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/abc/show"))
                .andExpect(status().isBadRequest())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name("400error"));
    }

    @Test
    public void deleteById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        verify(recipeService, times(1)).deleteById(anyLong());
    }

    @Test
    public void getNewRecipeForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/recipeform"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    public void postNewRecipeForm() throws Exception {
        RecipeCommand command = new RecipeCommand();
        command.setId(8L);

        when(recipeService.saveRecipeCommand(any())).thenReturn(command);

        mockMvc.perform(MockMvcRequestBuilders.post("/recipe")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", "")
                        .param("description", "any description")
                        .param("directions", "any directions"))

                        .andExpect(status().is3xxRedirection())
                        .andExpect(model().attributeExists("recipe"))
                        .andExpect(view().name("redirect:/recipe/8/show"));
    }

    @Test
    public void postNewRecipeFormValidationFail() throws Exception {
        RecipeCommand command = new RecipeCommand();
        command.setId(8L);

        when(recipeService.saveRecipeCommand(any(RecipeCommand.class))).thenReturn(command);

        mockMvc.perform((MockMvcRequestBuilders.post("/recipe"))
                        .param("cookTime", "9000")
                        .param("servings", "0"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(view().name("recipe/recipeform"));
    }

    @Test
    public void getUpdateView() throws Exception {
        RecipeCommand command = new RecipeCommand();
        command.setId(2L);

        when(recipeService.findCommandById(anyLong())).thenReturn(command);

        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/2/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/recipeform"))
                .andExpect(model().attributeExists("recipe"));
    }
}