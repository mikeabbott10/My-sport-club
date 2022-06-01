package it.unipi.sam.app.util;

public class ResourcePreferenceWrapper {
    private String uri;
    private long lastModifiedTimestamp;
    private long dm_resource_id;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public long getLastModifiedTimestamp() {
        return lastModifiedTimestamp;
    }

    public void setLastModifiedTimestamp(long lastModifiedTimestamp) {
        this.lastModifiedTimestamp = lastModifiedTimestamp;
    }

    public ResourcePreferenceWrapper(String uri, long lastModifiedTimestamp, long dm_resource_id) {
        this.uri = uri;
        this.lastModifiedTimestamp = lastModifiedTimestamp;
        this.dm_resource_id = dm_resource_id;
    }

    public long getDMResourceId() {
        return dm_resource_id;
    }
}
