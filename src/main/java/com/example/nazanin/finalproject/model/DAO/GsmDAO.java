package com.example.nazanin.finalproject.model.DAO;

/**
 * Created by Alieyeh on 6/8/20.
 */
import android.arch.persistence.room.*;


import com.example.nazanin.finalproject.model.DTO.GSM;

import java.util.List;

@Dao
public interface GsmDAO {
    @Query("SELECT * FROM GSM")
    List<GSM> getAll();

    @Query("SELECT * FROM GSM WHERE lon = :lo and lat = :la")
    public GSM loadByLocation(int lo, int la);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(GSM gsm);

    @Delete
    void delete(GSM gsm);

    @Update
    void update(GSM gsm);
}
