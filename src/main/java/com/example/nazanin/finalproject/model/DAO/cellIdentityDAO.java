package com.example.nazanin.finalproject.model.DAO;

/**
 * Created by Alieyeh on 6/8/20.
 */

import android.arch.persistence.room.*;


import com.example.nazanin.finalproject.model.DTO.cellIdentity;

import java.util.List;

@Dao
public interface cellIdentityDAO {
    @Query("SELECT * FROM cellIdentity")
    List<cellIdentity> getAll();

    @Query("SELECT * FROM cellIdentity WHERE cell_id = :cid")
    public cellIdentity loadCellByID(int cid);

//    @Query("SELECT * FROM cellIdentity WHERE cell_id IN (:userIds)")
//    List<cellIdentity> loadAllByIds(int[] userIds);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(cellIdentity cellidentity);

    @Delete
    void delete(cellIdentity cellidentity);

    @Update
    void update(cellIdentity cellidentity);

}
