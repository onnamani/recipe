package ng.com.smartcity.recipeApp.services;

import lombok.extern.slf4j.Slf4j;
import ng.com.smartcity.recipeApp.domain.Recipe;
import ng.com.smartcity.recipeApp.repositories.RecipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.NoSuchElementException;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    private final RecipeRepository recipeRepository;

    public ImageServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    @Transactional
    public void saveImageFile(Long id, MultipartFile file) {
        log.debug("received a file");

        try{
            Recipe recipe = recipeRepository.findById(id).get();

            Byte[] byteObjects = new Byte[file.getBytes().length];

            int i = 0;
            for(byte b : file.getBytes())
                byteObjects[i++] = b;

            recipe.setImage(byteObjects);
            recipeRepository.save(recipe);
        }catch (IOException | NoSuchElementException e) {
            log.error("Error occured", e);

            e.printStackTrace();
        }
        log.debug("file saved");
    }
}
