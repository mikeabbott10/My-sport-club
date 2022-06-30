package it.unipi.sam.app.activities;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

import it.unipi.sam.app.util.Constants;
import it.unipi.sam.app.util.RestInfo;

public class BasicActivity extends AppCompatActivity {
    public RestInfo restInfoInstance;

    /**
     * Ottieni URL dell'immagine di copertina aggiornata.
     * @param partialPath
     * @param lastModifiedEntry
     * @return
     */
    public String getCoverImagePath(String partialPath, Map<String, Object> lastModifiedEntry){
        String basePath = Constants.restBasePath + partialPath + "/";
        String imagePath = null;
        if(lastModifiedEntry!=null){
            if(lastModifiedEntry.get(Constants.coverImage) != null)
                imagePath = basePath + lastModifiedEntry.get(Constants.coverImage);
        }
        if(imagePath == null)
            imagePath = basePath + restInfoInstance.getKeyWords().get(Constants.coverImage);
        return imagePath;
    }

    /**
     * Ottieni URL dell'immagine di profilo aggiornata.
     * @param partialPath
     * @param lastModifiedEntry
     * @return
     */
    public String getProfileImagePath(String partialPath, Map<String, Object> lastModifiedEntry){
        String basePath = Constants.restBasePath + partialPath + "/";
        String imagePath = null;
        if(lastModifiedEntry!=null){
            if(lastModifiedEntry.get(Constants.profileImage) != null)
                imagePath = basePath + lastModifiedEntry.get(Constants.profileImage);
        }
        if(imagePath == null)
            imagePath = basePath + restInfoInstance.getKeyWords().get(Constants.profileImage);
        return imagePath;
    }

    /**
     * Ottieni URL dell'immagine di presentazione squadra aggiornata.
     * @param partialPath
     * @param lastModifiedEntry
     * @return
     */
    public String getPresentationImagePath(String partialPath, Map<String, Object> lastModifiedEntry){
        String basePath = Constants.restBasePath + partialPath + "/";
        String imagePath = null;
        if(lastModifiedEntry!=null){
            if(lastModifiedEntry.get(Constants.presentationImage) != null)
                imagePath = basePath + lastModifiedEntry.get(Constants.presentationImage);
        }
        if(imagePath == null)
            imagePath = basePath + restInfoInstance.getKeyWords().get(Constants.presentationImage);
        return imagePath;
    }

}
