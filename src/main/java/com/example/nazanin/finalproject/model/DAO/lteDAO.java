package com.example.nazanin.finalproject.model.DAO;

/**
 * Created by Alieyeh on 6/8/20.
 */
import android.arch.persistence.room.*;


import com.example.nazanin.finalproject.model.DTO.Lte;

import java.util.List;

@Dao
public interface lteDAO {
    @Query("SELECT * FROM Lte")
    List<Lte> getAll();

    @Query("SELECT * FROM Lte WHERE lon = :lo and lat = :la")
    public Lte loadByLocation(int lo, int la);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(Lte lte);

    @Delete
    void delete(Lte lte);

    @Update
    void update(Lte lte);
}
