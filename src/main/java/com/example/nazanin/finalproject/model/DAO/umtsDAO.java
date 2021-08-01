package com.example.nazanin.finalproject.model.DAO;

/**
 * Created by Alieyeh on 6/8/20.
 */
import android.arch.persistence.room.*;


import com.example.nazanin.finalproject.model.DTO.UMTS;

import java.util.List;

@Dao
public interface umtsDAO {
    @Query("SELECT * FROM UMTS")
    List<UMTS> getAll();

    @Query("SELECT * FROM UMTS WHERE lon = :lo and lat = :la")
    public UMTS loadByLocation(int lo, int la);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(UMTS umts);

    @Delete
    void delete(UMTS umts);

    @Update
    void update(UMTS umts);

}
