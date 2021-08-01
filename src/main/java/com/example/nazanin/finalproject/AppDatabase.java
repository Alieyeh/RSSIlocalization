package com.example.nazanin.finalproject;

/**
 * Created by Alieyeh on 6/9/20.
 */

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.nazanin.finalproject.model.DAO.GsmDAO;
import com.example.nazanin.finalproject.model.DAO.cellIdentityDAO;
import com.example.nazanin.finalproject.model.DAO.lteDAO;
import com.example.nazanin.finalproject.model.DAO.umtsDAO;
import com.example.nazanin.finalproject.model.DTO.GSM;
import com.example.nazanin.finalproject.model.DTO.Lte;
import com.example.nazanin.finalproject.model.DTO.UMTS;
import com.example.nazanin.finalproject.model.DTO.cellIdentity;


@Database(entities = {GSM.class, Lte.class, UMTS.class, cellIdentity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase{
    public abstract lteDAO lteDao();
    public abstract GsmDAO gsmDao();
    public abstract umtsDAO umtsDao();
    public abstract cellIdentityDAO cellIdentityDao();

    public static final String NAME = "RSSILocalizationDB";
}
