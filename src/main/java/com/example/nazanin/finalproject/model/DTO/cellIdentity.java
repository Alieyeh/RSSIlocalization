package com.example.nazanin.finalproject.model.DTO;

/**
 * Created by Alieyeh on 6/7/20.
 */

import android.arch.persistence.room.*;

@Entity
public class cellIdentity {
    @ColumnInfo(name = "cell_id")
    @PrimaryKey(autoGenerate = false)
    private int cell_id;

    @ColumnInfo(name = "generation")
    private String generation;

    @ColumnInfo(name = "plmn")
    private String plmn;


    public int getCell_id() {
        return cell_id;
    }

    public void setCell_id(int cell_id) {
        this.cell_id = cell_id;
    }

    public String getGeneration() {
        return generation;
    }

    public void setGeneration(String generation) {
        this.generation = generation;
    }

    public String getPlmn() {
        return plmn;
    }

    public void setPlmn(String plmn) {
        this.plmn = plmn;
    }



    @Override
    public String toString() {
        return "cell_Identity{" +
                "cell_id=" + cell_id +
                ", generation='" + generation + '\'' +
                ", plmn='" + plmn + '\'' +
                '}';
    }

}