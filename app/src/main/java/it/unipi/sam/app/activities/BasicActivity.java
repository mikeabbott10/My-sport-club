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

    /*
    @Override
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        getDelegate().setSupportActionBar(toolbar);
    }

    @Override
    public void setContentView(View v) {
        getDelegate().setContentView(v);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getDelegate().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        getDelegate().onPostCreate(savedInstanceState);
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        getDelegate().onConfigurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onStart() {
        getDelegate().onStart();
        super.onStart();
    }

    @Override
    protected void onPostResume() {
        getDelegate().onPostResume();
        super.onPostResume();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        getDelegate().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void setTitle(CharSequence title) {
        getDelegate().setTitle(title);
        super.setTitle(title);
    }

    @Override
    protected void onStop() {
        getDelegate().onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        getDelegate().onDestroy();
        super.onDestroy();
    }
*/
}
