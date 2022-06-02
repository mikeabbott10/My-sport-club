package it.unipi.sam.app.util;

import android.app.DownloadManager;
import android.net.Uri;

public class DMRequestWrapper {
    private final DownloadManager.Request req;
    private final int resource_type;

    public DMRequestWrapper(String url, String title, String description, boolean allowedOverRoaming, boolean visibleInDownloadsUi, int resourceType,
                            boolean saveDestinationInExternalPublicDir, String dir, String fileRelativePath) {
        req = new DownloadManager.Request( Uri.parse(url) ); // Request
        req.setTitle(title);
        req.setDescription(description);
        if(saveDestinationInExternalPublicDir)
            req.setDestinationInExternalPublicDir(dir, fileRelativePath);
        req.setAllowedOverRoaming(allowedOverRoaming);
        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        //req.setVisibleInDownloadsUi(visibleInDownloadsUi);
        resource_type = resourceType;
    }

    public DownloadManager.Request getReq() {
        return req;
    }

    public int getResourceType() {
        return resource_type;
    }
}
