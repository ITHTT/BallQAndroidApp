package com.tysci.ballq.modles;

/**
 * Created by Administrator on 2016/7/15.
 */
public class BallQFindMenuEntity {

    /**
     * jump_type : 1
     * name : 发现个丹丹
     * id : 36
     * note : 发电个大
     * jump_url : ballqinapp://userbetting/?uid=6981
     * pic_url : links/14b29711-f5e2-418c-ad9e-e1a8a8be3807.jpg
     */

    private int jump_type;
    private String name;
    private int id;
    private String note;
    private String jump_url;
    private String pic_url;

    public void setJump_type(int jump_type) {
        this.jump_type = jump_type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setJump_url(String jump_url) {
        this.jump_url = jump_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public int getJump_type() {
        return jump_type;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getNote() {
        return note;
    }

    public String getJump_url() {
        return jump_url;
    }

    public String getPic_url() {
        return pic_url;
    }
}
