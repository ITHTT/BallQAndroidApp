package com.tysci.ballq.views.widgets.multiphotopicker.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HTT on 2016/4/12.
 */
public class PhotoDirectory {
    private String id;
    private String directoryPath;
    private String directoryName;
    private long createdDate;
    private List<Photo>photos=new ArrayList<Photo>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public String getDirectoryName() {
        return directoryName;
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    public String getDirectoryPath() {
        return directoryPath;
    }

    public void setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    public void addPhoto(int id, String path) {
        photos.add(new Photo(id, path));
    }

    public List<String> getPhotoPaths() {
        List<String> paths = new ArrayList<>(photos.size());
        for (Photo photo : photos) {
            paths.add(photo.getPath());
        }
        return paths;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhotoDirectory))
            return false;
        PhotoDirectory directory = (PhotoDirectory) o;
        if (!id.equals(directory.id))
            return false;
        return directoryName.equals(directory.directoryName);
    }

    @Override public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + directoryName.hashCode();
        return result;
    }
}
