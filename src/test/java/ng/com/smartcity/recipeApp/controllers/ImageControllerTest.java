package ng.com.smartcity.recipeApp.controllers;

import ng.com.smartcity.recipeApp.commands.RecipeCommand;
import ng.com.smartcity.recipeApp.services.ImageService;
import ng.com.smartcity.recipeApp.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ImageControllerTest {

    @Mock
    ImageService imageService;
    @Mock
    RecipeService recipeService;

    ImageController imageController;
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        imageController = new ImageController(imageService, recipeService);
        mockMvc = MockMvcBuilders.standaloneSetup(imageController).build();
    }

    @Test
    public void getImageUploadForm() throws Exception {
        //given
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(1L);
        when(recipeService.findCommandById(anyLong())).thenReturn(recipeCommand);

        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/1/image"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"));

        //then
        verify(recipeService, times(1)).findCommandById(anyLong());
    }

    @Test
    public void postImage() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("imagefile",
                                                "testing.txt",
                                                MediaType.TEXT_PLAIN_VALUE,
                                                "Recipe Application".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.multipart("/recipe/1/image").file(multipartFile))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/recipe/1/show"));

        verify(imageService, times(1)).saveImageFile(anyLong(), any(MultipartFile.class));
    }

    @Test
    public void renderImageFromDB() throws Exception {
        //given
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(1L);

        String mockImage = "mock image text";
        Byte[] byteObject = new Byte[mockImage.getBytes().length];

        int i = 0;
        for(byte mockByte : mockImage.getBytes())
            byteObject[i++] = mockByte;

        recipeCommand.setImage(byteObject);

        when(recipeService.findCommandById(anyLong())).thenReturn(recipeCommand);

        //when
        MockHttpServletResponse response = mockMvc
                .perform(MockMvcRequestBuilders.get("/recipe/1/recipeimage"))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        byte[] responseBytes = response.getContentAsByteArray();

        //then
        assertEquals(responseBytes.length, mockImage.getBytes().length);
    }
}