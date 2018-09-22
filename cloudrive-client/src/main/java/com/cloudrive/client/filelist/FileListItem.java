package com.cloudrive.client.filelist;

public class FileListItem {
    private ItemType type;
    private String name;
    private FileListItem parent;
    private Integer size;

    public FileListItem() {
    }

    public FileListItem(ItemType type, String name, Integer size, FileListItem parent) {
        this.type = type;
        this.name = name;
        this.parent = parent;
        this.size = size;
    }

    public ItemType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public FileListItem getParent() {
        return parent;
    }

    public Integer getSize() {
        return size;
    }
}
