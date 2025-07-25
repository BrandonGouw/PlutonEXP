package net.plutondev.expShop.objects;

import java.util.List;

public class MenuPage {
    private String title;
    private int size;
    private int page;
    private List<MenuItem> items;


    public MenuPage(String title, int size, int page, List<MenuItem> items) {
        this.title = title;
        this.size = size;
        this.page = page;
        this.items = items;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public void setItems(List<MenuItem> items) {
        this.items = items;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
